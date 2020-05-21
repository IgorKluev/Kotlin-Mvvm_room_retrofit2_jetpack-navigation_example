package com.example.movies.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movies.R
import com.example.movies.models.MovieWithGenres
import com.example.movies.utils.utilities.listen
import com.example.movies.utils.utilities.loadImage
import kotlinx.android.synthetic.main.item_movie.view.*

class MoviesAdapter(var moviesList : ArrayList<MovieWithGenres> = ArrayList(), var adapterOnClick : (MovieWithGenres) -> Unit) : RecyclerView.Adapter<MoviesAdapter.MovieViewHolder>()
{
    private val viewPool = RecyclerView.RecycledViewPool()/*used for nested recyelerviews to determine the nested pool to be preloaded*/


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : MovieViewHolder
    {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false)

        return MovieViewHolder(view).listen { position, type ->
            val item = moviesList.get(position)
            adapterOnClick(item)
        }
    }

    override fun getItemCount() : Int = moviesList.size

    fun updateMovies(list: ArrayList<MovieWithGenres> ){
        moviesList = list
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int)
    {
        holder.bind(moviesList[position])
    }

    inner class MovieViewHolder(view : View) : RecyclerView.ViewHolder(view)
    {
        private val movieImg = view.movieImg
        private val title = view.title
        private val rating = view.rating
        private val releaseYear = view.releaseYear
        private val categoriesRec : RecyclerView = view.categoriesRec


        fun bind(movieAndGenre : MovieWithGenres)
        {
            movieImg.loadImage(movieAndGenre.movie?.image)
            title.text = movieAndGenre.movie?.title
            rating.text =  "${movieAndGenre.movie?.rating}"
            releaseYear.text = "Release: ${movieAndGenre.movie?.releaseYear}"

            val childLayoutManager = LinearLayoutManager(categoriesRec.context, RecyclerView.HORIZONTAL, false)
            childLayoutManager.initialPrefetchItemCount = 3

            categoriesRec.apply{
                layoutManager = childLayoutManager
                adapter = GenreAdapter(movieAndGenre.genres)
                setRecycledViewPool(viewPool)
                setHasFixedSize(true)
            }
        }
    }


}