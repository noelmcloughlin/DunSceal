package ie.noel.dunsceal.views.home.dunlist

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import ie.noel.dunsceal.R
import ie.noel.dunsceal.models.entity.DunEntity
import ie.noel.dunsceal.views.home.HomePresenter
import ie.noel.dunsceal.views.home.HomeView
import ie.noel.dunsceal.views.home.dun.DunFragment
import ie.noel.dunsceal.views.home.dun.DunPresenter
import kotlinx.android.synthetic.main.activity_dun_list.*
import kotlinx.android.synthetic.main.appbar_fab_home.*
import org.jetbrains.anko.AnkoLogger

class DunListView : HomeView(), AnkoLogger, DunListener {

  lateinit var presenter: DunListPresenter

  companion object {
    private const val TAG = "DunListView"
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.dunlist_activity)
    if (toolbar == null) {
      super.init(toolbar, false)
    }

    presenter = initPresenter(DunListPresenter(this)) as DunListPresenter

    // Add fragment if this is first creation
    if (savedInstanceState == null) {
      val fragment = DunListFragment.newInstance(presenter)
      // Start home fragment
      R.id.content_home_frame.addFragment(fragment, TAG)
    }

    val layoutManager = LinearLayoutManager(this)
    myRecyclerView.layoutManager = layoutManager
    presenter.loadDuns()
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.menu_home, menu)
    return true   // TODO check if we call super
  }

  override fun onOptionsItemSelected(item: MenuItem?): Boolean {
    when (item?.itemId) {
      R.id.menu_dunlist_item_add -> presenter.doAdd()
      R.id.menu_dunlist_item_map -> presenter.doShowMap()
      R.id.menu_dunlist_item_logout -> presenter.doLogout()
    }
    return super.onOptionsItemSelected(item!!)
  }

  override fun onDunClick(dun: DunEntity) {
    presenter.doEdit(dun)
  }
}