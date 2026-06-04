package dev.stranik.controller

import dev.stranik.data.dto.AddTrackRequestDto
import dev.stranik.data.dto.CreatePlaylistRequestDto
import dev.stranik.data.dto.UpdatePlaylistRequestDto
import dev.stranik.domain.usecases.AddTrackToPlaylistUseCase
import dev.stranik.domain.usecases.CreatePlaylistUseCase
import dev.stranik.domain.usecases.DeletePlaylistUseCase
import dev.stranik.domain.usecases.GetPlaylistUseCase
import dev.stranik.domain.usecases.RemoveTrackFromPlaylistUseCase
import dev.stranik.domain.usecases.SearchPlaylistsUseCase
import dev.stranik.domain.usecases.UpdatePlaylistUseCase
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.routing

class PlaylistsController(
    private val searchPlaylistsUseCase: SearchPlaylistsUseCase,
    private val getPlaylistUseCase: GetPlaylistUseCase,
    private val createPlaylistUseCase: CreatePlaylistUseCase,
    private val updatePlaylistUseCase: UpdatePlaylistUseCase,
    private val deletePlaylistUseCase: DeletePlaylistUseCase,
    private val addTrackToPlaylistUseCase: AddTrackToPlaylistUseCase,
    private val removeTrackFromPlaylistUseCase: RemoveTrackFromPlaylistUseCase,
) {
    fun configure(application: Application) {
        application.routing {
            get("/") {
                val userId = call.request.queryParameters["userId"]?.toLongOrNull()
                val q = call.request.queryParameters["q"]
                val publicOnly = call.request.queryParameters["publicOnly"]?.toBooleanStrictOrNull()
                val limit = call.request.queryParameters["limit"]?.toIntOrNull() ?: 20
                val cursor = call.request.queryParameters["cursor"]

                val playlists = searchPlaylistsUseCase(userId = userId, q = q, publicOnly = publicOnly, limit = limit, cursor = cursor)

                call.respond(playlists)
            }

            get("/{playlistId}") {
                val playlistIdParam = call.parameters["playlistId"]

                if (playlistIdParam == null) {
                    call.respond(HttpStatusCode.BadRequest, mapOf("error" to "playlistId не задан"))
                    return@get
                }

                val playlistId = playlistIdParam.toLongOrNull()

                if (playlistId == null) {
                    call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Неверный playlistId"))
                    return@get
                }

                val playlist = getPlaylistUseCase(playlistId)

                if (playlist == null) {
                    call.respond(HttpStatusCode.NotFound, mapOf("error" to "Плейлист не найден"))
                    return@get
                }

                call.respond(playlist)
            }

            post("/") {
                val jwtUserId = call.principal<JWTPrincipal>()?.payload?.getClaim("userId")?.asLong()

                if (jwtUserId == null) {
                    call.respond(HttpStatusCode.Unauthorized, mapOf("error" to "Пользователь не авторизован"))
                    return@post
                }

                val body = call.receive<CreatePlaylistRequestDto>()

                val created = createPlaylistUseCase.invoke(jwtUserId, body.title, body.description, body.isPublic, body.coverUrl)

                if (created == null) {
                    call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "Не удалось создать плейлист"))
                    return@post
                }

                call.respond(HttpStatusCode.Created, created)
            }

            put("/{playlistId}") {
                val playlistIdParam = call.parameters["playlistId"]

                if (playlistIdParam == null) {
                    call.respond(HttpStatusCode.BadRequest, mapOf("error" to "playlistId не задан"))
                    return@put
                }

                val playlistId = playlistIdParam.toLongOrNull()

                if (playlistId == null) {
                    call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Неверный playlistId"))
                    return@put
                }

                val jwtUserId = call.principal<JWTPrincipal>()?.payload?.getClaim("userId")?.asLong()

                if (jwtUserId == null) {
                    call.respond(HttpStatusCode.Unauthorized, mapOf("error" to "Пользователь не авторизован"))
                    return@put
                }

                val update = call.receive<UpdatePlaylistRequestDto>()

                val updated = updatePlaylistUseCase.invoke(jwtUserId, playlistId, update.title, update.description, update.isPublic)

                if (!updated) {
                    call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "Не удалось обновить плейлист"))
                    return@put
                }

                call.respond(HttpStatusCode.OK, mapOf("message" to "Плейлист успешно обновлён"))
            }

            delete("/{playlistId}") {
                val playlistIdParam = call.parameters["playlistId"]

                if (playlistIdParam == null) {
                    call.respond(HttpStatusCode.BadRequest, mapOf("error" to "playlistId не задан"))
                    return@delete
                }

                val playlistId = playlistIdParam.toLongOrNull()

                if (playlistId == null) {
                    call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Неверный playlistId"))
                    return@delete
                }

                val jwtUserId = call.principal<JWTPrincipal>()?.payload?.getClaim("userId")?.asLong()

                if (jwtUserId == null) {
                    call.respond(HttpStatusCode.Unauthorized, mapOf("error" to "Пользователь не авторизован"))
                    return@delete
                }

                val deleted = deletePlaylistUseCase.invoke(jwtUserId, playlistId)

                if (!deleted) {
                    call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "Не удалось удалить плейлист"))
                    return@delete
                }

                call.respond(HttpStatusCode.OK, mapOf("message" to "Плейлист успешно удалён"))
            }

            post("/{playlistId}/tracks") {
                val playlistIdParam = call.parameters["playlistId"]

                if (playlistIdParam == null) {
                    call.respond(HttpStatusCode.BadRequest, mapOf("error" to "playlistId не задан"))
                    return@post
                }

                val playlistId = playlistIdParam.toLongOrNull()

                if (playlistId == null) {
                    call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Неверный playlistId"))
                    return@post
                }

                val jwtUserId = call.principal<JWTPrincipal>()?.payload?.getClaim("userId")?.asLong()

                if (jwtUserId == null) {
                    call.respond(HttpStatusCode.Unauthorized, mapOf("error" to "Пользователь не авторизован"))
                    return@post
                }

                val body = call.receive<AddTrackRequestDto>()

                val playlist = getPlaylistUseCase(playlistId)

                if (playlist == null) {
                    call.respond(HttpStatusCode.NotFound, mapOf("error" to "Плейлист не найден"))
                    return@post
                }

                if (playlist.owner.id != jwtUserId) {
                    call.respond(HttpStatusCode.Forbidden, mapOf("error" to "Вы не можете изменять этот плейлист"))
                    return@post
                }

                val added = addTrackToPlaylistUseCase.invoke(playlistId, body.trackId, body.position)

                if (!added) {
                    call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "Не удалось добавить трек в плейлист"))
                    return@post
                }

                call.respond(HttpStatusCode.OK, mapOf("message" to "Трек добавлен"))
            }

            delete("/{playlistId}/tracks/{trackId}") {
                val playlistIdParam = call.parameters["playlistId"]
                val trackIdParam = call.parameters["trackId"]

                if (playlistIdParam == null || trackIdParam == null) {
                    call.respond(HttpStatusCode.BadRequest, mapOf("error" to "playlistId или trackId не заданы"))
                    return@delete
                }

                val playlistId = playlistIdParam.toLongOrNull()
                val trackId = trackIdParam.toLongOrNull()

                if (playlistId == null || trackId == null) {
                    call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Неверный playlistId или trackId"))
                    return@delete
                }

                val jwtUserId = call.principal<JWTPrincipal>()?.payload?.getClaim("userId")?.asLong()

                if (jwtUserId == null) {
                    call.respond(HttpStatusCode.Unauthorized, mapOf("error" to "Пользователь не авторизован"))
                    return@delete
                }

                val playlist = getPlaylistUseCase(playlistId)

                if (playlist == null) {
                    call.respond(HttpStatusCode.NotFound, mapOf("error" to "Плейлист не найден"))
                    return@delete
                }

                if (playlist.owner.id != jwtUserId) {
                    call.respond(HttpStatusCode.Forbidden, mapOf("error" to "Вы не можете изменять этот плейлист"))
                    return@delete
                }

                val removed = removeTrackFromPlaylistUseCase.invoke(playlistId, trackId)

                if (!removed) {
                    call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "Не удалось удалить трек из плейлиста"))
                    return@delete
                }

                call.respond(HttpStatusCode.OK, mapOf("message" to "Трек удалён"))
            }
        }
    }
}