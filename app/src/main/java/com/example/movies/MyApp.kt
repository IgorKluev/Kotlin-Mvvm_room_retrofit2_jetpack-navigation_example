package com.example.movies

import android.app.Application
import com.example.movies.database.AppDatabase
import com.example.movies.database.MovieGenreDao
import com.example.movies.utils.retrofit.MoviesApi

class MyApp : Application()
{
    val moviesApi by lazy {
        MoviesApi.create()
    }

    val dataBase by lazy{
        AppDatabase.invoke(this)
    }

    override fun onCreate()
    {
        super.onCreate()
        instance = this
    }

    fun getApi() : MoviesApi = moviesApi

    fun getMovieGenreDao() : MovieGenreDao = dataBase.movieGenreDao()

    /*singleton init therefor the use of private set*/
    companion object {
        lateinit var instance: MyApp
            private set
    }

}