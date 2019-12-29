package ie.noel.dunsceal.views.dunlist

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import ie.noel.dunsceal.R
import ie.noel.dunsceal.models.DunModel
import ie.noel.dunsceal.views.BaseView
import kotlinx.android.synthetic.main.activity_dun_list.*
import kotlinx.android.synthetic.main.appbar_fab.*

class DunListView : BaseView(), DunListener {

  private lateinit var presenter: DunListPresenter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_dun_list)
    super.init(toolbar, false)

    presenter = initPresenter(DunListPresenter(this)) as DunListPresenter

    val layoutManager = LinearLayoutManager(this)
    recyclerView.layoutManager = layoutManager
    presenter.loadDuns()
  }

  override fun showDuns(duns: ArrayList<DunModel>) {
    recyclerView.adapter = DunAdapter(duns, this, false)
    recyclerView.adapter?.notifyDataSetChanged()
  }

  override fun onDunClick(dun: DunModel) {
    presenter.doEditDun(dun)
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    presenter.loadDuns()
    super.onActivityResult(requestCode, resultCode, data)
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.menu_main, menu)
    return super.onCreateOptionsMenu(menu)
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