package com.novasa.activityresultprovider

import android.content.Context
import androidx.activity.result.ActivityResultCaller
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract

abstract class ContractActivityResultProvider<I, O, CI, CO> : ActivityResultProvider<I, O> {

    private var launcher: ActivityResultLauncher<CI>? = null

    abstract fun createContract(): ActivityResultContract<CI, CO>

    open fun createContractInput(context: Context, input: I): CI = input as CI

    open fun parseOutput(context: Context, contractOutput: CO): O = contractOutput as O

    override fun onCreate(context: Context, caller: ActivityResultCaller, receiver: ActivityResultProvider.Receiver<O>) {
        launcher = caller.registerForActivityResult(createContract()) { contractOutput ->
            parseOutput(context, contractOutput)?.let { output ->
                receiver.onActivityResultReceived(output)
            }
        }
    }

    override fun onDestroy() {
        launcher?.unregister()
    }

    override fun present(context: Context, input: I) {
        launcher?.launch(createContractInput(context, input))
    }
}