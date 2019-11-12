package org.noel.dunsceal.activities

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_dun_list.*
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.startActivityForResult
import org.noel.dunsceal.R
import org.noel.dunsceal.adapters.DunAdapter
import org.noel.dunsceal.adapters.DunListener
import org.noel.dunsceal.main.MainApp
import org.noel.dunsceal.models.DunModel

class DunListActivity : AppCompatActivity(), DunListener {

  lateinit var app: MainApp

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_dun_list)
    app = application as MainApp
    toolbar.title = title
    setSupportActionBar(toolbar)

    val layoutManager = LinearLayoutManager(this)
    recyclerView.layoutManager = layoutManager
    val adapter = DunAdapter(this, app.duns.findAll())
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
    recyclerView.adapter = DunAdapter(this, duns)
    recyclerView.adapter?.notifyDataSetChanged()
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.menu_main, menu)
    return super.onCreateOptionsMenu(menu)
  }

  override fun onOptionsItemSelected(item: MenuItem?): Boolean {
    when (item?.itemId) {
      R.id.item_add -> startActivityForResult<DunActivity>(0)
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