package ie.noel.dunsceal.views.home.dunlist

import android.os.Bundle
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import ie.noel.dunsceal.R
import ie.noel.dunsceal.models.Dun
import ie.noel.dunsceal.views.BaseView
import ie.noel.dunsceal.views.home.HomePresenter
import ie.noel.dunsceal.views.home.HomeView
import ie.noel.dunsceal.views.home.dun.DunFragment
import kotlinx.android.synthetic.main.activity_dun_list.*
import kotlinx.android.synthetic.main.appbar_fab.*
import org.jetbrains.anko.AnkoLogger

class DunListView(var presenter: HomePresenter) : HomeView(), AnkoLogger {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.dunlist_activity)
    super.init(toolbar, false)

    presenter = initPresenter(HomePresenter(this)) as HomePresenter

    // Add dun list fragment if this is first creation
    if (savedInstanceState == null) {
      val fragment = DunListFragment(presenter)
      supportFragmentManager.beginTransaction()
          .add(R.id.fragment_container, fragment, DunListFragment.TAG).commit()

      val layoutManager = LinearLayoutManager(this)
      myRecyclerView.layoutManager = layoutManager
      presenter.loadDuns()

    }
  }

  override fun onOptionsItemSelected(item: MenuItem?): Boolean {
    when (item?.itemId) {
      R.id.item_add -> presenter.doAdd()
      R.id.item_map -> presenter.doShowMap()
    }
    return super.onOptionsItemSelected(item!!)
  }
}