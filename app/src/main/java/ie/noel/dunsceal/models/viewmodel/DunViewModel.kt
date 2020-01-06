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
package ie.noel.dunsceal.models.viewmodel

import android.app.Application
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory
import ie.noel.dunsceal.main.MainApp
import ie.noel.dunsceal.models.entity.DunEntity
import ie.noel.dunsceal.models.entity.InvestigationEntity
import ie.noel.dunsceal.persistence.DataRepository

class DunViewModel(application: Application, repository: DataRepository?,
                   private val mDunId: Long) : AndroidViewModel(application) {
  val observableDunEntity: LiveData<DunEntity?>? = repository!!.loadDun(mDunId)
  var dun = ObservableField<DunEntity>()
  /**
   * Expose the LiveData Investigations query so the UI can observe it.
   */
  val investigations: LiveData<List<InvestigationEntity>?>? = repository!!.loadInvestigations(mDunId)

  fun setDun(dun: DunEntity) {
    this.dun.set(dun)
  }

  /**
   * A creator is used to inject the dun ID into the ViewModel
   *
   *
   * This creator is to showcase how to inject dependencies into ViewModels. It's not
   * actually necessary in this case, as the dun ID can be passed in a public method.
   */
  class Factory(private val mApplication: Application, private val mDunId: Int) : NewInstanceFactory() {
    private val mRepository: DataRepository? = (mApplication as MainApp).repository
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
      return DunViewModel(mApplication, mRepository, mDunId.toLong()) as T
    }

  }

}