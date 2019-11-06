/*
 * Copyright 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.noel.dunsceal.activities.dun2

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.SpinnerAdapter
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.ORIENTATION_HORIZONTAL
import org.noel.dunsceal.duns.Dun
import androidx.viewpager2.widget.ViewPager2.ORIENTATION_VERTICAL
import org.noel.dunsceal.controllers.OrientationController
import org.noel.dunsceal.R

/**
 * Base class for the two activities in the demo. Sets up the list of duns and implements UI to
 * jump to arbitrary duns using setCurrentItem, either with or without smooth scrolling.
 */
abstract class DunBaseActivity : FragmentActivity() {

    protected lateinit var viewPager: ViewPager2
    private lateinit var dunSelector: Spinner
    private lateinit var gotoPage: Button

    private val translateX get() = viewPager.orientation == ORIENTATION_VERTICAL
    private val translateY get() = viewPager.orientation == ORIENTATION_HORIZONTAL

    protected open val layoutId: Int = R.layout.dun2_activity_no_tablayout

    private val mAnimator = ViewPager2.PageTransformer { page, position ->
        val absPos = Math.abs(position)
        page.apply {
            rotation = 0f
            translationY = if (translateY) absPos * 500f else 0f
            translationX = if (translateX) absPos * 350f else 0f
            val scale = if (absPos > 1) 0F else 1 - absPos
            scaleX = scale
            scaleY = scale
        }
    }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutId)

        viewPager = findViewById(R.id.view_pager)
        dunSelector = findViewById(R.id.dun_spinner)
        gotoPage = findViewById(R.id.jump_button)

        OrientationController(
            viewPager,
            findViewById(R.id.orientation_spinner)
        ).setUp()
        dunSelector.adapter = createDunAdapter()
        viewPager.setPageTransformer(mAnimator)

        gotoPage.setOnClickListener {
            val dun = dunSelector.selectedItemPosition
            viewPager.setCurrentItem(dun, true)
        }
    }

    private fun createDunAdapter(): SpinnerAdapter {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, Dun.DECK)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        return adapter
    }
}
