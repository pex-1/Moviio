package practice.moviio.di.modules

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import practice.moviio.data.database.MovieDao
import practice.moviio.data.database.MovieDatabase
import javax.inject.Singleton

@Module
class MovieDbModule {

    @Singleton
    @Provides
    fun provideMovieDatabase(application: Application): MovieDatabase{
        return Room.databaseBuilder(application, MovieDatabase::class.java, MovieDatabase.DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideMovieDao(database: MovieDatabase): MovieDao = database.movieDao()

}