package org.noel.dunsceal.main

import android.app.Application
import android.util.Log
import org.noel.dunsceal.R
import org.noel.dunsceal.model.DunMemStore
import org.noel.dunsceal.model.DunStore

class DunScealApp : Application() {

    lateinit var dunStore: DunStore

    override fun onCreate() {
        super.onCreate()
        dunStore = DunMemStore()
        Log.v("Donate", (R.string.app_name + R.string.app_started).toString())
    }
}