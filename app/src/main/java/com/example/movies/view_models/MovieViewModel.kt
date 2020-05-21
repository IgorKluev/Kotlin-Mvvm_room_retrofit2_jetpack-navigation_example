package com.example.movies.view_models

import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.movies.models.MovieWithGenres

class MovieViewModel(application: Application) : AndroidViewModel(application) {
    private val TAG = "MainViewModel"

    val movieListData = MutableLiveData<MovieWithGenres>()
    val errorMessage = MutableLiveData<String>()

    fun getMovieArguments(bundle: Bundle?)
    {
        if ((getMoviesFromArgs(bundle) != null)) {
            /* received a list of movies! */
            movieListData.value = getMoviesFromArgs(bundle)
        } else {
            /*on error occurred receiving movie list*/
            onError("ERROR: no movie received!");
        }
    }

    private fun getMoviesFromArgs(bundle: Bundle?) : MovieWithGenres? = bundle?.getParcelable<MovieWithGenres>("moveItem")

    private fun onError(errorMsg:String)
    {
        errorMessage.value = errorMsg
    }

}