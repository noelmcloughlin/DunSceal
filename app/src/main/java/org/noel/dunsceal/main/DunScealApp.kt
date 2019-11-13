package org.noel.dunsceal.main

import android.app.Application
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.noel.dunsceal.R
import org.noel.dunsceal.models.DunJSONStore
import org.noel.dunsceal.models.DunStore

class DunScealApp : Application(), AnkoLogger {

  lateinit var duns: DunStore

  override fun onCreate() {
    super.onCreate()
    duns = DunJSONStore(applicationContext)
    info("${R.string.app_name} started")
  }
}