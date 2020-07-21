package practice.moviio.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import practice.moviio.data.response.details.MovieDetails

@Database(entities = [MovieDetails::class], version = 1, exportSchema = false)
abstract class MovieDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao

    companion object {
        const val DATABASE_NAME = "movie.db"
    }
}