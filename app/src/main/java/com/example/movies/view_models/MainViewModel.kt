package com.example.movies.view_models

import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movies.adapters.GenreAdapter
import com.example.movies.adapters.GenrePickerAdapter
import com.example.movies.adapters.MoviesAdapter
import com.example.movies.models.MovieWithGenres
import com.example.movies.models.data.GenreModel
import com.example.movies.repositories.MoviesReposiory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application)
{
    private val TAG = "MainViewModel"
    val errorMessage = MutableLiveData<String>()
    val isLoading = MutableLiveData<Boolean>()
    val moviesListData = MutableLiveData<ArrayList<MovieWithGenres>>()
    val allGenres = MutableLiveData<ArrayList<GenreModel>>()
    val repository : MoviesReposiory = MoviesReposiory.instance

    var movieList : ArrayList<MovieWithGenres>? = null;
    var moviesAdapter : MoviesAdapter? = null
    var genresAdapter : GenrePickerAdapter? = null

    var layoutManager : LinearLayoutManager? = null
    var job : Job? = null

    fun getMoviesArguments(bundle: Bundle?)
    {
        if(moviesListData.value.isNullOrEmpty())
        {
            if(!(getMoviesFromArgs(bundle).isNullOrEmpty()))
            {
                /* received a list of movies! */
                getAllGenres()
                movieList = getMoviesFromArgs(bundle)
                Log.e(TAG,movieList.toString())
                moviesListData.value = movieList
            }
            else{
                /*on error occurred receiving movie list*/
                onError("ERROR: no movies received!");
            }
        }


    }


    fun adapterGenreClick(query : String?)
    {
        queryTitleAndGenres(query)
    }

    private fun getMoviesFromArgs(bundle: Bundle?) : ArrayList<MovieWithGenres>? = bundle?.getParcelableArrayList("tst")

    private fun onError(errorMsg:String)
    {
        isLoading.value = false
        errorMessage.value = errorMsg
    }

    fun getAllGenres()
    {
        job = viewModelScope.launch{
            repository.getAllGenres()
                .flowOn(Dispatchers.IO)
                .map {
                    val arrayList = ArrayList<GenreModel>()
                    val itemAll : GenreModel = GenreModel(0," All ",true)
                    arrayList.add(itemAll)
                    arrayList.addAll(it)
                    return@map arrayList
                 }
                .flowOn(Dispatchers.Main)
                .catch { e->onError("${e.message}") }
                .collect{
                    Log.e(TAG,it.toString())
                    allGenres.value = it
                }
        }
    }


    fun queryTitleAndGenres(queryTitle:String?)//queryTitleAndGenres
    {
        job?.cancel()
        job = viewModelScope.launch {
            repository.getQueriedMovies(queryTitle,genresAdapter?.selectedGenres)
                .flowOn(Dispatchers.IO)
                .onStart {
                    delay(100L)
                }
                .flowOn(Dispatchers.Main)
                .catch { e->onError("${e.message}") }
                .collect{
                    moviesListData.value = it
                }
        }
        job?.start()
    }

}