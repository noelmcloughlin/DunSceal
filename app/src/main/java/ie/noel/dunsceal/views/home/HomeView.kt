package ie.noel.dunsceal.views.home

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import ie.noel.dunsceal.R
import ie.noel.dunsceal.views.fragment.AboutUsFragment
import ie.noel.dunsceal.views.fragment.HomeFragment
import ie.noel.dunsceal.views.fragment.ReportAllFragment
import ie.noel.dunsceal.views.fragment.ReportFragment
import ie.noel.dunsceal.utils.Image.readImageUri
import ie.noel.dunsceal.utils.Image.showImagePicker
import ie.noel.dunsceal.views.BaseView
import ie.noel.dunsceal.views.login.LoginView
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import kotlinx.android.synthetic.main.appbar_fab.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.home.*
import kotlinx.android.synthetic.main.nav_header_home.view.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

open class HomeView : BaseView(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var presenter: HomePresenter
    private var userName: String = "User"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home)
        setSupportActionBar(toolbar)

        presenter = initPresenter(HomePresenter(this)) as HomePresenter
        presenter.fetchData()

        fab.setOnClickListener { view ->
            Snackbar.make(
                view, "Replace with your own action",
                Snackbar.LENGTH_LONG
            ).setAction("Action", null).show()
        }

        navView.setNavigationItemSelectedListener(this)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        userName = presenter.app.auth.currentUser?.displayName.toString()
        navView.getHeaderView(0).nav_user_displayname.text = userName
        homeTitle.text = "${getString(R.string.homeTitle) + userName}"

        //Checking if Google User, upload google profile pic
        //presenter.checkExistingPhoto(this)

        navView.getHeaderView(0).imageView
            .setOnClickListener { showImagePicker(this, 1) }

        val fragment = HomeFragment.newInstance(presenter, userName)
        presenter.ft = supportFragmentManager.beginTransaction()
        presenter.ft.replace(R.id.homeFrame, fragment)
        presenter.ft.commit()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.nav_donate ->
                navigateTo(HomeFragment.newInstance(presenter, userName))
            R.id.nav_report ->
                navigateTo(ReportFragment.newInstance())
            R.id.nav_report_all ->
                navigateTo(ReportAllFragment.newInstance())
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
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.item_add -> presenter.doAddDun()
            R.id.item_logout ->presenter.doLogout()
            R.id.item_search -> presenter.doSearch()
            R.id.item_map -> presenter.doShowDunsMap()
            R.id.item_navigate -> presenter.doShowDunsRoute()
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
            .replace(R.id.homeFrame, fragment)
            .addToBackStack(null)
            .commit()
    }

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
}