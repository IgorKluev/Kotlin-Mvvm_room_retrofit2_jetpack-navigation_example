package com.example.movies.models.data

import androidx.room.Entity

@Entity( primaryKeys = ["movieId", "genreId"])
data class MovieAndGenres(val movieId: Long,
                          val genreId: Long)