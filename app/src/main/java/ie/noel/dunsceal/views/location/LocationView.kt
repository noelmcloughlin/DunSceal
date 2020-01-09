package ie.noel.dunsceal.views.location

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.Marker
import ie.noel.dunsceal.R
import ie.noel.dunsceal.models.entity.DunEntity
import ie.noel.dunsceal.models.entity.LocationEntity
import ie.noel.dunsceal.views.BaseFragment
import ie.noel.dunsceal.views.BaseView
import ie.noel.dunsceal.views.dun.DunPresenter
import ie.noel.dunsceal.views.dun.InvestigationViewFragment
import kotlinx.android.synthetic.main.activity_edit_location.*
import kotlinx.android.synthetic.main.appbar_fab_home.*
import org.jetbrains.anko.AnkoLogger
import org.wit.placemark.R
import org.wit.placemark.views.BaseView

open class LocationView : BaseView(), AnkoLogger {

  lateinit var presenter: LocationPresenter
  var location = LocationEntity()

  companion object {
    const val TAG = "DunMapView"
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.nav_drawer_home)
    super.init(toolbar, true, TAG)

    presenter = initPresenter(LocationPresenter(this)) as LocationPresenter

    // Add fragment if this is first creation
    if (savedInstanceState == null) {
      val fragment : BaseFragment = LocationFragment.forLocation(presenter, location.)
      fragManager.beginTransaction()
          .replace(R.id.home, fragment, InvestigationViewFragment.TAG).commit()
    }
    presenter.loadDuns()
  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    menuInflater.inflate(R.menu.menu_edit_location, menu)
    return super.onCreateOptionsMenu(menu)
  }

  override fun onOptionsItemSelected(item: MenuItem?): Boolean {
    when (item?.itemId) {
      R.id.item_save -> {
        presenter.doSave()
      }
    }
    return super.onOptionsItemSelected(item)
  }

  override fun onMarkerDragStart(marker: Marker) {}

  override fun onMarkerDrag(marker: Marker) {
    lat.setText("%.6f".format(marker.position.latitude))
    lng.setText("%.6f".format(marker.position.longitude))
  }

  override fun onMarkerDragEnd(marker: Marker) {
    presenter.doUpdateLocation(marker.position.latitude, marker.position.longitude)
  }

  override fun onMarkerClick(marker: Marker): Boolean {
    presenter.doUpdateMarker(marker)
    return false
  }

  override fun onDestroy() {
    super.onDestroy()
    mapView.onDestroy()
  }

  override fun onLowMemory() {
    super.onLowMemory()
    mapView.onLowMemory()
  }

  override fun onPause() {
    super.onPause()
    mapView.onPause()
  }

  override fun onResume() {
    super.onResume()
    mapView.onResume()
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    mapView.onSaveInstanceState(outState)
  }
}