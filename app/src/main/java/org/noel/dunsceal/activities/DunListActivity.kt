package org.noel.dunsceal.activities

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.dun_list_activity.*
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.startActivityForResult
import org.noel.dunsceal.R
import org.noel.dunsceal.adapters.DunAdapter
import org.noel.dunsceal.adapters.DunListener
import org.noel.dunsceal.main.DunScealApp
import org.noel.dunsceal.model.DunModel

class DunListActivity : AppCompatActivity(), DunListener {

    lateinit var app: DunScealApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dun_list_activity)
        app = application as DunScealApp
        toolbar.title = title
        setSupportActionBar(toolbar)

        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = DunAdapter(app.dunStore.findAll(), this)
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
        recyclerView.adapter?.notifyDataSetChanged()
        super.onActivityResult(requestCode, resultCode, data)
    }
}