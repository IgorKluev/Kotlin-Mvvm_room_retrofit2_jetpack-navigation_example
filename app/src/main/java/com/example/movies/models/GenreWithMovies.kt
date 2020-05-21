package com.example.movies.models

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.movies.models.data.GenreModel
import com.example.movies.models.data.MovieAndGenres
import com.example.movies.models.data.MovieModel

data class GenreWithMovies(@Embedded val genre : GenreModel,
                           @Relation(parentColumn = "gId",
                                         entity = MovieModel::class,
                                         entityColumn = "id",
                                   associateBy = Junction(value = MovieAndGenres::class,
                                       parentColumn = "genreId",
                                       entityColumn = "movieId")
                                   ) val movies : List<MovieModel>)