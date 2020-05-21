package com.example.movies.models

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.movies.models.data.GenreModel
import com.example.movies.models.data.MovieAndGenres
import com.example.movies.models.data.MovieModel
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MovieWithGenres(
    @Embedded
        val movie: MovieModel?,
    @Relation(
            parentColumn = "id",
            entity = GenreModel::class,
            entityColumn = "gId",
            associateBy = Junction(
                value = MovieAndGenres::class,
                parentColumn = "movieId",
                entityColumn = "genreId")
        )
         val genres: List<GenreModel>?) : Parcelable
{

    override fun toString(): String {
        return "MovieDetails: $movie \ngenresDB: $genres\n"
    }
}