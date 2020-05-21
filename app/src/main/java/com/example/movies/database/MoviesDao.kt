package com.example.movies.database

import androidx.room.*

@Dao
abstract class MoviesDao
{
//    @Query("SELECT * FROM movies")
//    abstract fun getAll() : Maybe<List<MovieModel>>
//
//    @Query("SELECT * FROM movies WHERE title LIKE :title")
//    abstract fun getByTitle(title:String) :  Maybe<MovieModel>
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    abstract fun insertRx(vararg movie: MovieModel) : Completable
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    abstract fun insert(movie: MovieModel) : Long
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    abstract  fun insertAll(movies: List<MovieModel>) : List<Long>
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    abstract  fun insertAllRx(movies: List<MovieModel>) : Completable
//
//    @Delete
//    abstract fun delete(movie: MovieModel) : Completable
//
//    @Update
//    abstract fun updateMovie(vararg movie: MovieModel) : Completable

}