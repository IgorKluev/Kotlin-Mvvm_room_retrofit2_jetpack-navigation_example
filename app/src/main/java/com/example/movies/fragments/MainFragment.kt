package com.example.movies.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movies.R
import com.example.movies.adapters.GenrePickerAdapter
import com.example.movies.adapters.MoviesAdapter
import com.example.movies.fragments.base.BaseFragment
import com.example.movies.models.MovieWithGenres
import com.example.movies.utils.utilities.debouncing_coroutine_query.DebouncingQueryTextListener
import com.example.movies.view_models.MainViewModel
import kotlinx.android.synthetic.main.fragment_main.*


/**
 * A simple [Fragment] subclass of BaseFragment
 */
class MainFragment : BaseFragment()
{
    private val TAG = "MainFragment"
    private lateinit var mainViewModel: MainViewModel

    override fun getLayoutResource(): Int = R.layout.fragment_main

    override fun initViewModel() {
        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)

    }

    override fun onViewCreated()
    {
        mainViewModel.getMoviesArguments(arguments)
        setLayoutManager()
        setLayoutAnimation()
        initSearchListeners()
    }

    private fun setLayoutManager()
    {
        mainViewModel.layoutManager = LinearLayoutManager(rootView.context, RecyclerView.VERTICAL, false)
        moviesRecList.layoutManager = mainViewModel.layoutManager
    }

    private fun setLayoutAnimation()
    {
        val resId: Int = R.anim.layout_animation_fall_down
        val animation =
            AnimationUtils.loadLayoutAnimation(rootView.context, resId)
        moviesRecList.layoutAnimation = animation
    }


    override fun setLiveDataListeners()
    {
        mainViewModel.errorMessage.observe(viewLifecycleOwner, Observer { errorMsg ->
            setErrorMsg(errorMsg)
        })

        mainViewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->})

        /* movies observer */
        mainViewModel.moviesListData.observe(viewLifecycleOwner, Observer{ moviesList ->
            setAdapter(moviesList)
        })

        /*genres observer */
        mainViewModel.allGenres.observe(viewLifecycleOwner, Observer{ genresList->
            mainViewModel.genresAdapter = GenrePickerAdapter(genresList){items->
               moviesRecList.removeAllViewsInLayout();
                mainViewModel.adapterGenreClick(search.query.toString())
            }
            genresRecList.apply{
                adapter = mainViewModel.genresAdapter
                layoutManager = LinearLayoutManager(rootView.context, RecyclerView.HORIZONTAL, false)
                setHasFixedSize(true)
                setItemViewCacheSize(genresList.size)
            }
        })
    }

    private fun adapterItemClick(item : MovieWithGenres)
    {
        clearSearchQuery()
        val bundle = bundleOf("moveItem" to item)
        navController.navigate(R.id.action_mainFragment_to_movieFragment,bundle)
    }

    fun setAdapter(moviesList : ArrayList<MovieWithGenres>)
    {
        if(mainViewModel.genresAdapter  == null || moviesRecList.adapter == null)
        {
            mainViewModel.moviesAdapter = MoviesAdapter(moviesList) { item->adapterItemClick(item) }
            moviesRecList.adapter = mainViewModel.moviesAdapter
        }
        else
        {
            mainViewModel.moviesAdapter?.updateMovies(moviesList)
        }
    }

    private fun setErrorMsg(errorMsg: String) {
        if (errorText.visibility != View.VISIBLE) {
            errorText.visibility = View.VISIBLE
        }
        errorText.text = errorMsg
    }
    override fun setClickListeners() {}

    private fun initSearchListeners()
    {
        search.setBackPressListener {
            if(search.query.isNullOrEmpty() && !search.isIconified) {
                clearSearchQuery()
            }
        }

        search.setOnQueryTextListener(DebouncingQueryTextListener(this.lifecycle){ newText->
            newText.let {
                    mainViewModel.queryTitleAndGenres(it)
            }
        })
    }

    private fun clearSearchQuery()
    {
        search.setQuery("", false);
        search.isIconified = true;
    }

    override fun onBackClick(): Boolean
    {
        //TODO ask if wishes to exit with toast
        return false
    }
}
