package practice.moviio.di.modules

import dagger.Module
import dagger.Provides
import practice.moviio.api.OmdbApi
import practice.moviio.data.database.MovieDao
import practice.moviio.data.repository.*
import practice.moviio.di.scope.DetailsScope
import practice.moviio.di.scope.FavoritesScope
import practice.moviio.di.scope.SearchScope

@Module
class RepositoryModule {

    @FavoritesScope
    @Provides
    fun provideFavoritesRepository(movieDao: MovieDao): FavoritesRepository = FavoritesRepositoryImpl(movieDao)

    @SearchScope
    @Provides
    fun provideSearchRepository(movieDao: MovieDao, api: OmdbApi): SearchRepository = SearchRepositoryImpl(movieDao, api)

    @DetailsScope
    @Provides
    fun provideDetailsRepository(movieDao: MovieDao, api: OmdbApi): DetailsRepository = DetailsRepositoryImpl(movieDao, api)
}