package ie.noel.dunsceal.views.home

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import ie.noel.dunsceal.R
import ie.noel.dunsceal.views.home.dunlist.ReportAllFragment
import ie.noel.dunsceal.views.home.dunlist.ReportFragment
import ie.noel.dunsceal.utils.Image.readImageUri
import ie.noel.dunsceal.utils.Image.showImagePicker
import ie.noel.dunsceal.views.BaseFragment
import ie.noel.dunsceal.views.VIEW
import ie.noel.dunsceal.views.about.AboutUsFragment
import ie.noel.dunsceal.views.login.LoginView
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import kotlinx.android.synthetic.main.appbar_fab_home.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.home.*
import kotlinx.android.synthetic.main.nav_header_home.view.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

open class HomeView : LoginView(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var presenter: HomePresenter
    private var userName: String = "User"

    companion object {
        private const val TAG = "HomeView"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home)
        if (toolbar == null) {
            setSupportActionBar(toolbar)
            // TODO supportActionBar?.title = "Explore"
        }

        presenter = initPresenter(HomePresenter(this)) as HomePresenter
        presenter.fetchData()

        fab.setOnClickListener { view ->
            Snackbar.make(
                view, "Replace with your own action",
                Snackbar.LENGTH_LONG
            ).setAction("Action", null).show()
        }

      // Start home fragment
      val fragment = HomeFragment.newInstance(presenter, userName)
      R.id.content_home_frame.addFragment(fragment, TAG)

      // TODO Fix this button
        if (this.home_play_btn != null) {
            this.home_play_btn.setOnClickListener {
                navigateTo(VIEW.LIST)
            }
        }

        navView.setNavigationItemSelectedListener(this)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // customize Navigation Header
        userName = presenter.app.auth.currentUser?.displayName.toString()
        navView.getHeaderView(0).nav_header_email.text = userName

        //Checking if Google User, upload google profile pic
        // TODO presenter.checkExistingPhoto(this)

        navView.getHeaderView(0).imageView
            .setOnClickListener { showImagePicker(this, 1) }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.nav_home ->
                navigateTo(HomeFragment.newInstance(presenter, userName))
            R.id.nav_report ->
                navigateTo(ReportFragment.newInstance(presenter))
            R.id.nav_report_all ->
                navigateTo(ReportAllFragment.newInstance(presenter))
            R.id.nav_aboutus ->
                navigateTo(AboutUsFragment.newInstance())
            R.id.nav_sign_out -> signOut()

            else -> toast("You Selected Something Else")
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_home_item_list -> {
                navigateTo(VIEW.LIST)
            }
        }
        return super.onOptionsItemSelected(item!!)
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START)
        else
            super.onBackPressed()
    }

    private fun navigateTo(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.content_home_frame, fragment)
            .addToBackStack(null)
            .commit()
    }

    // Return to login screen if we sign out
    private fun signOut() {
        presenter.app.googleSignInClient.signOut().addOnCompleteListener(this) {
            presenter.app.auth.signOut()
            startActivity<LoginView>()
            finish()   // finish this activity
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            1 -> {
                if (data != null) {
                    presenter.writeImageRef(readImageUri(resultCode, data).toString())
                    Picasso.get().load(readImageUri(resultCode, data).toString())
                        .resize(180, 180)
                        .transform(CropCircleTransformation())
                        .into(navView.getHeaderView(0).imageView, object : Callback {
                            override fun onSuccess() {
                                // Drawable is ready
                                presenter.uploadImageView(navView.getHeaderView(0).imageView)
                            }
                            override fun onError(e: Exception) {}
                        })
                }
            }
        }
    }

    // Avoid db is not initialized issues
    override fun onResume() {
        super.onResume()
        presenter.fetchData()
    }

    open fun Int.swapFragment(fragment: BaseFragment) {
        presenter.ft = supportFragmentManager.beginTransaction()
        presenter.ft.replace(this, fragment)
        presenter.ft.commit()
    }

    open fun Int.addFragment(fragment: BaseFragment, tag: String) {
        presenter.ft = supportFragmentManager.beginTransaction()
        presenter.ft.add(this, fragment, tag)
        presenter.ft.commit()
    }
}