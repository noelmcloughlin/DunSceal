/*
 * Copyright 2018, The Android Open Source Project
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

package ie.noel.dunsceal.views.login

import android.content.Intent
import ie.noel.dunsceal.R
import android.os.Bundle
import android.os.Handler
import android.view.*
import ie.noel.dunsceal.views.BaseView
import kotlinx.android.synthetic.main.fragment_splash_screen.*

/**
 * An startup activity can inflate layout with NavHostFragment.
 * But I reverted to View/Presenter approach for splash
 */
class SplashView : BaseView(), View.OnClickListener {

  private lateinit var presenter: LoginPresenter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    makeFullScreen()
    setContentView(R.layout.fragment_splash_screen)

    presenter = initPresenter(LoginPresenter(this)) as LoginPresenter

    register_login_play_btn.setOnClickListener {
      presenter.skipSplash()
    }

    // Using a handler to delay loading the MainActivity
    Handler().postDelayed({
      // Start activity
      startActivity(Intent(this, LoginView::class.java))
      // Animate the loading of new activity
      overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
      // Close this activity
      finish()
    }, 5000)
  }

  override fun onClick(v: View) {
    presenter.skipSplash()
  }
  private fun makeFullScreen() {
    // Remove Title
    requestWindowFeature(Window.FEATURE_NO_TITLE)
    // Make Fullscreen
    window.setFlags(
        WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN
    )
    // Hide the toolbar
    supportActionBar?.hide()
  }

  /* fun onCreateView(
      inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
  ): View? {
    val view = inflater.inflate(R.layout.fragment_splash_screen, container, false)

    view.findViewById<Button>(R.id.play_btn).setOnClickListener {
      //Navigation.findNavController(view).navigate(R.id.action_nav_splash_screen_to_login)
      startActivity(Intent(this, LoginView::class.java))
    }
    return view
  } */
}