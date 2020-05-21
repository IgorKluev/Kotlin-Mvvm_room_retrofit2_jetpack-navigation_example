package com.example.movies.utils.utilities

import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.example.movies.R

fun ImageView.loadImage(uri: String?)
{
    val options = RequestOptions()
        .error(R.drawable.appicon_svg)
        .placeholder(R.drawable.thumb_icon)

    Glide.with(this.context)
        .setDefaultRequestOptions(options)
        .load(uri)
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(this)
}

fun <T : RecyclerView.ViewHolder> T.listen(event: (position: Int, type: Int) -> Unit): T {
    itemView.setOnClickListener {
        event.invoke(getAdapterPosition(), getItemViewType())
    }
    return this
}
