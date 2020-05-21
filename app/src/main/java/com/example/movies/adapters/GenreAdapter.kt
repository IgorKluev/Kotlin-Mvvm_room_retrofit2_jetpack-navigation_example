package com.example.movies.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.movies.R
import com.example.movies.models.data.GenreModel
import com.example.movies.utils.utilities.listen
import kotlinx.android.synthetic.main.item_genre.view.*

class GenreAdapter(var genreList: List<GenreModel>?) : RecyclerView.Adapter<GenreAdapter.GenreViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =GenreViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_genre, parent, false))

    override fun getItemCount(): Int = if (genreList != null) genreList!!.size else 0

    override fun onBindViewHolder(holder: GenreViewHolder, position: Int) {
        if (genreList?.get(position) != null) holder.bind(genreList!![position])
        else holder.view.visibility = View.GONE
    }

    inner class GenreViewHolder(var view: View) : RecyclerView.ViewHolder(view)
    {
        private val genreName = view.genreName

        fun bind(genre: GenreModel?)
        {
            if (genre != null) {
                genreName.text = genre.genreName
            } else {
                view.visibility = View.GONE
            }
        }

    }
}