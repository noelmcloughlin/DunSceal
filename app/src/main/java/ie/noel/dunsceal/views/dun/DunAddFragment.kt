package ie.noel.dunsceal.views.dun

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import ie.noel.dunsceal.R
import ie.noel.dunsceal.databinding.FragmentDunAddBinding
import ie.noel.dunsceal.models.entity.DunEntity
import ie.noel.dunsceal.models.entity.LocationEntity
import ie.noel.dunsceal.utils.Loader
import ie.noel.dunsceal.views.BaseFragment
import ie.noel.dunsceal.views.home.HomeView
import kotlinx.android.synthetic.main.fragment_dun_add.chooseImage
import kotlinx.android.synthetic.main.fragment_dun_add.description
import kotlinx.android.synthetic.main.fragment_dun_add.dunImage
import kotlinx.android.synthetic.main.fragment_dun_add.dunLatitude
import kotlinx.android.synthetic.main.fragment_dun_add.dunLongitude
import kotlinx.android.synthetic.main.fragment_dun_add.name
import kotlinx.android.synthetic.main.fragment_dun_add.view.*
import kotlinx.android.synthetic.main.fragment_dun_maps.view.mapView
import org.jetbrains.anko.AnkoLogger

class DunAddFragment(val presenter: DunPresenter, private val user: String)
  : BaseFragment(), AnkoLogger {

  var dun = DunEntity()
  private var mBinding: FragmentDunAddBinding? = null

  companion object {
    const val TAG = "DunAddFragment"

    /** Creates empty dun fragment  */
    @JvmStatic
    fun newInstance(presenter: DunPresenter, user: String): DunAddFragment {
      return DunAddFragment(presenter, user).apply {
        arguments = Bundle().apply {}
      }
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    // We need new options menu
    setHasOptionsMenu(true)
    super.onCreate(savedInstanceState)
  }

  override fun onCreateView(
      inflater: LayoutInflater,
      container: ViewGroup?,
      savedInstanceState: Bundle?
  ): View? {

    super.onCreateView(inflater, container, savedInstanceState)

    // Inflate this data binding layout
    mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_dun_add, container, false)
    loader = Loader.createLoader(activity!!)
    activity?.title = getString(R.string.action_dun)

    // Setup the map view
    if (mBinding!!.mapView != null) {
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

  fun showDun(dun: DunEntity) {
    name.setText(dun.name)
    description.setText(dun.description)
    Glide.with(this).load(dun.image).into(dunImage)
    if (dun.image != null) {
      chooseImage.setText(R.string.change_dun_image)
    }
    this.showLocation(dun.location)
  }

  private fun showLocation(location: LocationEntity) {
    dunLatitude.text = ("%.6f".format(location.latitude))
    dunLongitude.text = ("%.6f".format(location.longitude))
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    if (data != null) {
      presenter.doActivityResult(requestCode, resultCode, data)
    }
  }

  override fun onDestroy() {
    super.onDestroy()
    mBinding!!.mapView.onDestroy()
  }

  override fun onLowMemory() {
    super.onLowMemory()
    mBinding!!.mapView.onLowMemory()
  }

  override fun onPause() {
    super.onPause()
    mBinding!!.mapView.onPause()
  }

  override fun onResume() {
    super.onResume()
    mBinding!!.mapView.onResume()
    presenter.doRestartLocationUpdates()
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    mBinding!!.mapView.onSaveInstanceState(outState)
  }

  // OPTIONS MENU
  override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
    inflater.inflate(R.menu.menu_dun, menu)
    super.onCreateOptionsMenu(menu!!, inflater)
  }

  //handle item clicks
  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when (item?.itemId) {
      R.id.menu_dun_item_delete -> {
        presenter.doDelete()
      }
      R.id.menu_dun_item_save -> {
        if (name.text.toString().isEmpty()) {
          // anko toast is not working inside fragment, using Toast instead.
          Toast.makeText(
              context, R.string.enter_dun_title,
              Toast.LENGTH_SHORT
          ).show()
        } else {
          presenter.doAddOrSave(name.text.toString(), description.text.toString())
        }
      }
    }
    presenter.dataStore!!.fetchDuns {
      (activity as HomeView).fragManager.popBackStackImmediate()
    }
    return true
  }
}


