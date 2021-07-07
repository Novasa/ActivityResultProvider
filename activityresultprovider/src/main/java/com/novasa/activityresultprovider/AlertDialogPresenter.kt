package com.novasa.activityresultprovider

import android.content.Context
import android.widget.ArrayAdapter
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AlertDialog

class AlertDialogPresenter<I, O>(private val config: Config) : DelegateActivityResultProvider.Presenter<I, O> {

    class Config(
        val title: CharSequence,
        @LayoutRes val layout: Int,
        @IdRes val titleId: Int
    )

    override fun preset(context: Context, input: I, providers: List<ActivityResultProvider<I, O>>) {

        val adapter = ArrayAdapter(context, config.layout, config.titleId, providers)

        AlertDialog.Builder(context)
            .setTitle(config.title)
            .setAdapter(adapter) { _, which -> providers[which].present(context, input) }
            .create()
            .show()
    }
}
