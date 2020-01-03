
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
package ie.noel.dunsceal.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import ie.noel.dunsceal.R
import ie.noel.dunsceal.adapters.DunViewAdapter
import ie.noel.dunsceal.databinding.ListFragmentBinding
import ie.noel.dunsceal.main.TestActivity
import ie.noel.dunsceal.models.Dun
import ie.noel.dunsceal.models.entity.DunEntity
import ie.noel.dunsceal.persistence.viewmodel.DunListViewModel

class DunListFragment : Fragment() {
  private var mDunViewAdapter: DunViewAdapter? = null
  private var mBinding: ListFragmentBinding? = null
  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                            savedInstanceState: Bundle?): View? {
    mBinding = DataBindingUtil.inflate(inflater, R.layout.list_fragment, container, false)
    mDunViewAdapter = DunViewAdapter(mDunClickCallback)
    mBinding!!.dunsList.adapter = mDunViewAdapter
    return mBinding!!.root
  }

  @Override
  override fun onActivityCreated(@Nullable savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    val viewModel = ViewModelProvider(this).get(DunListViewModel::class.java)

    mBinding!!.dunsSearchBtn.setOnClickListener {
      val query = mBinding!!.dunsSearchBox.text
      if (query == null || query.toString().isEmpty()) {
        subscribeUi(viewModel.duns)
      } else {
        subscribeUi(viewModel.searchDuns("*$query*"))
      }
    }
    subscribeUi(viewModel.duns)
  }

  private fun subscribeUi(liveData: LiveData<List<DunEntity>>) { // Update the list when the data changes
    liveData.observe(this.viewLifecycleOwner, Observer { myDuns: List<DunEntity>? ->
      if (myDuns != null) {
          mBinding!!.isLoading = false
          mDunViewAdapter!!.setDunList(myDuns)
      } else {
        mBinding!!.isLoading = true
      }
      // espresso does not know how to wait for data binding's loop so we execute changes
// sync.
      mBinding!!.executePendingBindings()
    })
  }

  private val mDunClickCallback = DunClickCallback { dunModel: Dun? ->
    if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
      (activity as TestActivity?)!!.show(dunModel!!)
    }
  }

  companion object {
    const val TAG = "DunListFragment"
  }
}