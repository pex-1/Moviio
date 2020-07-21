package practice.moviio.data.database

import androidx.room.*
import practice.moviio.data.response.details.MovieDetails

@Dao
interface MovieDao {

    @Query("SELECT * FROM movie_table ORDER BY year")
    suspend fun getFavorites(): List<MovieDetails>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movie: MovieDetails): Long

    @Delete
    suspend fun deleteMovie(movie: MovieDetails)

    @Query("DELETE FROM movie_table WHERE imdbID LIKE :id")
    suspend fun deleteMovieUsingId(id: String)

    @Query("SELECT EXISTS (SELECT imdbID FROM movie_table WHERE imdbID LIKE :id)")
    suspend fun movieExists(id: String): Boolean

    @Query("SELECT imdbId FROM movie_table")
    suspend fun getIds(): List<String>

}