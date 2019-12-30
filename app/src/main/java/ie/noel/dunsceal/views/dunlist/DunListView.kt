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
    myRecyclerView.layoutManager = layoutManager
    presenter.loadDuns()

    /* searchView.setOnClickListener {view ->
    //startActivity(Intent(this, SearchViewActivity::class.java))
    Snackbar.make(
        view, "Replace with your own search action",
        Snackbar.LENGTH_LONG
    ).setAction("Action", null).show()
} */
  }

  override fun getAllDuns(duns: ArrayList<DunModel>) {
    myRecyclerView.adapter = DunAdapter(duns, this, false)
    myRecyclerView.adapter?.notifyDataSetChanged()
  }

  override fun onDunClick(dun: DunModel) {
    presenter.doEditDun(dun)
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    presenter.loadDuns()
    super.onActivityResult(requestCode, resultCode, data)
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.menu_home, menu)
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