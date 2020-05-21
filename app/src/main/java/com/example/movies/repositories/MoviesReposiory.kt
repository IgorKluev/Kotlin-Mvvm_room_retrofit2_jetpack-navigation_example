package com.example.movies.repositories

import android.content.ContentValues.TAG
import android.util.Log
import androidx.annotation.WorkerThread
import com.example.movies.MyApp
import com.example.movies.database.MovieGenreDao
import com.example.movies.models.data.GenreModel
import com.example.movies.models.data.MovieAndGenres
import com.example.movies.models.data.MovieModel
import com.example.movies.models.MovieWithGenres
import com.example.movies.utils.retrofit.MoviesApi
import io.reactivex.Observable
import io.reactivex.Single
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Repository class which is used as comunicator between the app and server-side/local Db
 *
 * */
class MoviesReposiory private constructor() {
    private val dbAccess: MovieGenreDao = MyApp.instance.getMovieGenreDao()
    private val moviesService: MoviesApi = MyApp.instance.moviesApi


    var job: Job? = null
    var movieAndGenre: ArrayList<MovieWithGenres> = ArrayList()

    companion object {
        val instance: MoviesReposiory by lazy { HOLDER.INSTANCE }
    }

    private object HOLDER {
        val INSTANCE = MoviesReposiory()
    }

    fun getMovieData(): Flow<ArrayList<MovieWithGenres>> {
        return flow {

            val dataBasePopulated: Long? = dbAccess.checkTablePopulatedCoroutine()
            if (dataBasePopulated != null && dataBasePopulated > 0) {
                delay(1500)
                val dbMovies = dbAccess.getMovieWithGenresCoroutines() as ArrayList<MovieWithGenres>
                emit(dbMovies)
            } else {
                val responseMovies = moviesService.getMoviesListCoroutine()

                if (responseMovies.isSuccessful && responseMovies.body() != null) {
                    val moviesWithGenres = insertMoviesToDataBase(responseMovies.body()!!)
                    emit(moviesWithGenres)
                } else {
                    val errorMsg =
                        "onServerCallError: responseCallSuccess:${responseMovies.isSuccessful} | body call is returned null: ${responseMovies.body() == null}"
                    error(errorMsg)
                }
            }
        }
    }


    /*
    * Acccess DataBase and retrieves single MovieAndGenres id exists
    * if exists retrieves ArrayList<MovieWithGenres> and passes to onSuccess
    * if database is Empty will return onError "DataBase isEmpty!"
    * */
    fun checkDbPopulated(): Single<ArrayList<MovieWithGenres>> {
        return Single.create {
            if (dbAccess.checkTablePopulated() > 0) {
                movieAndGenre = dbAccess.getMovieWithGenres() as ArrayList
                it.onSuccess(movieAndGenre)
            } else {
                it.onError(Throwable("DataBase isEmpty!"))
            }
        }
    }

    suspend fun insertMoviesToDataBase(movies: List<MovieModel>): ArrayList<MovieWithGenres> {
        val moviesWithGenresList: ArrayList<MovieWithGenres> = ArrayList()
        val genreMap: HashMap<String, Long> = HashMap()

        movies.forEach {
            val genrelist = ArrayList<GenreModel>()
            val movieId = dbAccess.insertMovie(it)

            if (it.genreString != null) {
                for (genre in it.genreString!!) {
                    val isGenreInserted: Long? = genreMap[genre]
                    genrelist.add(
                        GenreModel(
                            genreName = genre
                        )
                    )

                    val genreModel = if (isGenreInserted != null) {
                        addMovieAndGenre(movieId, isGenreInserted)
                        continue
                    } else {
                        GenreModel(genreName = genre)
                    }

                    val genreId = dbAccess.insertGenre(genreModel)
                    genreMap[genre] = genreId
                    addMovieAndGenre(movieId, genreId)
                }
                moviesWithGenresList.add(MovieWithGenres(it, genrelist))
            }
        }

        return moviesWithGenresList
    }

    fun insertMoviesToDataBaseObservable(movies: List<MovieModel>): Observable<ArrayList<MovieWithGenres>> {
        return Observable.create {
            Log.e(TAG, "A1")
            val moviesWithGenresList: ArrayList<MovieWithGenres> = ArrayList()
            val genreMap: HashMap<String, Long> = HashMap()

            movies.forEach {
                val genrelist = ArrayList<GenreModel>()
                val movieId = dbAccess.insertMovie(it)

                if (it.genreString != null) {
                    for (genre in it.genreString!!) {
                        //TODO here
                        val isGenreInserted: Long? = genreMap[genre]
                        genrelist.add(
                            GenreModel(
                                genreName = genre
                            )
                        )

                        val genreModel = if (isGenreInserted != null) {
                            addMovieAndGenre(movieId, isGenreInserted)
                            continue
                        } else {
                            GenreModel(genreName = genre)
                        }

                        val genreId = dbAccess.insertGenre(genreModel)
                        genreMap[genre] = genreId
                        addMovieAndGenre(movieId, genreId)
                    }
                    moviesWithGenresList.add(MovieWithGenres(it, genrelist))
                }
            }
            Log.e(TAG, "A2");
            it.onNext(moviesWithGenresList)
            it.onComplete()
        }
    }

    fun getQuriedMovies(query: String?): Flow<ArrayList<MovieWithGenres>> {
        return flow {

            if (query.isNullOrEmpty()) {
                val dbMovies = dbAccess.getMovieWithGenresCoroutines() as ArrayList<MovieWithGenres>
                emit(dbMovies)
            } else {
                val searchQuery = "%$query%"
                val queriedMovies =
                    dbAccess.getMovieByTitleCoroutine(searchQuery) as ArrayList<MovieWithGenres>
                emit(queriedMovies)
            }
        }
    }

    @WorkerThread
    private fun addMovieAndGenre(movieId: Long, genreId: Long) {
        val movieAndGenres =
            MovieAndGenres(movieId, genreId)
        dbAccess.addMovieAndGenre(movieAndGenres)

    }

    fun getAllGenres(): Flow<ArrayList<GenreModel>> {

        return flow {
            val genres = dbAccess.getAllGenresCoroutine() as ArrayList
            emit(genres)
        }

    }

    suspend fun getMoviesByGenres(genreList: List<Long>): Flow<ArrayList<MovieWithGenres>> {
        return flow {
            if (genreList.size == 1 && genreList[0] == 0L) {
                val test = dbAccess.getMovieWithGenresCoroutines() as ArrayList
                emit(test)
            } else {
                val test = dbAccess.getMoviesGenresQueried("%district 9%", genreList) as ArrayList
                emit(test)
            }
        }
    }

    suspend fun getQueriedMovies(query: String?, genreList: List<Long>?): Flow<ArrayList<MovieWithGenres>> {
        return flow {
            val test =
                if (query.isNullOrEmpty() && (genreList.isNullOrEmpty() || genreList.size == 1 && genreList[0] == 0L)) {
                    dbAccess.getMovieWithGenresCoroutines() as ArrayList
                } else if (query.isNullOrEmpty() && genreList?.size!! >= 1 && genreList[0] != 0L) {
                    dbAccess.getMoviesWithSelectedGeneres(genreList!!) as ArrayList
                }
                else{
                    val items = if (genreList?.size!! <= 1 && (genreList.isEmpty() || genreList[0] == 0L )) {
                        dbAccess.getMovieByTitleCoroutine("%$query%") as ArrayList
                    } else {
                        dbAccess.getMoviesGenresQueried("%$query%", genreList!!) as ArrayList
                    }
                    items
                }
            emit(test)

        }
    }

}