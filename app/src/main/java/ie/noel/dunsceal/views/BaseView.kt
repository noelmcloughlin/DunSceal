package ie.noel.dunsceal.views

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import ie.noel.dunsceal.models.entity.DunEntity
import ie.noel.dunsceal.models.entity.LocationEntity
import org.jetbrains.anko.AnkoLogger
import ie.noel.dunsceal.utils.Loader
import ie.noel.dunsceal.views.home.dunlist.DunListView
import ie.noel.dunsceal.views.home.location.EditLocationView
import ie.noel.dunsceal.views.home.HomeView
import ie.noel.dunsceal.views.home.dun.DunView
import ie.noel.dunsceal.views.login.LoginView
import ie.noel.dunsceal.views.home.map.DunMapView
import ie.noel.dunsceal.views.login.SplashView

const val IMAGE_REQUEST = 1
const val LOCATION_REQUEST = 2

enum class VIEW {
  DUN, HOME, LIST, LOCATION, LOGIN, MAPS, SPLASH
}

open abstract class BaseView : AppCompatActivity(), AnkoLogger {

  // declare a presenter
  private var basePresenter: BasePresenter? = null
  lateinit var loader : AlertDialog

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    loader = Loader.createLoader(this)
  }

  fun navigateTo(view: VIEW, code: Int = 0, key: String = "", value: Parcelable? = null) {
    val intent = when (view) {
      VIEW.LOCATION -> Intent(this, EditLocationView::class.java)
      VIEW.HOME -> Intent(this, HomeView::class.java)
      VIEW.DUN -> Intent(this, DunView::class.java)
      VIEW.MAPS -> Intent(this, DunMapView::class.java)
      VIEW.LIST -> Intent(this, DunListView::class.java)
      VIEW.LOGIN -> Intent(this, LoginView::class.java)
      VIEW.SPLASH -> Intent(this, SplashView::class.java)
    }
    if (key != "") {
      intent.putExtra(key, value)
    }
    startActivityForResult(intent, code)
  }

  fun initPresenter(presenter: BasePresenter): BasePresenter {
    basePresenter = presenter
    return presenter
  }

  fun init(toolbar: Toolbar, upEnabled: Boolean, title: String) {
    toolbar.title = title
    setSupportActionBar(toolbar)
    val user = FirebaseAuth.getInstance().currentUser
    if (user != null) {
      toolbar.title = "${title}: ${user.email}"
    }
    supportActionBar?.setDisplayHomeAsUpEnabled(upEnabled)
  }

  override fun onDestroy() {
    basePresenter?.onDestroy()
    super.onDestroy()
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    if (data != null) {
      basePresenter?.doActivityResult(requestCode, resultCode, data)
    }
  }

  override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
    basePresenter?.doRequestPermissionsResult(requestCode, permissions, grantResults)
  }

  open fun showDun(dun: DunEntity) {}
  open fun getAllDuns(duns: ArrayList<DunEntity>) {}
  open fun showLocation(locationEntity : LocationEntity) {}
  open fun showProgress() {}
  open fun hideProgress() {}
}