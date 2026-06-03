package dev.stranik.di

import dev.stranik.controller.AlbumsController
import dev.stranik.controller.ArtistsController
import dev.stranik.controller.AuthController
import dev.stranik.controller.GenresController
import dev.stranik.controller.PlaylistsController
import dev.stranik.controller.RecommendationsController
import dev.stranik.controller.SearchController
import dev.stranik.controller.TracksController
import dev.stranik.controller.UsersController
import dev.stranik.data.repository.FollowedArtistsRepositoryImpl
import dev.stranik.data.repository.UserRepositoryImpl
import dev.stranik.domain.repository.FollowedArtistsRepository
import dev.stranik.domain.repository.UserRepository
import dev.stranik.domain.usecases.GetAllFollowsUseCase
import dev.stranik.domain.usecases.GetAvatarUseCase
import dev.stranik.domain.usecases.GetUserInfoUseCase
import dev.stranik.domain.usecases.LoginUseCase
import dev.stranik.domain.usecases.RegisterUseCase
import dev.stranik.domain.usecases.UpdateUserUseCase
import dev.stranik.security.PasswordHasher
import io.ktor.server.application.Application

object AppContainer {
    val userRepository: UserRepository by lazy { UserRepositoryImpl() }
    val followedArtistsRepository: FollowedArtistsRepository by lazy { FollowedArtistsRepositoryImpl() }

    val loginUseCase: LoginUseCase by lazy { LoginUseCase(userRepository, PasswordHasher) }
    val registerUseCase: RegisterUseCase by lazy { RegisterUseCase(userRepository, PasswordHasher) }
    val getUserInfoUseCase: GetUserInfoUseCase by lazy { GetUserInfoUseCase(userRepository) }
    val updateUserUseCase: UpdateUserUseCase by lazy { UpdateUserUseCase(userRepository) }
    val getAvatarUseCase: GetAvatarUseCase by lazy { GetAvatarUseCase(userRepository) }
    val getAllFollowsUseCase: GetAllFollowsUseCase by lazy { GetAllFollowsUseCase(followedArtistsRepository) }

    val authController: AuthController by lazy { AuthController(loginUseCase, registerUseCase, PasswordHasher) }
    val usersController: UsersController by lazy {
        UsersController(
            getUserInfoUseCase,
            updateUserUseCase,
            getAvatarUseCase,
            getAllFollowsUseCase
        )
    }
    val artistsController: ArtistsController by lazy { ArtistsController() }
    val albumsController: AlbumsController by lazy { AlbumsController() }
    val tracksController: TracksController by lazy { TracksController() }
    val genresController: GenresController by lazy { GenresController() }
    val playlistsController: PlaylistsController by lazy { PlaylistsController() }
    val recommendationsController: RecommendationsController by lazy { RecommendationsController() }
    val searchController: SearchController by lazy { SearchController() }
}

fun Application.appModule() {
    println("DI инициализирован")
}