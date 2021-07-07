package com.novasa.activityresultprovider

import android.content.Context
import androidx.activity.result.ActivityResultCaller

interface ActivityResultProvider<I, O> {

    interface Receiver<O> {
        fun onActivityResultReceived(output: O)
        fun onActivityResultError()
    }

    fun onCreate(context: Context, caller: ActivityResultCaller, receiver: Receiver<O>)
    fun onDestroy()
    fun present(context: Context, input: I)
}
