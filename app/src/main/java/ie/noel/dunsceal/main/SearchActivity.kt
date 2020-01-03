/*
 * Copyright 2017, The Android Open Source Project
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
package ie.noel.dunsceal.main

import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import ie.noel.dunsceal.R
import ie.noel.dunsceal.models.Dun
import ie.noel.dunsceal.ui.DunFragment
import ie.noel.dunsceal.ui.DunListFragment
import kotlinx.android.synthetic.main.search_activity.view.*


class SearchActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.search_activity)

    // Add dun list fragment if this is first creation
    if (savedInstanceState == null) {
      val fragment = DunListFragment()
      supportFragmentManager.beginTransaction()
          .add(R.id.fragment_container, fragment, DunListFragment.TAG).commit()
    }
  }

  /** Shows the dun detail fragment  */
  fun show(dun: Dun) {
    val dunFragment = DunFragment.forDun(dun.id)
    // fix bug
    // findViewById(android.R.id.content).getRootView()
    supportFragmentManager
        .beginTransaction()
        .addToBackStack("dun")
        .replace(R.id.fragment_container,
            dunFragment, null).commit()
  }
}