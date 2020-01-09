package ie.noel.dunsceal.views.login

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import ie.noel.dunsceal.models.storage.DunFireStoreEntity
import ie.noel.dunsceal.views.BasePresenter
import org.jetbrains.anko.toast
import ie.noel.dunsceal.views.BaseView
import ie.noel.dunsceal.views.VIEW

open class LoginPresenter(view: BaseView) : BasePresenter(view) {

    var dataStore: DunFireStoreEntity? = null

    init {
        if (app.duns is DunFireStoreEntity) {
            dataStore = app.duns as DunFireStoreEntity
        }
    }

    fun doSignUp(email: String, password: String) {
        view?.showProgress()
        app.auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(view!!) { task ->
            if (task.isSuccessful) {
                if (dataStore != null) {
                    dataStore!!.fetchDuns {
                        view?.hideProgress()
                        view?.navigateTo(VIEW.HOME)
                    }
                } else {
                    view?.hideProgress()
                    view?.navigateTo(VIEW.HOME)
                }
            } else {
                view?.hideProgress()
                view?.toast("Sign Up Failed: ${task.exception?.message}")
            }
        }
    }

    fun doLogin(email: String, password: String) {
        view?.showProgress()
        app.auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(view!!) { task ->
            if (task.isSuccessful) {
                if (dataStore != null) {
                    dataStore!!.fetchDuns {
                        view?.hideProgress()
                        view?.navigateTo(VIEW.HOME)
                    }
                } else {
                    view?.hideProgress()
                    view?.navigateTo(VIEW.HOME)
                }
            } else {
                view?.hideProgress()
                view?.toast("Sign In Failed: ${task.exception?.message}")
            }
        }
    }

    fun fetchData() {
        if (dataStore != null && isUserLoggedIn()) {
            dataStore!!.fetchDuns {
                app.db = FirebaseDatabase.getInstance().reference
                app.userId = FirebaseAuth.getInstance().currentUser!!.uid
                app.st = FirebaseStorage.getInstance().reference
                app.db = FirebaseDatabase.getInstance().reference
            }
        }
    }
}