package ie.noel.dunsceal.views.home.dun

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.bumptech.glide.Glide
import org.jetbrains.anko.AnkoLogger
import ie.noel.dunsceal.R
import ie.noel.dunsceal.models.entity.Dun
import ie.noel.dunsceal.models.entity.Location
import ie.noel.dunsceal.views.BaseView
import kotlinx.android.synthetic.main.activity_dun.*
import kotlinx.android.synthetic.main.appbar_fab.*
import org.jetbrains.anko.toast

class DunView : BaseView(), AnkoLogger {

  lateinit var presenter: DunPresenter
  var dun = Dun()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_dun)
    super.init(toolbar, true)

    presenter = initPresenter (DunPresenter(this)) as DunPresenter

    mapView.onCreate(savedInstanceState)
    mapView.getMapAsync {
      presenter.doConfigureMap(it)
      it.setOnMapClickListener { presenter.doSetLocation() }
    }

    chooseImage.setOnClickListener { presenter.doSelectImage() }
  }

  override fun showDun(dun: Dun) {
    dunTitle.setText(dun.name)
    description.setText(dun.description)
    Glide.with(this).load(dun.image).into(dunImage)
    chooseImage.setText(R.string.change_dun_image)
    this.showLocation(dun.location)
  }

  @SuppressLint("SetTextI18n")
  override fun showLocation(location: Location) {
    dunLatitude.text = "%.6f".format(location.latitude)
    dunLongitude.text = "%.6f".format(location.longitude)
  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    menuInflater.inflate(R.menu.menu_dun, menu)
    return super.onCreateOptionsMenu(menu)
  }

  override fun onOptionsItemSelected(item: MenuItem?): Boolean {
    when (item?.itemId) {
      R.id.item_delete -> {
        presenter.doDelete()
      }
      R.id.item_save -> {
        if (dunTitle.text.toString().isEmpty()) {
          toast(R.string.enter_dun_title)
        } else {
          presenter.doAddOrSave(dunTitle.text.toString(), description.text.toString())
        }
      }
    }
    return super.onOptionsItemSelected(item)
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    if (data != null) {
      presenter.doActivityResult(requestCode, resultCode, data)
    }
  }

  override fun onBackPressed() {
    presenter.doCancel()
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
    presenter.doRestartLocationUpdates()
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    mapView.onSaveInstanceState(outState)
  }
}


