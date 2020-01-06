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
package ie.noel.dunsceal.persistence.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import ie.noel.dunsceal.main.MainApp
import ie.noel.dunsceal.models.entity.Dun
import ie.noel.dunsceal.persistence.DataRepository

class DunListViewModel(application: Application) : AndroidViewModel(application) {
  private val mRepository: DataRepository?
  // MediatorLiveData can observe other LiveData objects and react on their emissions.
  private val mObservableDuns: MediatorLiveData<List<Dun?>?> = MediatorLiveData()

  /**
   * Expose the LiveData Duns query so the UI can observe it.
   */
  val duns: LiveData<List<Dun?>?>
    get() = mObservableDuns

  fun searchDuns(query: String?): LiveData<List<Dun?>?>? {
    return mRepository!!.searchDuns(query)
  }

  init {
    // set by default null, until we get data from the database.
    mObservableDuns.value = null
    mRepository = (application as MainApp).repository
    val duns = mRepository!!.duns
    // observe the changes of the duns from the database and forward them
    mObservableDuns.addSource(duns) { value: List<Dun?>? -> mObservableDuns.setValue(value) }
  }
}