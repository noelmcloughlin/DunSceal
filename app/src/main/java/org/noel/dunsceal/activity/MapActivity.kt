package org.noel.dunsceal.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.startActivityForResult
import org.noel.dunsceal.R
import org.noel.dunsceal.models.Location


class MapActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerDragListener, GoogleMap.OnMarkerClickListener {

  private lateinit var map: GoogleMap
  var location = Location()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.fragment_map)
    location = intent.extras?.getParcelable<Location>("location")!!
    val mapFragment = supportFragmentManager
      .findFragmentById(R.id.map) as SupportMapFragment
    mapFragment.getMapAsync(this)
  }

  override fun onMapReady(googleMap: GoogleMap) {
    map = googleMap
    map.setOnMarkerDragListener(this)
    map.setOnMarkerClickListener(this)
    val loc = LatLng(location.lat, location.lng)
    val options = MarkerOptions()
      .title("Dun")
      .snippet("GPS : " + loc.toString())
      .draggable(true)
      .position(loc)
    map.addMarker(options)
    map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, location.zoom))
  }

  override fun onMarkerDragStart(marker: Marker) {
  }

  override fun onMarkerDrag(marker: Marker) {
  }

  override fun onMarkerDragEnd(marker: Marker) {
    location.lat = marker.position.latitude
    location.lng = marker.position.longitude
    location.zoom = map.cameraPosition.zoom
  }

  override fun onBackPressed() {
    val resultIntent = Intent()
    resultIntent.putExtra("location", location)
    setResult(Activity.RESULT_OK, resultIntent)
    finish()
    super.onBackPressed()
  }

  override fun onMarkerClick(marker: Marker): Boolean {
    val loc = LatLng(location.lat, location.lng)
    marker.setSnippet("GPS : " + loc.toString())
    return false
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.menu_dun_cancel, menu)
    menuInflater.inflate(R.menu.menu_dun_list, menu)
    menuInflater.inflate(R.menu.menu_dun_users, menu)
    return super.onCreateOptionsMenu(menu)
  }

  override fun onOptionsItemSelected(item: MenuItem?): Boolean {
    when (item?.itemId) {
      R.id.action_dun_add -> startActivityForResult<DunActivity>(0)
      R.id.action_dun_list-> startActivity<DunListActivity>()
      R.id.action_dun_users -> startActivity<DunUserActivity>()
      else -> finish()
    }
    return super.onOptionsItemSelected(item)
  }
}