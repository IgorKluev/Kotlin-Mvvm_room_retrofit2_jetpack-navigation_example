package com.example.movies.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.movies.models.*
import com.example.movies.models.data.GenreModel
import com.example.movies.models.data.MovieAndGenres
import com.example.movies.models.data.MovieModel
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable

@Dao
interface MovieGenreDao{

    @Query("DELETE FROM MovieAndGenres WHERE MovieAndGenres.movieId = :movieId")
    fun removeAllMovieByMovideId(movieId: Long)

    @Query("DELETE FROM MovieAndGenres WHERE MovieAndGenres.genreId =:genreId")
    fun removeAllGenreById(genreId: Long)

    /** get all movies regular **/
    @Query("SELECT * FROM movies")
    fun getMovieWithGenres(): List<MovieWithGenres>

    /** get all movies RxJava2 **/
    @Query("SELECT * FROM movies")
    fun getMovieWithGenresRx(): Observable<List<MovieWithGenres>>

    /** get all movies coroutine **/
    @Query("SELECT * FROM movies")
   suspend fun getMovieWithGenresCoroutines(): List<MovieWithGenres>

    /** search query regular **/
    @Query("SELECT * FROM movies WHERE title LIKE :query")
     fun getMovieByTitle(query:String) :  List<MovieWithGenres>

    /** search query coroutine **/
    @Query("SELECT * FROM movies WHERE title LIKE :query")
   suspend fun getMovieByTitleCoroutine(query:String) :  List<MovieWithGenres>

    @Query("SELECT * FROM genre")
    fun getGenresWithMovies(): List<GenreWithMovies>

    @Query("SELECT * FROM genre")
    fun getGenresWithMoviesRx(): Observable<List<GenreWithMovies>>

    @Query("SELECT * FROM genre")
    suspend fun getGenresWithMoviesCoroutine(): List<GenreWithMovies>

    @Query("SELECT * FROM genre WHERE gId IN(:gId)")
    suspend fun getGenresWithMoviesCoroutine(vararg gId : Long): List<GenreWithMovies>

    @Query("SELECT * FROM genre WHERE gId  IN(:gIdList)")
    suspend fun getGenresWithMoviesCoroutine(gIdList : List<Long>): List<GenreWithMovies>

    @Query("SELECT DISTINCT movies.id,movies.title,movies.image,movies.rating,movies.releaseYear FROM movies JOIN MovieAndGenres ON movies.id = MovieAndGenres.movieId AND MovieAndGenres.genreId IN (:gIdList)")
    suspend fun getMoviesWithSelectedGeneres(gIdList : List<Long>): List<MovieWithGenres>

    @Query("SELECT DISTINCT movies.id,movies.title,movies.image,movies.rating,movies.releaseYear FROM movies JOIN MovieAndGenres ON movies.id = MovieAndGenres.movieId AND MovieAndGenres.genreId IN (:gIdList) AND movies.title LIKE :query")
    suspend fun getMoviesGenresQueried(query: String?, gIdList : List<Long>): List<MovieWithGenres>

    @Query("SELECT * FROM genre")
   suspend fun getAllGenresCoroutine() : List<GenreModel>

    /** normal check of database population **/
    @Query("SELECT 1 FROM MovieAndGenres LIMIT 1")
    fun checkTablePopulated(): Long

    /** Rxjava check using maybe if database populated **/
    @Query("SELECT 1 FROM MovieAndGenres LIMIT 1")
    fun checkTablePopulatedRx(): Maybe<Long>

    /** Kotlin Coroutine check if database populated **/
    @Query("SELECT 1 FROM MovieAndGenres LIMIT 1")
    suspend fun checkTablePopulatedCoroutine() : Long

    /* * *MovieAndGenre insert* * */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addMovieAndGenre(movieAndGenres: MovieAndGenres)
    /* * *movies insert* * */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovie(movie: MovieModel) : Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovieRx(vararg movie: MovieModel) : Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllMovies(movies: List<MovieModel>) : List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
      fun insertAllMoviesRx(movies: List<MovieModel>) : Completable

    /* * *genre insert* * */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGenre(movie: GenreModel) : Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGenreRx(vararg movie: GenreModel) : Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllGenre(genres: List<GenreModel>) : List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllGenreRx(genres: List<GenreModel>) : Completable
}
