package com.example.movies.utils.retrofit

import com.example.movies.models.data.MovieModel
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import io.reactivex.Observable
import kotlinx.coroutines.flow.Flow
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import retrofit2.Call
import retrofit2.Response


interface MoviesApi
{
    @GET("movies.json")
    fun getMoviesList() : Observable<List<MovieModel>>

    @GET("movies.json")
    suspend fun getMoviesListCoroutine() : Response<List<MovieModel>>

    /** companion object used to create isntance of MoviesApi Retrofit!*/
    companion object {
        fun create(): MoviesApi {

            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .baseUrl("https://api.androidhive.info/json/")
                .build()

            return retrofit.create(MoviesApi::class.java)
        }
    }
}