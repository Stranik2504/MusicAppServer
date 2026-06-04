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
import dev.stranik.data.repository.ArtistRepositoryImpl
import dev.stranik.data.repository.FollowedArtistsRepositoryImpl
import dev.stranik.data.repository.AlbumRepositoryImpl
import dev.stranik.data.repository.GenresRepositoryImpl
import dev.stranik.data.repository.TrackRepositoryImpl
import dev.stranik.data.repository.ListeningHistoryRepositoryImpl
import dev.stranik.data.repository.RecommendationRepositoryImpl
import dev.stranik.data.repository.PlaylistRepositoryImpl
import dev.stranik.data.repository.UserRepositoryImpl
import dev.stranik.domain.repository.ArtistRepository
import dev.stranik.domain.repository.AlbumRepository
import dev.stranik.domain.repository.FollowedArtistsRepository
import dev.stranik.domain.repository.GenresRepository
import dev.stranik.domain.repository.ListeningHistoryRepository
import dev.stranik.domain.repository.PlaylistRepository
import dev.stranik.domain.repository.RecommendationRepository
import dev.stranik.domain.repository.TrackRepository
import dev.stranik.domain.repository.UserRepository
import dev.stranik.domain.usecases.AddListeningHistoryUseCase
import dev.stranik.domain.usecases.AddTrackToPlaylistUseCase
import dev.stranik.domain.usecases.CreatePlaylistUseCase
import dev.stranik.domain.usecases.DeletePlaylistUseCase
import dev.stranik.domain.usecases.FollowArtistUseCase
import dev.stranik.domain.usecases.GetArtistUseCase
import dev.stranik.domain.usecases.GetAlbumUseCase
import dev.stranik.domain.usecases.GetTrackUseCase
import dev.stranik.domain.usecases.GetArtistRecommendationsUseCase
import dev.stranik.domain.usecases.GetHomeRecommendationsUseCase
import dev.stranik.domain.usecases.GetTrackStreamUseCase
import dev.stranik.domain.usecases.LikeTrackUseCase
import dev.stranik.domain.usecases.SearchTracksUseCase
import dev.stranik.domain.usecases.UnlikeTrackUseCase
import dev.stranik.domain.usecases.GetTrackLikesCountUseCase
import dev.stranik.domain.usecases.GetAllFollowsUseCase
import dev.stranik.domain.usecases.GetAllGenresUseCase
import dev.stranik.domain.usecases.GetAvatarUseCase
import dev.stranik.domain.usecases.GetListeningHistoryUseCase
import dev.stranik.domain.usecases.GetPlaylistUseCase
import dev.stranik.domain.usecases.GetUserInfoUseCase
import dev.stranik.domain.usecases.LoginUseCase
import dev.stranik.domain.usecases.RegisterUseCase
import dev.stranik.domain.usecases.RemoveTrackFromPlaylistUseCase
import dev.stranik.domain.usecases.SearchAlbumsUseCase
import dev.stranik.domain.usecases.SearchArtistsUseCase
import dev.stranik.domain.usecases.SearchPlaylistsUseCase
import dev.stranik.domain.usecases.UpdateUserUseCase
import dev.stranik.domain.usecases.UnfollowArtistUseCase
import dev.stranik.domain.usecases.UpdatePlaylistUseCase
import dev.stranik.domain.usecases.GetTrackRecommendationsUseCase
import dev.stranik.security.PasswordHasher
import io.ktor.server.application.Application

object AppContainer {
    val userRepository: UserRepository by lazy { UserRepositoryImpl() }
    val albumRepository: AlbumRepository by lazy { AlbumRepositoryImpl() }
    val trackRepository: TrackRepository by lazy { TrackRepositoryImpl() }
    val artistRepository: ArtistRepository by lazy { ArtistRepositoryImpl() }
    val followedArtistsRepository: FollowedArtistsRepository by lazy { FollowedArtistsRepositoryImpl() }
    val listeningHistoryRepository: ListeningHistoryRepository by lazy { ListeningHistoryRepositoryImpl() }
    val playlistRepository: PlaylistRepository by lazy { PlaylistRepositoryImpl() }
    val recommendationRepository: RecommendationRepository by lazy {
        RecommendationRepositoryImpl(albumRepository, trackRepository, listeningHistoryRepository)
    }
    val genresRepository: GenresRepository by lazy { GenresRepositoryImpl() }

    val loginUseCase: LoginUseCase by lazy { LoginUseCase(userRepository, PasswordHasher) }
    val registerUseCase: RegisterUseCase by lazy { RegisterUseCase(userRepository, PasswordHasher) }
    val getUserInfoUseCase: GetUserInfoUseCase by lazy { GetUserInfoUseCase(userRepository) }
    val updateUserUseCase: UpdateUserUseCase by lazy { UpdateUserUseCase(userRepository) }
    val getAvatarUseCase: GetAvatarUseCase by lazy { GetAvatarUseCase(userRepository) }
    val searchAlbumsUseCase: SearchAlbumsUseCase by lazy { SearchAlbumsUseCase(albumRepository) }
    val getAlbumUseCase: GetAlbumUseCase by lazy { GetAlbumUseCase(albumRepository) }
    val searchTracksUseCase: SearchTracksUseCase by lazy { SearchTracksUseCase(trackRepository) }
    val getTrackUseCase: GetTrackUseCase by lazy { GetTrackUseCase(trackRepository) }
    val getTrackStreamUseCase: GetTrackStreamUseCase by lazy { GetTrackStreamUseCase(trackRepository) }
    val likeTrackUseCase: LikeTrackUseCase by lazy { LikeTrackUseCase(trackRepository) }
    val unlikeTrackUseCase: UnlikeTrackUseCase by lazy { UnlikeTrackUseCase(trackRepository) }
    val getTrackLikesCountUseCase: GetTrackLikesCountUseCase by lazy { GetTrackLikesCountUseCase(trackRepository) }
    val searchArtistsUseCase: SearchArtistsUseCase by lazy { SearchArtistsUseCase(artistRepository) }
    val getArtistUseCase: GetArtistUseCase by lazy { GetArtistUseCase(artistRepository) }
    val followArtistUseCase: FollowArtistUseCase by lazy { FollowArtistUseCase(artistRepository, followedArtistsRepository) }
    val unfollowArtistUseCase: UnfollowArtistUseCase by lazy { UnfollowArtistUseCase(followedArtistsRepository) }
    val getAllFollowsUseCase: GetAllFollowsUseCase by lazy { GetAllFollowsUseCase(followedArtistsRepository) }
    val getListeningHistoryUseCase: GetListeningHistoryUseCase by lazy { GetListeningHistoryUseCase(listeningHistoryRepository) }
    val addListeningHistoryUseCase: AddListeningHistoryUseCase by lazy { AddListeningHistoryUseCase(listeningHistoryRepository) }
    val searchPlaylistsUseCase: SearchPlaylistsUseCase by lazy { SearchPlaylistsUseCase(playlistRepository) }
    val getPlaylistUseCase: GetPlaylistUseCase by lazy { GetPlaylistUseCase(playlistRepository) }
    val createPlaylistUseCase: CreatePlaylistUseCase by lazy { CreatePlaylistUseCase(playlistRepository) }
    val updatePlaylistUseCase: UpdatePlaylistUseCase by lazy { UpdatePlaylistUseCase(playlistRepository) }
    val deletePlaylistUseCase: DeletePlaylistUseCase by lazy { DeletePlaylistUseCase(playlistRepository) }
    val addTrackToPlaylistUseCase: AddTrackToPlaylistUseCase by lazy { AddTrackToPlaylistUseCase(playlistRepository) }
    val removeTrackFromPlaylistUseCase: RemoveTrackFromPlaylistUseCase by lazy { RemoveTrackFromPlaylistUseCase(playlistRepository) }
    val getHomeRecommendationsUseCase: GetHomeRecommendationsUseCase by lazy { GetHomeRecommendationsUseCase(recommendationRepository) }
    val getTrackRecommendationsUseCase: GetTrackRecommendationsUseCase by lazy { GetTrackRecommendationsUseCase(recommendationRepository) }
    val getArtistRecommendationsUseCase: GetArtistRecommendationsUseCase by lazy { GetArtistRecommendationsUseCase(recommendationRepository) }
    val getAllGenresUseCase: GetAllGenresUseCase by lazy { GetAllGenresUseCase(genresRepository) }

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
    val artistsController: ArtistsController by lazy {
        ArtistsController(
            searchArtistsUseCase,
            getArtistUseCase,
            followArtistUseCase,
            unfollowArtistUseCase,
        )
    }
    val tracksController: TracksController by lazy {
        TracksController(
            searchTracksUseCase,
            getTrackUseCase,
            getTrackStreamUseCase,
            likeTrackUseCase,
            unlikeTrackUseCase,
            getTrackLikesCountUseCase
        )
    }
    val genresController: GenresController by lazy { GenresController(getAllGenresUseCase) }
    val playlistsController: PlaylistsController by lazy {
        PlaylistsController(
            searchPlaylistsUseCase,
            getPlaylistUseCase,
            createPlaylistUseCase,
            updatePlaylistUseCase,
            deletePlaylistUseCase,
            addTrackToPlaylistUseCase,
            removeTrackFromPlaylistUseCase,
        )
    }
    val recommendationsController: RecommendationsController by lazy {
        RecommendationsController(
            getHomeRecommendationsUseCase,
            getTrackRecommendationsUseCase,
            getArtistRecommendationsUseCase,
        )
    }
    val searchController: SearchController by lazy {
        SearchController(
            searchTracksUseCase,
            searchAlbumsUseCase,
            searchArtistsUseCase,
            searchPlaylistsUseCase
        )
    }
}

fun Application.appModule() {
    println("DI инициализирован")
}