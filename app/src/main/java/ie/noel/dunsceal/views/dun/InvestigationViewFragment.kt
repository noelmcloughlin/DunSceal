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
import ie.noel.dunsceal.R
import ie.noel.dunsceal.databinding.FragmentDunViewBinding
import ie.noel.dunsceal.models.entity.DunEntity
import ie.noel.dunsceal.utils.Loader
import ie.noel.dunsceal.views.BaseFragment
import kotlinx.android.synthetic.main.fragment_dun_maps.view.mapView
import kotlinx.android.synthetic.main.fragment_dun_add.view.*
import org.jetbrains.anko.AnkoLogger
import java.util.*

class InvestigationViewFragment(val presenter: DunPresenter) : BaseFragment(), AnkoLogger {

  private var mBinding: FragmentDunViewBinding? = null
  //private var mInvestigationAdapter: InvestigationAdapter? = null

  companion object {
    private const val KEY_DUN_ID = "dun_id"
    const val TAG = "DunViewFragment"

    /** Creates dun fragment for specific dun ID  */
    @JvmStatic
    fun forDun(presenter: DunPresenter, dunId: Long): InvestigationViewFragment {
      val fragment = InvestigationViewFragment(presenter)
      val args = Bundle()
      args.putInt(KEY_DUN_ID, dunId.toInt())
      fragment.arguments = args
      return fragment
    }
  }

  override fun onCreateView(
      inflater: LayoutInflater,
      container: ViewGroup?,
      savedInstanceState: Bundle?
  ): View? {
    // Inflate this data binding layout
    mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_dun_view, container, false)
    loader = Loader.createLoader(activity!!)
    activity?.title = getString(R.string.action_dun)

    // Setup the map view
    if (mBinding!!.root.mapView != null) {
      mBinding!!.mapView.onCreate(savedInstanceState)
      mBinding!!.mapView.getMapAsync {
        presenter.doConfigureMap(it)
        it.setOnMapClickListener { presenter.doSetLocation() }
      }
    }

    // setup the image chooser
    if (mBinding!!.root.chooseImage != null) {
      mBinding!!.chooseImage.setOnClickListener { presenter.doSelectImage() }
    }
    return mBinding!!.root
  }

  /*private val mInvestigationClickCallback = object : InvestigationClickCallback() {
    override fun onClick(investigation: Investigation?) {
    }
    // no-op, not needed
  } */

  override fun onResume() {
    super.onResume()
    presenter
  }

  // TODO incorporate this in doAdd()
  private fun writeNewDun(dun: DunEntity) {
    // Create new dun at /duns & /duns/$uid
    Loader.showLoader(loader, "Adding Dun to Firebase")
    val uid = presenter.app.auth.currentUser!!.uid
    val key = presenter.app.db.child("duns").push().key!!.toLong()
    dun.id = key
    val dunValues = dun.toMap()

    val childUpdates = HashMap<String, Any>()
    childUpdates["/duns/$key"] = dunValues
    childUpdates["/user-duns/$uid/$key"] = dunValues

    presenter.app.db.updateChildren(childUpdates)
    Loader.hideLoader(loader)
  }
}