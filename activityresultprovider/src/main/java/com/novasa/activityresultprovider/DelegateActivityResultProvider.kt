package com.novasa.activityresultprovider

import android.content.Context
import androidx.activity.result.ActivityResultCaller

abstract class DelegateActivityResultProvider<I, O>(private val providers: Set<ActivityResultProvider<I, O>>) : ActivityResultProvider<I, O> {

    protected abstract val presenter: Presenter<I, O>

    interface Delegate<I> {
        fun filter(context: Context, input: I): Boolean = true
    }

    interface Presenter<I, O> {
        fun preset(context: Context, input: I, providers: List<ActivityResultProvider<I, O>>)
    }

    override fun onCreate(context: Context, caller: ActivityResultCaller, receiver: ActivityResultProvider.Receiver<O>) {
        for (provider in providers) {
            provider.onCreate(context, caller, receiver)
        }
    }

    override fun onDestroy() {
        for (provider in providers) {
            provider.onDestroy()
        }
    }

    final override fun present(context: Context, input: I) {

        @Suppress("UNCHECKED_CAST")
        val providers = this.providers.filter { (it as? Delegate<I>)?.filter(context, input) ?: true }

        if (providers.isNotEmpty()) {
            if (providers.size == 1) {
                providers.first().present(context, input)

            } else {
                presenter.preset(context, input, providers)
            }
        }
    }
}
