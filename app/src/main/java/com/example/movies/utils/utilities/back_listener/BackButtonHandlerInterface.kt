package com.example.movies.utils.utilities.back_listener

import com.example.movies.utils.utilities.back_listener.OnBackClickListener

interface BackButtonHandlerInterface
{
    fun addBackClickListener(onBackClickListener : OnBackClickListener)
    fun removeBackClickListener (onBackClickListener : OnBackClickListener)
}