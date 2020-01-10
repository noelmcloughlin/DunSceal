package ie.noel.dunsceal.views.location

import android.content.Intent
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import ie.noel.dunsceal.models.entity.LocationEntity
import ie.noel.dunsceal.views.BaseView
import ie.noel.dunsceal.views.home.HomePresenter

open class LocationPresenter(view: BaseView) : HomePresenter(view) {

  var location = LocationEntity()

  init {
    if (view.intent.extras != null) {
      location = view.intent.extras?.getParcelable("location")!!
    }
  }

  fun doConfigureMapLocation(map: GoogleMap) {
    val loc = LatLng(location.latitude, location.longitude)
    val options = MarkerOptions()
      .title("Dun")
      .snippet("GPS : $loc")
      .draggable(true)
      .position(loc)
    map.addMarker(options)
    map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, location.zoom))
  }

  fun doUpdateMapLocation(latitude: Double, longitude: Double) {
    location.latitude = latitude
    location.longitude = longitude
  }

  fun doSaveMapLocation() {
    val resultIntent = Intent()
    resultIntent.putExtra("location", location)
    view?.setResult(0, resultIntent)
    view?.fragManager!!.popBackStackImmediate()
  }

  fun doUpdateMapMarker(marker: Marker) {
    val loc = LatLng(location.latitude, location.longitude)
    marker.snippet = "GPS : $loc"
  }

  fun doCancel() {
    view?.fragManager!!.popBackStackImmediate()
  }
}