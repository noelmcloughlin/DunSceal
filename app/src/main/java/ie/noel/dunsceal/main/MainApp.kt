/*
 * Copyright 2017, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ie.noel.dunsceal.main

import android.app.Application
import android.net.Uri
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference
import ie.noel.dunsceal.models.DunStore
import ie.noel.dunsceal.persistence.db.DunFireStore
import ie.noel.dunsceal.persistence.db.LiveDataRepository
import ie.noel.dunsceal.persistence.db.room.DunDatabase
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

/**
 * Android Application class. Used for accessing singletons.
 */
class MainApp : Application(), AnkoLogger {

  private var mAppExecutors: AppExecutors? = null

  lateinit var duns: DunStore
  lateinit var liveduns: LiveDataRepository
  lateinit var auth: FirebaseAuth
  lateinit var googleSignInClient: GoogleSignInClient
  lateinit var userImage: Uri
  lateinit var db: DatabaseReference
  lateinit var st: StorageReference
  lateinit var userId: String

  override fun onCreate() {
    super.onCreate()
    mAppExecutors = AppExecutors()
    duns = DunFireStore(applicationContext)
    info("Dun started")
  }

  // helpers for livedata repository
  private fun getDatabase(): DunDatabase? {
    return DunDatabase.getInstance(this, mAppExecutors!!)
  }

  open fun getRepository(): LiveDataRepository? {
    return LiveDataRepository.getInstance(getDatabase()!!)
  }
}