package com.example.movies.activities.main_activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import com.example.movies.R
import com.example.movies.models.MovieWithGenres
import com.example.movies.utils.utilities.back_listener.BackButtonHandlerInterface
import com.example.movies.utils.utilities.back_listener.OnBackClickListener
import java.lang.ref.WeakReference


class MainActivity : AppCompatActivity(), BackButtonHandlerInterface {
    val TAG = "MainActivity";
    private val backClickListenersList: ArrayList<WeakReference<OnBackClickListener>> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val listMovies = intent.extras?.getParcelableArrayList<MovieWithGenres>("tst")

        val navGraph = Navigation.findNavController(this, R.id.navController)
        val bundle = bundleOf("tst" to listMovies)
        navGraph.setGraph(navGraph.graph, bundle)
    }

    override fun onBackPressed()
    {
        if(!fragmentsBackKeyIntercept()){
            super.onBackPressed();
        }
    }

    override fun addBackClickListener(onBackClickListener: OnBackClickListener) {
        backClickListenersList.add(WeakReference(onBackClickListener))
    }

    override fun removeBackClickListener(onBackClickListener: OnBackClickListener) {
        val iterator = backClickListenersList.iterator()
        while (iterator.hasNext()) {
            val weakRef = iterator.next()
            if (weakRef.get() === onBackClickListener) {
                iterator.remove()
            }
        }
    }

    private fun fragmentsBackKeyIntercept(): Boolean {
        var isIntercept = false
        for (weakRef in backClickListenersList) {
            val onBackClickListener = weakRef.get()
            if (onBackClickListener != null) {
                val isFragmIntercept = onBackClickListener.onBackClick()
                if (!isIntercept) isIntercept = isFragmIntercept
            }
        }
        return isIntercept
    }

}
