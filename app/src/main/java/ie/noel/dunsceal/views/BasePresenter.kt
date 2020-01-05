package ie.noel.contentceal.views

import android.content.Intent
import com.google.firebase.auth.FirebaseAuth
import ie.noel.contentceal.main.MainApp
import ie.noel.contentceal.models.entity.ContentEntity
import ie.noel.dunsceal.models.entity.ContentEntity
import ie.noel.dunsceal.views.VIEW

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
    app.content.findAll()
  }

  fun doLogout() {
    app.content.clear()
    FirebaseAuth.getInstance().signOut()
    app.userId = ""
    view?.navigateTo(VIEW.LOGIN, 0, "logout")
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

  fun doEditContent(content: ContentEntity) {
    view?.navigateTo(VIEW.SPLASH, 0, "content_edit", content)
  }
}