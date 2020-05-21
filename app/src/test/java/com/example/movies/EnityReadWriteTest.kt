package com.example.movies

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.movies.database.AppDatabase
import com.example.movies.database.MoviesDao
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class EnityReadWriteTest {
    private lateinit var todoDao: MoviesDao
    private lateinit var db: AppDatabase

    @Before
    fun createDb() {
        val context  = InstrumentationRegistry.getInstrumentation().context
        db = Room.inMemoryDatabaseBuilder(
                context, AppDatabase::class.java).build()
//        todoDao = db.moviesDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun writeUserAndReadInList() {
//        val movie: MovieModel = MovieModel(title = "title", image = "imgurl",rating = 0.0,releaseYear = 1970)
//        todoDao.insertAll(movie)
//        val todoItem : MovieModel  = todoDao.getByTitle(movie.title)
//        assertThat(todoItem, equalTo(movie))
    }
}