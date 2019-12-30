package ie.noel.dunsceal.views.dunlist

import android.os.Bundle
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import ie.noel.dunsceal.R
import ie.noel.dunsceal.views.BaseView
import kotlinx.android.synthetic.main.activity_dun_list.*
import kotlinx.android.synthetic.main.appbar_fab.*

class DunListView : BaseView() {

  private lateinit var presenter: DunListPresenter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_dun_list)
    super.init(toolbar, false)

    presenter = initPresenter(DunListPresenter(this)) as DunListPresenter

    val layoutManager = LinearLayoutManager(this)
    myRecyclerView.layoutManager = layoutManager
    presenter.loadDuns()
  }

  override fun onOptionsItemSelected(item: MenuItem?): Boolean {
    when (item?.itemId) {
      R.id.item_add -> presenter.doAddDun()
      R.id.item_map -> presenter.doShowDunsMap()
      R.id.item_logout ->presenter.doLogout()
    }
    return super.onOptionsItemSelected(item!!)
  }
}