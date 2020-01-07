package ie.noel.dunsceal.views.home.map

import android.os.Bundle
import com.bumptech.glide.Glide
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import ie.noel.dunsceal.R
import ie.noel.dunsceal.models.entity.DunEntity
import ie.noel.dunsceal.views.BaseView
import kotlinx.android.synthetic.main.appbar_fab_home.*
import kotlinx.android.synthetic.main.content_dun_maps.*

class DunMapView : BaseView(), GoogleMap.OnMarkerClickListener {

  private lateinit var presenter: DunMapPresenter
  lateinit var map : GoogleMap

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_dun_map)
    super.init(toolbar, true)

    presenter = initPresenter (DunMapPresenter(this)) as DunMapPresenter

    mapView.onCreate(savedInstanceState)
    mapView.getMapAsync {
      map = it
      map.setOnMarkerClickListener(this)
      presenter.loadDuns()
    }
  }

  override fun showDun(dun: DunEntity) {
    currentTitle.text = dun.name
    currentDescription.text = dun.description
    Glide.with(this).load(dun.image).into(currentImage)
  }

  override fun getAllDuns(duns: ArrayList<DunEntity>) {
    presenter.doPopulateMap(map, duns)
  }

  override fun onMarkerClick(marker: Marker): Boolean {
    presenter.doMarkerSelected(marker)
    return true
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