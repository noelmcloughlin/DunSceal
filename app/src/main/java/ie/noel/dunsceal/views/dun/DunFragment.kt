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
package ie.noel.dunsceal.views.dun

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import ie.noel.dunsceal.R
import ie.noel.dunsceal.adapters.InvestigationAdapter
import ie.noel.dunsceal.databinding.DunFragmentBinding
import ie.noel.dunsceal.main.MainApp
import ie.noel.dunsceal.models.Investigation
import ie.noel.dunsceal.persistence.viewmodel.DunViewModel
import ie.noel.dunsceal.utils.Loader
import ie.noel.dunsceal.views.BaseFragment

class DunFragment : BaseFragment() {

  lateinit var app: MainApp
  private var mBinding: DunFragmentBinding? = null
  private var mInvestigationAdapter: InvestigationAdapter? = null

  override fun onCreateView(
      inflater: LayoutInflater,
      container: ViewGroup?,
      savedInstanceState: Bundle?
  ): View? {
    // Inflate this data binding layout (or fragment_edit)
    mBinding = DataBindingUtil.inflate(inflater, R.layout.dun_fragment, container, false)

    loader = Loader.createLoader(activity!!)

    // Create and set the adapter for the RecyclerView.
    mInvestigationAdapter = InvestigationAdapter(mInvestigationClickCallback)
    mBinding!!.investigationList.adapter = mInvestigationAdapter

    return mBinding!!.root
  }

  private val mInvestigationClickCallback = object : InvestigationClickCallback {
    override fun onClick(investigation: Investigation?) {
    }
    // no-op, not needed
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    val factory = DunViewModel.Factory(
        requireActivity().application, arguments!!.getInt(KEY_DUN_ID))
    val model = ViewModelProvider(this, factory)
        .get(DunViewModel::class.java)
    mBinding!!.dunViewModel = model
    subscribeToModel(model)
  }

  private fun subscribeToModel(model: DunViewModel) { // Observe dun data
    model.observableDun!!.observe(viewLifecycleOwner, Observer { dunEntity -> model.setDun(dunEntity!!) })
    // Observe investigations
    model.investigations!!.observe(viewLifecycleOwner, Observer { investigationEntities ->
      if (investigationEntities != null) {
        mBinding!!.isLoading = false
        mInvestigationAdapter!!.setInvestigationList(investigationEntities)
      } else {
        mBinding!!.isLoading = true
      }
    })
  }

  companion object {
    private const val KEY_DUN_ID = "dun_id"
    /** Creates dun fragment for specific dun ID  */
    @JvmStatic
    fun forDun(dunId: Int): DunFragment {
      val fragment = DunFragment()
      val args = Bundle()
      args.putInt(KEY_DUN_ID, dunId)
      fragment.arguments = args
      return fragment
    }
  }
}