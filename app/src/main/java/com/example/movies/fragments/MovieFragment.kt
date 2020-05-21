package com.example.movies.fragments

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.movies.R
import com.example.movies.adapters.GenreAdapter
import com.example.movies.fragments.base.BaseFragment
import com.example.movies.models.MovieWithGenres
import com.example.movies.utils.utilities.loadImage
import com.example.movies.view_models.MovieViewModel
import kotlinx.android.synthetic.main.fragment_movie.*

/**
 * A simple [Fragment] subclass.
 */
class MovieFragment : BaseFragment()
{
    lateinit var movieModel : MovieViewModel

    override fun getLayoutResource(): Int  = R.layout.fragment_movie

    override fun initViewModel()
    {
        movieModel = ViewModelProvider(this).get(MovieViewModel::class.java)
    }

    override fun onViewCreated()
    {
//        val movieItem : MovieWithGenres? = arguments?.getParcelable<MovieWithGenres>("moveItem")
        setLiveDataListener()
        movieModel.getMovieArguments(arguments)

    }


    private fun setLiveDataListener()
    {
        movieModel.movieListData.observe(viewLifecycleOwner, Observer {

            imgMovie.loadImage(it.movie?.image)
            title.text = it.movie?.title
            rating.text = "Rated: ${it.movie?.rating.toString()}/10"
            releaseYear.text = "Release Year: ${it.movie?.releaseYear.toString()}"

            categoriesRec.apply {
                adapter = GenreAdapter(it.genres)
                layoutManager = LinearLayoutManager(this.context, RecyclerView.HORIZONTAL, false)
                setHasFixedSize(true)
            }
        })
    }

    override fun setLiveDataListeners() {
    }

    override fun setClickListeners() {
    }



    override fun onBackClick(): Boolean {
        return false
    }


}
