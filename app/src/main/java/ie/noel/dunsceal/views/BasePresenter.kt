package ie.noel.dunsceal.views

import android.content.Intent
import com.google.firebase.auth.FirebaseAuth
import ie.noel.dunsceal.main.MainApp
import ie.noel.dunsceal.models.entity.DunEntity
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

open class BasePresenter(var view: BaseView?) {

  var app: MainApp = view?.application as MainApp
  var auth: FirebaseAuth? = null

  open fun doActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
  }

  open fun doRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
  }

  open fun onDestroy() {
    view = null
  }

  fun onResume() {
    app.duns.findAll()
  }

  fun isUserLoggedIn() : Boolean {
    auth = FirebaseAuth.getInstance()
    val user= auth!!.currentUser
    if(user != null) {
      return true
    }
    return false
  }

  fun skipSplash() {
    view?.navigateTo(VIEW.LOGIN)
  }

  open fun loadDuns() {
    doAsync {
      val duns = app.duns.findAll()
      uiThread {
        view?.getAllDuns(duns as ArrayList<DunEntity>)
      }
    }
  }

  // LIST MENU
  fun doAdd() {
    view?.navigateTo(VIEW.DUN)
  }

  fun doEdit(dun: DunEntity) {
    view?.navigateTo(VIEW.DUN, 0, "dun_edit", dun)
  }

  fun doShowMap() {
    view?.navigateTo(VIEW.MAPS)
  }

  fun doLogout() {
    app.duns.clear()
    FirebaseAuth.getInstance().signOut()
    app.userId = ""
    view?.navigateTo(VIEW.LOGIN, 0, "logout")
  }
}