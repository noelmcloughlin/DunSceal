package ie.noel.dunsceal.views.home

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import ie.noel.dunsceal.R
import ie.noel.dunsceal.models.entity.DunEntity
import ie.noel.dunsceal.views.dunlist.DunListAllFragment
import ie.noel.dunsceal.utils.Image.readImageUri
import ie.noel.dunsceal.utils.Image.showImagePicker
import ie.noel.dunsceal.views.BaseView
import ie.noel.dunsceal.views.VIEW
import ie.noel.dunsceal.views.dun.DunPresenter
import ie.noel.dunsceal.views.dun.DunFragment
import ie.noel.dunsceal.views.dunlist.DunListFragment
import ie.noel.dunsceal.views.dunlist.DunListPresenter
import ie.noel.dunsceal.views.location.LocationFragment
import ie.noel.dunsceal.views.location.LocationPresenter
import ie.noel.dunsceal.views.login.LoginView
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import kotlinx.android.synthetic.main.appbar_fab_home.*
import kotlinx.android.synthetic.main.nav_drawer_home.*
import kotlinx.android.synthetic.main.nav_header_home.view.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

open class HomeView : BaseView(), NavigationView.OnNavigationItemSelectedListener {

  private lateinit var presenter: HomePresenter
  private var userName: String = "User"

  companion object {
    const val TAG = "HomeView"
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.nav_drawer_home)
    super.init(toolbar, false, TAG)

    presenter = initPresenter(DunListPresenter(this)) as DunListPresenter
    presenter.fetchData()

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
    presenter.fetchData()
    presenter.checkExistingPhoto(this)
    navView.getHeaderView(0).imageView
        .setOnClickListener { showImagePicker(this, 1) }

    // add nav_drawer_home fragment if this is the first creation
    if (savedInstanceState == null) {
      val fragment = HomeFragment.newInstance(presenter, userName)
      fragManager.beginTransaction()
          .add(R.id.content_home_frame, fragment, HomeFragment.TAG).commit()
    }
  }

  override fun onNavigationItemSelected(item: MenuItem): Boolean {

    when (item.itemId) {
      R.id.nav_home -> {
        presenter.dunDataStore!!.fetchDuns {
            navigateTo(VIEW.HOME)
        }
      }
      R.id.nav_report -> {
        presenter.dunDataStore!!.fetchDuns {
            fragmentTo(DunListFragment.newInstance(presenter))
        }
      }
      R.id.nav_report_all -> {
        toast("You Selected report all")
        fragmentTo(DunListAllFragment.newInstance(presenter))
      }
      R.id.nav_aboutus -> {
        toast("You Selected about us")
        fragmentTo(AboutFragment.newInstance())
      }
      R.id.nav_sign_out -> {
        toast("You Selected Sign out")
        signOut()
      }

      else -> toast("You Selected Something Else")
    }
    drawerLayout.closeDrawer(GravityCompat.START)
    return true
  }

  override fun onBackPressed() {
    if (drawerLayout.isDrawerOpen(GravityCompat.START))
      drawerLayout.closeDrawer(GravityCompat.START)
    else
      super.onBackPressed()
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
    if (toolbar == null) {
      setSupportActionBar(toolbar)
    }
    presenter.fetchData()
  }

  open fun fragmentTo(fragment: Fragment) {
    fragManager.beginTransaction()
        .replace(R.id.content_home_frame, fragment)
        .addToBackStack(null)
        .commit()
  }

  /** Shows dun detail fragment  */
  fun showDunFragment(dun: DunEntity) {
    val dunFragment = DunFragment.forDun(DunPresenter(this), dun)
    fragManager
        .beginTransaction()
        .addToBackStack("dun")
        .replace(R.id.content_home_frame,
            dunFragment, null).commit()
  }

  // Options Menu
  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.menu_main, menu)
    return super.onCreateOptionsMenu(menu)
  }

  override fun onOptionsItemSelected(item: MenuItem?): Boolean {
    val dun = DunEntity()
    when (item?.itemId) {
      R.id.menu_main_item_add ->
      {
        fragmentTo(DunFragment.newInstance(DunPresenter(this), dun, ""))
      }
      R.id.menu_main_item_map ->
      {
        fragmentTo(LocationFragment.newInstance(LocationPresenter(this)))
      }
      R.id.menu_main_item_logout ->
      {
        presenter.doLogout()
      }
    }
    return super.onOptionsItemSelected(item!!)
  }
}