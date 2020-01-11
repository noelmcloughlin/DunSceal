package ie.noel.dunsceal.views.location

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import ie.noel.dunsceal.R
import ie.noel.dunsceal.databinding.FragmentLocationBinding
import ie.noel.dunsceal.models.entity.LocationEntity
import ie.noel.dunsceal.utils.Loader
import ie.noel.dunsceal.views.BaseFragment
import ie.noel.dunsceal.views.home.HomeView
import kotlinx.android.synthetic.main.fragment_location.*
import org.jetbrains.anko.AnkoLogger

class LocationFragment(val presenter: LocationPresenter)
  : BaseFragment(), AnkoLogger, GoogleMap.OnMarkerDragListener, GoogleMap.OnMarkerClickListener {

  var location = LocationEntity()
  private var mBinding: FragmentLocationBinding? = null

  companion object {
    private const val KEY_LOCATION_ID = "location_id"  // not used
    const val TAG = "LocationEditFragment"

    /** Creates blank location dun fragment  */
    @JvmStatic
    fun newInstance(presenter: LocationPresenter): LocationFragment {
      return LocationFragment(presenter).apply {
        arguments = Bundle().apply {}
      }
    }

    /** Creates location fragment for specific location ID  */
    @JvmStatic
    fun forLocation(presenter: LocationPresenter, locationId: Long): LocationFragment {
      val fragment = LocationFragment(presenter)
      val args = Bundle()
      args.putInt(KEY_LOCATION_ID, locationId.toInt())
      fragment.arguments = args
      return fragment
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
    mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_location, container, false)
    loader = Loader.createLoader(activity!!)
    activity?.title = getString(R.string.action_location_edit)

    // Setup the map view
    if (mBinding!!.mapViewLocation != null) {
      mBinding!!.mapViewLocation.onCreate(savedInstanceState)
      mBinding!!.mapViewLocation.getMapAsync {
        it.setOnMarkerDragListener(this)
        it.setOnMarkerClickListener(this)
        presenter.doConfigureMapLocation(it)
      }
    }
    return mBinding!!.root
  }

  override fun onMarkerDragStart(marker: Marker) {}

  @SuppressLint("SetTextI18n")
  override fun onMarkerDrag(marker: Marker) {
    dunLatitude.text = "%.6f".format(marker.position.latitude)
    dunLongitude.text = "%.6f".format(marker.position.longitude)
  }

  override fun onMarkerDragEnd(marker: Marker) {
    presenter.doUpdateMapLocation(marker.position.latitude, marker.position.longitude)
  }

  override fun onMarkerClick(marker: Marker): Boolean {
    presenter.doUpdateMapMarker(marker)
    return false
  }

  override fun onDestroy() {
    super.onDestroy()
    mBinding!!.mapViewLocation.onDestroy()
  }

  override fun onLowMemory() {
    super.onLowMemory()
    mBinding!!.mapViewLocation.onLowMemory()
  }

  override fun onPause() {
    super.onPause()
    mBinding!!.mapViewLocation.onPause()
  }

  override fun onResume() {
    super.onResume()
    mBinding!!.mapViewLocation.onResume()
  }

  /*fun onBackPressed() {
    (activity as HomeView).fragManager.popBackStackImmediate()
  }*/

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    mBinding!!.mapViewLocation.onSaveInstanceState(outState)
  }

  // OPTIONS MENU
  override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
    inflater.inflate(R.menu.menu_edit_location, menu)
    return super.onCreateOptionsMenu(menu, inflater)
  }

  //handle item clicks
  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when (item.itemId) {
      R.id.menu_location_item_save -> {
        presenter.doSaveMapLocation()
      }
    }
    presenter.dunDataStore!!.fetchDuns {
      if (activity != null)
        (activity as HomeView).fragManager.popBackStackImmediate()
    }
    return true
  }
}