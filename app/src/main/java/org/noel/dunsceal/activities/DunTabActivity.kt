/*
 * Copyright (c) 2019 Razeware LLC
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  Notwithstanding the foregoing, you may not use, copy, modify, merge, publish,
 *  distribute, sublicense, create a derivative work, and/or sell copies of the
 *  Software in any work that is designed, intended, or marketed for pedagogical or
 *  instructional purposes related to programming, coding, application development,
 *  or information technology.  Permission for such use, copying, modification,
 *  merger, publication, distribution, sublicensing, creation of derivative works,
 *  or sale is expressly withheld.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 */

package org.noel.dunsceal.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.MODE_SCROLLABLE
import kotlinx.android.synthetic.main.activity_tab.*
import kotlinx.android.synthetic.main.activity_tab.view.*
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.startActivityForResult
import org.noel.dunsceal.R
import org.noel.dunsceal.adapters.DunFragmentPagerAdapter
import org.noel.dunsceal.adapters.DunListener
import org.noel.dunsceal.adapters.DunRecycleViewAdapter
import org.noel.dunsceal.main.MainApp
import org.noel.dunsceal.models.DunModel

class DunTabActivity : AppCompatActivity(), DunListener {

    lateinit var app: MainApp
    private var adapter: DunFragmentPagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tab)
        app = application as MainApp

        adapter = DunFragmentPagerAdapter(this, app.duns.findAll(), supportFragmentManager)
        val viewPager: ViewPager = findViewById(R.id.view_pager)
        viewPager.adapter = adapter
        loadDuns()

        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.tabMode = MODE_SCROLLABLE
        tabs.setupWithViewPager(view_pager)

        view_pager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs))
        tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                view_pager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })
    }

    private fun loadDuns() {
        showDuns( app.duns.findAll())
    }

    private fun showDuns (duns: List<DunModel>) {
        view_pager.adapter = DunFragmentPagerAdapter(this, duns, supportFragmentManager)
        view_pager.adapter?.notifyDataSetChanged()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.item_add -> startActivityForResult<DunTabActivity>(0)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDunClick(dun: DunModel) {
        startActivityForResult(intentFor<DunTabActivity>().putExtra("dun_edit", dun), 0)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        loadDuns()
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun dummyDunData(): List<DunModel> {
        val tabs = arrayListOf<DunModel>()
        tabs.add(
            DunModel(
                1,
                getString(R.string.tab1_desc),
                getString(R.string.tab1_title),
                false,
                "None",
                getString(R.string.tab1_url),
                0.0,
                0.0,
                0f
            )
        )
        tabs.add(
            DunModel(
                2,
                getString(R.string.tab2_title),
                getString(R.string.tab2_desc),
                false,
                "None",
                getString(R.string.tab2_url),
                0.0,
                0.0,
                0f
            )
        )
        tabs.add(
            DunModel(
                3,
                getString(R.string.tab3_title),
                getString(R.string.tab3_desc),
                false,
                "None",
                getString(R.string.tab3_url),
                0.0,
                0.0,
                0f
            )
        )
        return tabs
    }
}