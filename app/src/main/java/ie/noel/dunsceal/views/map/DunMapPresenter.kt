package ie.noel.dunsceal.views.map

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import ie.noel.dunsceal.models.DunModel
import ie.noel.dunsceal.views.BasePresenter
import ie.noel.dunsceal.views.BaseView

class DunMapPresenter(view: BaseView) : BasePresenter(view) {

  fun doPopulateMap(map: GoogleMap, duns: List<DunModel>) {
    map.uiSettings.isZoomControlsEnabled = true
    duns.forEach {
      val loc = LatLng(it.location.lat, it.location.lng)
      val options = MarkerOptions().title(it.title).position(loc)
      map.addMarker(options).tag = it
      map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, it.location.zoom))
    }
  }

  fun doMarkerSelected(marker: Marker) {
    if (marker.tag != null) {
      val tag = marker.tag
      doAsync {
        val dun = tag as DunModel
        uiThread {
          if (dun != null) view?.showDun(dun)
        }
      }
    }
  }

  fun loadDuns() {
    doAsync {
      val duns = app.duns.findAll()
      uiThread {
        view?.showDuns(duns as ArrayList<DunModel>)
      }
    }
  }
}