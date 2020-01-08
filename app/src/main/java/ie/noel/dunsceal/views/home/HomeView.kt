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
import ie.noel.dunsceal.views.home.dunlist.DunListAllFragment
import ie.noel.dunsceal.utils.Image.readImageUri
import ie.noel.dunsceal.utils.Image.showImagePicker
import ie.noel.dunsceal.views.BaseFragment
import ie.noel.dunsceal.views.BaseView
import ie.noel.dunsceal.views.about.AboutUsFragment
import ie.noel.dunsceal.views.home.dun.DunFragment
import ie.noel.dunsceal.views.home.dunlist.DunListFragment
import ie.noel.dunsceal.views.home.dunlist.DunListPresenter
import ie.noel.dunsceal.views.login.LoginView
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import kotlinx.android.synthetic.main.appbar_fab_home.*
import kotlinx.android.synthetic.main.home.*
import kotlinx.android.synthetic.main.nav_header_home.view.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

open class HomeView : BaseView(), NavigationView.OnNavigationItemSelectedListener {

  private lateinit var presenter: HomePresenter
  private var userName: String = "User"

  companion object {
    private const val TAG = "HomeView"
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.home)
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
    // TODO presenter.checkExistingPhoto(this)
    navView.getHeaderView(0).imageView
        .setOnClickListener { showImagePicker(this, 1) }

    // add home fragment if this is the first creation
    if (savedInstanceState == null) {
      val fragment = HomeFragment.newInstance(presenter, userName)
      supportFragmentManager.beginTransaction()
          .add(R.id.content_home_frame, fragment, TAG).commit()
    }

    /* TODO fab or no fab?
  fab.setOnClickListener { view ->
    Snackbar.make(
        view, "Replace with your own action",
        Snackbar.LENGTH_LONG
    ).setAction("Action", null).show()
  } */
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.menu_main, menu)
    return super.onCreateOptionsMenu(menu)
  }

  override fun onNavigationItemSelected(item: MenuItem): Boolean {

    when (item.itemId) {
      R.id.nav_home -> {
        presenter.dataStore!!.fetchDuns {
          startActivity<HomeView>()
        }
      }
      R.id.nav_report -> {
        presenter.dataStore!!.fetchDuns {
          navigateTo(DunListFragment.newInstance(presenter))
        }
      }
      R.id.nav_report_all -> {
        toast("You Selected report all")
        navigateTo(DunListAllFragment.newInstance(presenter))
      }
      R.id.nav_aboutus -> {
        toast("You Selected about us")
        navigateTo(AboutUsFragment.newInstance())
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
    if (toolbar == null) {
      setSupportActionBar(toolbar)
    }
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

  /** Shows dun detail fragment  */
  fun showDunFragment(dun: DunEntity) {
    val dunFragment = DunFragment.forDun(presenter, dun.id)
    supportFragmentManager
        .beginTransaction()
        .addToBackStack("dun")
        .replace(R.id.content_home_frame,
            dunFragment, null).commit()
  }

  fun OnPlayBtnSelected() {
    supportFragmentManager.beginTransaction()
        .replace(R.id.content_home_frame, DunListFragment.newInstance(presenter), TAG).commit()
  }

  // OPTIONS MENU
  override fun onOptionsItemSelected(item: MenuItem?): Boolean {
    when (item?.itemId) {
      R.id.menu_main_item_add -> presenter.doAdd()
      R.id.menu_main_item_map -> presenter.doShowMap()
      R.id.menu_main_item_logout -> presenter.doLogout()
    }
    return super.onOptionsItemSelected(item!!)
  }
}