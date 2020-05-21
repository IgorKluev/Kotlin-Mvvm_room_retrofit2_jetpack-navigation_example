package com.example.movies.fragments.base

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.movies.utils.utilities.back_listener.BackButtonHandlerInterface
import com.example.movies.utils.utilities.back_listener.OnBackClickListener


/**
 * A simple [Fragment] subclass.
 */
abstract class BaseFragment : Fragment(), OnBackClickListener
{
    lateinit var rootView : View
    lateinit var navController : NavController
    private var backButtonHandler: BackButtonHandlerInterface? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        // Inflate the layout for this fragment
         rootView = inflater.inflate(getLayoutResource(), container, false)
         return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(rootView)
        initViewModel()
        setLiveDataListeners()
        setClickListeners()
        onViewCreated()
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        backButtonHandler = activity as BackButtonHandlerInterface
        backButtonHandler?.addBackClickListener(this)
    }

    override fun onDetach() {
        super.onDetach()
        backButtonHandler?.removeBackClickListener(this)
        backButtonHandler = null
    }

    abstract fun getLayoutResource() : Int
    abstract fun onViewCreated()
    abstract fun initViewModel()
    abstract fun setLiveDataListeners()
    abstract fun setClickListeners()
}
