package ie.noel.dunsceal.views.home.location

import android.content.Intent
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import ie.noel.dunsceal.models.entity.LocationEntity
import ie.noel.dunsceal.views.home.HomePresenter

class EditLocationPresenter(view: EditLocationView) : HomePresenter(view) {

  var location = LocationEntity()

  init {
    location = view.intent.extras?.getParcelable("location")!!
  }

  fun doConfigureMap(map: GoogleMap) {
    val loc = LatLng(location.latitude, location.longitude)
    val options = MarkerOptions()
      .title("Dun")
      .snippet("GPS : $loc")
      .draggable(true)
      .position(loc)
    map.addMarker(options)
    map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, location.zoom))
  }

  fun doUpdateLocation(latitude: Double, longitude: Double) {
    location.latitude = latitude
    location.longitude = longitude
  }

  fun doSave() {
    val resultIntent = Intent()
    resultIntent.putExtra("location", location)
    view?.setResult(0, resultIntent)
    view?.finish()
  }

  fun doUpdateMarker(marker: Marker) {
    val loc = LatLng(location.latitude, location.longitude)
    marker.snippet = "GPS : $loc"
  }
}