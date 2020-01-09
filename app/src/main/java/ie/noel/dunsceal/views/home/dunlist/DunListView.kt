package ie.noel.dunsceal.views.home.dunlist

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import ie.noel.dunsceal.R
import ie.noel.dunsceal.models.entity.DunEntity
import ie.noel.dunsceal.views.BaseFragment
import ie.noel.dunsceal.views.BaseView
import ie.noel.dunsceal.views.home.dun.DunListener
import kotlinx.android.synthetic.main.appbar_fab_home.*
import org.jetbrains.anko.AnkoLogger

open class DunListView : BaseView(), AnkoLogger, DunListener {

  open lateinit var presenter: DunListPresenter

  companion object {
    private const val TAG = "DunListView"
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.home)
    super.init(toolbar, false, TAG)

    presenter = initPresenter(DunListPresenter(this)) as DunListPresenter

    // Add product list fragment if this is first creation
    if (savedInstanceState == null) {
      val fragment : BaseFragment = DunListFragment.newInstance(presenter)
      supportFragmentManager.beginTransaction()
          .replace(R.id.content_home_frame, fragment, TAG).commit()
    }
    presenter.loadDuns()
  }

  /* override fun showDuns(duns: List<DunEntity>) {
  recyclerView.adapter = DunAdapter(duns, this)
  recyclerView.adapter?.notifyDataSetChanged()
} */

  override fun onDunClick(dun: DunEntity) {
    presenter.doEdit(dun)
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    presenter.loadDuns()
    super.onActivityResult(requestCode, resultCode, data)
  }
}