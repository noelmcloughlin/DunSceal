package ie.noel.dunsceal.views.location

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import ie.noel.dunsceal.R
import ie.noel.dunsceal.models.entity.LocationEntity
import ie.noel.dunsceal.views.BaseFragment
import ie.noel.dunsceal.views.BaseView
import ie.noel.dunsceal.views.dun.DunView
import kotlinx.android.synthetic.main.appbar_fab_home.*
import org.jetbrains.anko.AnkoLogger

open class LocationView : BaseView(), AnkoLogger {

  lateinit var presenter: LocationPresenter
  var location = LocationEntity()

  companion object {
    const val TAG = "LocationView"
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.nav_drawer_home)
    if (toolbar == null) {
      super.init(toolbar, true, TAG)
    }

    presenter = initPresenter(LocationPresenter(this)) as LocationPresenter

    // Add fragment if this is first creation
    if (savedInstanceState == null) {
      val fragment : BaseFragment = LocationFragment.newInstance(presenter)
      fragManager.beginTransaction()
          .addToBackStack(DunView.TAG)
          .replace(R.id.home, fragment, LocationFragment.TAG).commit()
    }

  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    menuInflater.inflate(R.menu.menu_edit_location, menu)
    return super.onCreateOptionsMenu(menu)
  }

  override fun onOptionsItemSelected(item: MenuItem?): Boolean {
    when (item?.itemId) {
      R.id.menu_location_item_save -> {
        presenter.doSaveMapLocation()
      }
    }
    return super.onOptionsItemSelected(item)
  }
}