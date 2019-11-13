package org.noel.dunsceal.activity

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_list.*
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.startActivityForResult
import org.noel.dunsceal.R
import org.noel.dunsceal.adapters.DunRecycleViewAdapter
import org.noel.dunsceal.adapters.DunListener
import org.noel.dunsceal.main.DunScealApp
import org.noel.dunsceal.models.DunModel

class DunListActivity : AppCompatActivity(), DunListener {

  lateinit var app: DunScealApp

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_list)
    app = application as DunScealApp

    toolbar.title = title
    setSupportActionBar(toolbar)

    val layoutManager = LinearLayoutManager(this)
    recyclerView.layoutManager = layoutManager
    val adapter = DunRecycleViewAdapter(this, app.duns.findAll(), this)
    recyclerView.adapter = adapter
    loadDuns()

    fab_recycler.setOnClickListener {
      adapter.showCheckboxState()
    }
  }

  private fun loadDuns() {
    showDuns( app.duns.findAll())
  }

  private fun showDuns (duns: List<DunModel>) {
    recyclerView.adapter = DunRecycleViewAdapter(this, duns, this)
    recyclerView.adapter?.notifyDataSetChanged()
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.menu_dun_add, menu)
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

  override fun onDunClick(dun: DunModel) {
    startActivityForResult(intentFor<DunActivity>().putExtra("dun_edit", dun), 0)
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    loadDuns()
    super.onActivityResult(requestCode, resultCode, data)
  }
}