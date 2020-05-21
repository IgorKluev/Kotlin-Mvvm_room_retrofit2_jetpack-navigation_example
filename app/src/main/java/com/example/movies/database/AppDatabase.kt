package com.example.movies.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.movies.models.data.GenreModel
import com.example.movies.models.data.MovieAndGenres
import com.example.movies.models.data.MovieModel

@Database(entities = arrayOf(
    MovieModel::class,
    GenreModel::class, MovieAndGenres::class), version = 1)
abstract class AppDatabase : RoomDatabase()
{
//    abstract fun moviesDao():MoviesDao
//
//    abstract fun genresDao():GenreDao

    abstract fun movieGenreDao() : MovieGenreDao

    companion object
    {
        @Volatile private var instance: AppDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK)
        {
            instance ?: buildDatabase(context).also { instance = it}
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(context,
            AppDatabase::class.java, "movies.db")
            .build()
    }
}