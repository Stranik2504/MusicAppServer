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
import dev.stranik.data.repository.AlbumRepositoryImpl
import dev.stranik.data.repository.ListeningHistoryRepositoryImpl
import dev.stranik.data.repository.UserRepositoryImpl
import dev.stranik.domain.repository.AlbumRepository
import dev.stranik.domain.repository.FollowedArtistsRepository
import dev.stranik.domain.repository.ListeningHistoryRepository
import dev.stranik.domain.repository.UserRepository
import dev.stranik.domain.usecases.AddListeningHistoryUseCase
import dev.stranik.domain.usecases.GetAlbumUseCase
import dev.stranik.domain.usecases.GetAllFollowsUseCase
import dev.stranik.domain.usecases.GetAvatarUseCase
import dev.stranik.domain.usecases.GetListeningHistoryUseCase
import dev.stranik.domain.usecases.GetUserInfoUseCase
import dev.stranik.domain.usecases.LoginUseCase
import dev.stranik.domain.usecases.RegisterUseCase
import dev.stranik.domain.usecases.SearchAlbumsUseCase
import dev.stranik.domain.usecases.UpdateUserUseCase
import dev.stranik.security.PasswordHasher
import io.ktor.server.application.Application

object AppContainer {
    val userRepository: UserRepository by lazy { UserRepositoryImpl() }
    val albumRepository: AlbumRepository by lazy { AlbumRepositoryImpl() }
    val followedArtistsRepository: FollowedArtistsRepository by lazy { FollowedArtistsRepositoryImpl() }
    val listeningHistoryRepository: ListeningHistoryRepository by lazy { ListeningHistoryRepositoryImpl() }

    val loginUseCase: LoginUseCase by lazy { LoginUseCase(userRepository, PasswordHasher) }
    val registerUseCase: RegisterUseCase by lazy { RegisterUseCase(userRepository, PasswordHasher) }
    val getUserInfoUseCase: GetUserInfoUseCase by lazy { GetUserInfoUseCase(userRepository) }
    val updateUserUseCase: UpdateUserUseCase by lazy { UpdateUserUseCase(userRepository) }
    val getAvatarUseCase: GetAvatarUseCase by lazy { GetAvatarUseCase(userRepository) }
    val searchAlbumsUseCase: SearchAlbumsUseCase by lazy { SearchAlbumsUseCase(albumRepository) }
    val getAlbumUseCase: GetAlbumUseCase by lazy { GetAlbumUseCase(albumRepository) }
    val getAllFollowsUseCase: GetAllFollowsUseCase by lazy { GetAllFollowsUseCase(followedArtistsRepository) }
    val getListeningHistoryUseCase: GetListeningHistoryUseCase by lazy { GetListeningHistoryUseCase(listeningHistoryRepository) }
    val addListeningHistoryUseCase: AddListeningHistoryUseCase by lazy { AddListeningHistoryUseCase(listeningHistoryRepository) }

    val authController: AuthController by lazy { AuthController(loginUseCase, registerUseCase, PasswordHasher) }
    val usersController: UsersController by lazy {
        UsersController(
            getUserInfoUseCase,
            updateUserUseCase,
            getAvatarUseCase,
            getAllFollowsUseCase,
            getListeningHistoryUseCase,
            addListeningHistoryUseCase
        )
    }
    val albumsController: AlbumsController by lazy { AlbumsController(searchAlbumsUseCase, getAlbumUseCase) }
    val artistsController: ArtistsController by lazy { ArtistsController() }
    val tracksController: TracksController by lazy { TracksController() }
    val genresController: GenresController by lazy { GenresController() }
    val playlistsController: PlaylistsController by lazy { PlaylistsController() }
    val recommendationsController: RecommendationsController by lazy { RecommendationsController() }
    val searchController: SearchController by lazy { SearchController() }
}

fun Application.appModule() {
    println("DI инициализирован")
}