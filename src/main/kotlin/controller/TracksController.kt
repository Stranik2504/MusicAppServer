package dev.stranik.controller

import dev.stranik.domain.usecases.GetTrackLikesCountUseCase
import dev.stranik.domain.usecases.GetTrackStreamUseCase
import dev.stranik.domain.usecases.GetTrackUseCase
import dev.stranik.domain.usecases.LikeTrackUseCase
import dev.stranik.domain.usecases.SearchTracksUseCase
import dev.stranik.domain.usecases.UnlikeTrackUseCase
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.response.respondRedirect
import io.ktor.server.response.respondOutputStream
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import java.io.RandomAccessFile
import java.nio.charset.StandardCharsets
import java.net.URI
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.security.MessageDigest
import java.util.concurrent.TimeUnit
import kotlin.math.max
import kotlin.math.min

class TracksController(
    private val searchTracksUseCase: SearchTracksUseCase,
    private val getTrackUseCase: GetTrackUseCase,
    private val getTrackStreamUseCase: GetTrackStreamUseCase,
    private val likeTrackUseCase: LikeTrackUseCase,
    private val unlikeTrackUseCase: UnlikeTrackUseCase,
    private val getTrackLikesCountUseCase: GetTrackLikesCountUseCase,
) {
    fun configure(route: Route) {
        route.apply {
            get("/") {
                val artistId = call.request.queryParameters["artistId"]?.toLongOrNull()
                val albumId = call.request.queryParameters["albumId"]?.toLongOrNull()
                val q = call.request.queryParameters["q"]
                val durationMin = call.request.queryParameters["durationMin"]?.toIntOrNull()
                val durationMax = call.request.queryParameters["durationMax"]?.toIntOrNull()
                val limit = call.request.queryParameters["limit"]?.toIntOrNull() ?: 20
                val cursor = call.request.queryParameters["cursor"]

                val tracks = searchTracksUseCase(
                    q = q,
                    artistId = artistId,
                    albumId = albumId,
                    durationMin = durationMin,
                    durationMax = durationMax,
                    limit = limit,
                    cursor = cursor,
                )

                call.respond(tracks)
            }

            get("/{trackId}/stream") {
                val trackIdParam = call.parameters["trackId"]

                if (trackIdParam == null) {
                    call.respond(HttpStatusCode.BadRequest, mapOf("error" to "trackId не задан"))
                    return@get
                }

                val trackId = trackIdParam.toLongOrNull()

                if (trackId == null) {
                    call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Неверный trackId"))
                    return@get
                }

                val url = getTrackStreamUseCase(trackId)

                if (url == null) {
                    call.respond(HttpStatusCode.NotFound, mapOf("error" to "Файл стрима не найден"))
                    return@get
                }

                if (isRemoteUrl(url)) {
                    call.respondRedirect(url)
                        return@get
                }

                val filePath = resolveLocalPath(url)

                if (filePath == null || !Files.exists(filePath) || !Files.isRegularFile(filePath)) {
                    call.respond(HttpStatusCode.NotFound, mapOf("error" to "Локальный файл не найден"))
                    return@get
                }

                streamFile(call, filePath)
            }

            get("/{trackId}/hls") {
                val trackIdParam = call.parameters["trackId"]

                if (trackIdParam == null) {
                    call.respond(HttpStatusCode.BadRequest, mapOf("error" to "trackId не задан"))
                    return@get
                }

                val trackId = trackIdParam.toLongOrNull()

                if (trackId == null) {
                    call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Неверный trackId"))
                    return@get
                }

                try {
                    val url = getTrackStreamUseCase(trackId)

                    if (url == null) {
                        call.respond(HttpStatusCode.NotFound, mapOf("error" to "Файл стрима не найден"))
                        return@get
                    }

                    if (isRemoteUrl(url)) {
                        if (url.endsWith(".m3u8", ignoreCase = true)) {
                            call.respondRedirect(url)
                            return@get
                        }

                        call.respond(
                            HttpStatusCode.BadGateway,
                            mapOf("error" to "Для удалённого источника доступен только готовый HLS manifest"),
                        )
                        return@get
                    }

                    val filePath = resolveLocalPath(url)

                    if (filePath == null || !Files.exists(filePath) || !Files.isRegularFile(filePath)) {
                        call.respond(HttpStatusCode.NotFound, mapOf("error" to "Локальный файл не найден"))
                        return@get
                    }

                    val hlsPackage = prepareHlsPackage(trackId, filePath)

                    if (hlsPackage == null) {
                        call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "Не удалось подготовить HLS"))
                        return@get
                    }

                    val manifestText = buildManifestText(trackId, hlsPackage.manifestFile)

                    call.response.headers.append("Cache-Control", "no-store")
                    call.respondText(manifestText, ContentType.parse("application/vnd.apple.mpegurl"), HttpStatusCode.OK)
                }
                catch (e: Exception) {
                    e.printStackTrace()
                    call.respond(HttpStatusCode.InternalServerError, mapOf("error" to (e.message ?: "Ошибка")))
                }
            }

            get("/{trackId}/hls/{segmentFile}") {
                val trackIdParam = call.parameters["trackId"]
                val segmentFile = call.parameters["segmentFile"]

                if (trackIdParam == null) {
                    call.respond(HttpStatusCode.BadRequest, mapOf("error" to "trackId не задан"))
                    return@get
                }

                val trackId = trackIdParam.toLongOrNull()

                if (trackId == null) {
                    call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Неверный trackId"))
                    return@get
                }

                if (segmentFile.isNullOrBlank() || segmentFile.contains("/") || segmentFile.contains("\\") || segmentFile.contains("..")) {
                    call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Неверный segmentFile"))
                    return@get
                }

                val url = getTrackStreamUseCase(trackId)

                if (url == null) {
                    call.respond(HttpStatusCode.NotFound, mapOf("error" to "Файл стрима не найден"))
                    return@get
                }

                val filePath = resolveLocalPath(url)

                if (filePath == null || !Files.exists(filePath) || !Files.isRegularFile(filePath)) {
                    call.respond(HttpStatusCode.NotFound, mapOf("error" to "Локальный файл не найден"))
                    return@get
                }

                val hlsPackage = prepareHlsPackage(trackId, filePath)

                if (hlsPackage == null) {
                    call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "Не удалось подготовить HLS"))
                    return@get
                }

                val segmentPath = hlsPackage.outputDir.resolve(segmentFile).normalize()

                if (!segmentPath.startsWith(hlsPackage.outputDir) || !Files.exists(segmentPath) || !Files.isRegularFile(segmentPath)) {
                    call.respond(HttpStatusCode.NotFound, mapOf("error" to "Сегмент не найден"))
                    return@get
                }

                serveSegment(call, segmentPath)
            }

            post("/{trackId}/like") {
                val trackIdParam = call.parameters["trackId"]
                val jwtUserId = call.principal<JWTPrincipal>()?.payload?.getClaim("userId")?.asLong()

                if (jwtUserId == null) {
                    call.respond(HttpStatusCode.Unauthorized, mapOf("error" to "Пользователь не авторизован"))
                    return@post
                }

                val trackId = trackIdParam?.toLongOrNull()

                if (trackId == null) {
                    call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Неверный trackId"))
                    return@post
                }

                val result = likeTrackUseCase.invoke(jwtUserId, trackId)

                if (!result) {
                    call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "Не удалось поставить лайк"))
                    return@post
                }

                call.respond(HttpStatusCode.OK, mapOf("message" to "Лайк добавлен"))
            }

            delete("/{trackId}/like") {
                val trackIdParam = call.parameters["trackId"]
                val jwtUserId = call.principal<JWTPrincipal>()?.payload?.getClaim("userId")?.asLong()

                if (jwtUserId == null) {
                    call.respond(HttpStatusCode.Unauthorized, mapOf("error" to "Пользователь не авторизован"))
                    return@delete
                }

                val trackId = trackIdParam?.toLongOrNull()

                if (trackId == null) {
                    call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Неверный trackId"))
                    return@delete
                }

                val result = unlikeTrackUseCase.invoke(jwtUserId, trackId)

                if (!result) {
                    call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "Не удалось удалить лайк"))
                    return@delete
                }

                call.respond(HttpStatusCode.OK, mapOf("message" to "Лайк удалён"))
            }

            get("/{trackId}/likes") {
                val trackIdParam = call.parameters["trackId"]

                if (trackIdParam == null) {
                    call.respond(HttpStatusCode.BadRequest, mapOf("error" to "trackId не задан"))
                    return@get
                }

                val trackId = trackIdParam.toLongOrNull()

                if (trackId == null) {
                    call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Неверный trackId"))
                    return@get
                }

                val count = getTrackLikesCountUseCase.invoke(trackId)

                call.respond(mapOf("likes" to count))
            }

            get("/{trackId}") {
                val trackIdParam = call.parameters["trackId"]

                if (trackIdParam == null) {
                    call.respond(HttpStatusCode.BadRequest, mapOf("error" to "trackId не задан"))
                    return@get
                }

                val trackId = trackIdParam.toLongOrNull()

                if (trackId == null) {
                    call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Неверный trackId"))
                    return@get
                }

                val track = getTrackUseCase(trackId)

                if (track == null) {
                    call.respond(HttpStatusCode.NotFound, mapOf("error" to "Трек не найден"))
                    return@get
                }

                call.respond(track)
            }
        }
    }

    private fun isRemoteUrl(url: String): Boolean {
        return url.startsWith("http://", ignoreCase = true) || url.startsWith("https://", ignoreCase = true)
    }

    private fun resolveLocalPath(url: String): Path? {
        return try {
            val rawPath = when {
                url.startsWith("file:", ignoreCase = true) -> Paths.get(URI.create(url))
                else -> Paths.get(url)
            }

            if (rawPath.isAbsolute) {
                rawPath
            } else {
                val mediaRoot = System.getenv("MEDIA_ROOT")?.takeIf { it.isNotBlank() }
                    ?: "/storage/music"
                Paths.get(mediaRoot).resolve(rawPath).normalize()
            }
        } catch (_: Exception) {
            null
        }
    }

    private data class HlsPackage(
        val outputDir: Path,
        val manifestFile: Path,
    )

    private fun prepareHlsPackage(trackId: Long, sourceFile: Path): HlsPackage? {
        val cacheRoot = Paths.get(System.getProperty("java.io.tmpdir"), "musicappserver-hls")
        val sourceFingerprint = buildSourceFingerprint(sourceFile)
        val outputDir = cacheRoot.resolve(trackId.toString()).resolve(sourceFingerprint)
        val manifestFile = outputDir.resolve("index.m3u8")
        val readyMarker = outputDir.resolve(".ready")

        if (Files.exists(manifestFile) && Files.exists(readyMarker)) {
            return HlsPackage(outputDir, manifestFile)
        }

        Files.createDirectories(outputDir)

        val segmentPattern = outputDir.resolve("segment_%03d.ts").toString()
        val process = ProcessBuilder(
            "ffmpeg",
            "-y",
            "-i",
            sourceFile.toString(),
            "-vn",
            "-c:a",
            "aac",
            "-b:a",
            "128k",
            "-hls_time",
            "6",
            "-hls_playlist_type",
            "vod",
            "-hls_segment_filename",
            segmentPattern,
            manifestFile.toString(),
        )
            .redirectErrorStream(true)
            .redirectOutput(ProcessBuilder.Redirect.DISCARD)
            .redirectError(ProcessBuilder.Redirect.DISCARD)
            .start()

        val finished = process.waitFor(90, TimeUnit.SECONDS)

        if (!finished) {
            process.destroyForcibly()
            return null
        }

        if (process.exitValue() != 0 || !Files.exists(manifestFile)) {
            return null
        }

        Files.writeString(readyMarker, "ok", StandardCharsets.UTF_8)
        return HlsPackage(outputDir, manifestFile)
    }

    private fun buildSourceFingerprint(sourceFile: Path): String {
        val absolute = sourceFile.toAbsolutePath().normalize().toString()
        val size = runCatching { Files.size(sourceFile) }.getOrDefault(0L)
        val modified = runCatching { Files.getLastModifiedTime(sourceFile).toMillis() }.getOrDefault(0L)
        val raw = "$absolute|$size|$modified"
        val digest = MessageDigest.getInstance("SHA-256").digest(raw.toByteArray(StandardCharsets.UTF_8))
        return digest.joinToString("") { "%02x".format(it) }
    }

    private fun buildManifestText(trackId: Long, manifestFile: Path): String {
        val basePath = "/api/tracks/$trackId/hls"
        return Files.readAllLines(manifestFile, StandardCharsets.UTF_8)
            .joinToString("\n") { line ->
                val trimmed = line.trim()
                when {
                    trimmed.isEmpty() -> line
                    trimmed.startsWith("#") -> line
                    else -> "$basePath/$trimmed"
                }
            } + "\n"
    }

    private suspend fun serveSegment(call: io.ktor.server.application.ApplicationCall, segmentPath: Path) {
        call.response.headers.append("Cache-Control", "public, max-age=31536000, immutable")
        call.response.headers.append("Accept-Ranges", "bytes")

        call.respondOutputStream(contentType = ContentType.parse("video/mp2t"), status = HttpStatusCode.OK) {
            Files.newInputStream(segmentPath).use { input ->
                input.copyTo(this)
            }
        }
    }

    private suspend fun streamFile(call: io.ktor.server.application.ApplicationCall, filePath: Path) {
        val fileSize = Files.size(filePath)
        val rangeHeader = call.request.headers["Range"]
        val range = parseRange(rangeHeader, fileSize)

        if (rangeHeader != null && range == null) {
            call.response.headers.append("Accept-Ranges", "bytes")
            call.response.headers.append("Content-Range", "bytes */$fileSize")
            call.respond(HttpStatusCode.RequestedRangeNotSatisfiable)
            return
        }

        val start = range?.start ?: 0L
        val end = range?.end ?: (fileSize - 1)
        val contentLength = if (fileSize == 0L) 0L else (end - start + 1)
        val status = if (range != null) HttpStatusCode.PartialContent else HttpStatusCode.OK

        call.response.headers.append("Accept-Ranges", "bytes")
        if (range != null) {
            call.response.headers.append("Content-Range", "bytes $start-$end/$fileSize")
        }

        call.respondOutputStream(contentType = ContentType.parse("audio/mpeg"), status = status) {
            if (fileSize == 0L) return@respondOutputStream

            RandomAccessFile(filePath.toFile(), "r").use { file ->
                file.seek(start)
                var remaining = contentLength
                val buffer = ByteArray(DEFAULT_BUFFER_SIZE)

                while (remaining > 0) {
                    val read = file.read(buffer, 0, min(buffer.size.toLong(), remaining).toInt())
                    if (read == -1) break

                    write(buffer, 0, read)
                    remaining -= read
                }
            }
        }
    }

    private data class ByteRange(val start: Long, val end: Long)

    private fun parseRange(rangeHeader: String?, fileSize: Long): ByteRange? {
        if (rangeHeader.isNullOrBlank() || !rangeHeader.startsWith("bytes=")) return null

        val value = rangeHeader.removePrefix("bytes=").trim()
        if (value.contains(',')) return null

        val parts = value.split('-', limit = 2)
        if (parts.isEmpty()) return null

        val startPart = parts[0].trim()
        val endPart = parts.getOrNull(1)?.trim().orEmpty()

        return when {
            startPart.isEmpty() && endPart.isNotEmpty() -> {
                val suffixLength = endPart.toLongOrNull() ?: return null
                if (suffixLength <= 0) return null

                val start = max(0L, fileSize - suffixLength)
                ByteRange(start, if (fileSize == 0L) 0L else fileSize - 1)
            }

            startPart.isNotEmpty() -> {
                val start = startPart.toLongOrNull() ?: return null
                val end = if (endPart.isNotEmpty()) endPart.toLongOrNull() ?: return null else if (fileSize == 0L) 0L else fileSize - 1

                if (fileSize == 0L || start < 0 || start >= fileSize || end < start) return null

                ByteRange(start, min(end, fileSize - 1))
            }

            else -> null
        }
    }
}