package com.novasa.activityresultprovider.permissions

import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import com.novasa.activityresultprovider.ContractActivityResultProvider

class RequestPermissionProvider : ContractActivityResultProvider<String, Boolean, String, Boolean>() {

    override fun createContract(): ActivityResultContract<String, Boolean> = ActivityResultContracts.RequestPermission()

}
