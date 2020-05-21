package com.example.movies.activities.splash_activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.movies.R
import com.example.movies.activities.main_activity.MainActivity
import com.example.movies.models.MovieWithGenres
import com.example.movies.view_models.SplashViewModel
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity()
{
    val TAG = "SplashActivity";
    private var viewModel : SplashViewModel? = null;

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        theme.applyStyle(R.style.AppTheme_NoActionBar, true)
        setContentView(R.layout.activity_splash)
        initViewModel()
        startListeners()
        /* get movies using kotlin flow */
        viewModel?.getMoviesDataFlow()
    }

    /* initialize the viewModel of Splash */
    private fun initViewModel()
    {
        viewModel = ViewModelProvider(this).get(SplashViewModel::class.java)
    }

    private fun startListeners()
    {
        viewModel?.isLoading?.observe(this, Observer {
            lightsSetter(it)
        })

        viewModel?.errorListener?.observe(this, Observer {
            Log.e(TAG,"onError fetching movie Data occured: $it");
        })

        viewModel?.movieWithGenres?.observe(this, Observer {

            /* on data generated, move to main activity  */
            goToMainActivity(it)

        })
    }

    private fun lightsSetter(isLoading : Boolean)
    {
        if(isLoading) lightLoader.on() else lightLoader.off()
    }

    private fun goToMainActivity(moviesWithGenres : ArrayList<MovieWithGenres>)
    {
        val intent = (Intent(this@SplashActivity,MainActivity::class.java))
        intent.putExtra("tst", moviesWithGenres)
        startActivity(intent)
        finish()
    }
}