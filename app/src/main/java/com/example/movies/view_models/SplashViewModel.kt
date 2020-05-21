package com.example.movies.view_models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.movies.MyApp
import com.example.movies.models.MovieWithGenres
import com.example.movies.repositories.MoviesReposiory
import com.example.movies.utils.retrofit.MoviesApi
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SplashViewModel(application: Application) : AndroidViewModel(application)
{
    val TAG = "MoviesViewModel"

    val repository : MoviesReposiory = MoviesReposiory.instance

    val movieWithGenres :  MutableLiveData<ArrayList<MovieWithGenres>> = MutableLiveData()
    val isLoading :  MutableLiveData<Boolean> = MutableLiveData()
    val errorListener :  MutableLiveData<String> = MutableLiveData()
    val moviesApi : MoviesApi = MyApp.instance.getApi()

    var disposable : Disposable? = null;

    /**
     * TODO documentation
     */
    fun getMoviesDataFlow()
    {
        viewModelScope.launch {
             repository.getMovieData()
                .flowOn(Dispatchers.IO)
                .onStart {
                    /* TODO check internet? */
                    isLoading.value = true
                    delay(500L)
                }.flowOn(Dispatchers.Main)
                .catch { e-> val erorTxt = "onError occured: $e"
                            setError(erorTxt)
                }.collect {
                     isLoading.value = false
                     setMoviesWithGenres(it)
                }
        }

    }


    /**
     * check if movies data has been inserted in the DB
     *  on success: retrieved data from DB and set in movieWit
     * on error: database is Empty start server request
     * **/
    fun getMoviesData()
    {
        disposable = repository
            .checkDbPopulated()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                setIsLoading(true)
            }
            .subscribe({
                setIsLoading(false)
                setMoviesWithGenres(it)
            },{
                getMovieFromsever()
            })
    }

    private fun getMovieFromsever()
    {
        disposable = moviesApi
            .getMoviesList()
            .subscribeOn(Schedulers.io())
            .flatMap {repository.insertMoviesToDataBaseObservable(it) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                setMoviesWithGenres(it)
            },{
                //TODO on error occured
                setError(it.message)
            })
    }

    private fun setIsLoading(isInLoading : Boolean)
    {
        isLoading.value = isInLoading
    }

    private fun setMoviesWithGenres(moviesList : ArrayList<MovieWithGenres>)
    {
        movieWithGenres.value = moviesList
    }

    private fun setError(errorMsg : String?)
    {
        setIsLoading(false)
        errorListener.value = errorMsg
    }


    override fun onCleared() {
        super.onCleared()
        disposable?.dispose()
    }
}