package dev.stranik.controller

import dev.stranik.data.dto.UserInfoDto
import dev.stranik.di.AppContainer.followedArtistsRepository
import dev.stranik.domain.mapper.toUser
import dev.stranik.domain.usecases.GetAllFollowsUseCase
import dev.stranik.domain.usecases.GetAvatarUseCase
import dev.stranik.domain.usecases.GetUserInfoUseCase
import dev.stranik.domain.usecases.UpdateUserUseCase
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.patch
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.routing

class UsersController(
    private val userInfoUseCase: GetUserInfoUseCase,
    private val updateUserUseCase: UpdateUserUseCase,
    private val getAvatarUseCase: GetAvatarUseCase,
    private val getAllFollowsUseCase: GetAllFollowsUseCase
) {
    fun configure(application: Application) {
        application.routing {
            get("/{userId}") {
                val userId = call.parameters["userId"]!!.toLong()

                val info = userInfoUseCase.execute(userId)

                if (info == null) {
                    call.respond(HttpStatusCode.NotFound, mapOf("error" to "Информация или пользователь не найден"))
                    return@get
                }

                call.respond(info)
            }

            put("/{userId}") {
                val userId = call.parameters["userId"]!!.toLong()
                val jwtUserId = call.principal<JWTPrincipal>()?.payload?.getClaim("userId")?.asLong()

                if (userId != jwtUserId) {
                    call.respond(HttpStatusCode.Forbidden, mapOf("error" to "Вы не можете редактировать чужой профиль"))
                    return@put
                }

                val update = call.receive<UserInfoDto>()
                val result = updateUserUseCase.invoke(update.toUser(userId))

                if (!result) {
                    call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "Не удалось обновить информацию"))
                    return@put
                }

                call.respond(HttpStatusCode.OK, mapOf("message" to "Информация успешно обновлена"))
            }

            patch("/{userId}/avatar") {
                val userId = call.parameters["userId"]!!.toLong()

                val avatar = getAvatarUseCase.invoke(userId)

                if (avatar == null) {
                    call.respond(HttpStatusCode.NotFound, mapOf("error" to "Аватар не найден"))
                    return@patch
                }

                call.respond(avatar)
            }

            get("/me") {
                val jwtUserId = call.principal<JWTPrincipal>()?.payload?.getClaim("userId")?.asLong()

                if (jwtUserId == null) {
                    call.respond(HttpStatusCode.Unauthorized, mapOf("error" to "Пользователь не авторизован"))
                    return@get
                }

                val info = userInfoUseCase.execute(jwtUserId)

                if (info == null) {
                    call.respond(HttpStatusCode.NotFound, mapOf("error" to "Информация не найдена"))
                    return@get
                }

                call.respond(info)
            }

            get("/{userId}/followers") {
                val userId = call.parameters["userId"]!!.toLong()

                val followed = followedArtistsRepository.getFollowedArtists(userId)

                call.respond(followed)
            }

            get("/{userId}/recently-played") {
                val userId = call.parameters["userId"]!!
            }

            post("/{userId}/history") {
                val userId = call.parameters["userId"]!!
            }
        }
    }
}