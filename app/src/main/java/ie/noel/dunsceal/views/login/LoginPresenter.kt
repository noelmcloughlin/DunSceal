package ie.noel.dunsceal.views.login

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import ie.noel.dunsceal.models.storage.DunFireStoreEntity
import ie.noel.dunsceal.models.storage.InvestigationFireStoreEntity
import ie.noel.dunsceal.views.BasePresenter
import org.jetbrains.anko.toast
import ie.noel.dunsceal.views.BaseView
import ie.noel.dunsceal.views.VIEW

open class LoginPresenter(view: BaseView) : BasePresenter(view) {

    var dunDataStore: DunFireStoreEntity? = null
    var investigationDataStore: InvestigationFireStoreEntity? = null


    init {
        if (app.duns is DunFireStoreEntity) {
            dunDataStore = app.duns as DunFireStoreEntity
        }
        if (app.investigations is InvestigationFireStoreEntity) {
            investigationDataStore = app.investigations as InvestigationFireStoreEntity
        }
    }

    fun doSignUp(email: String, password: String) {
        view?.showProgress()
        app.auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(view!!) { task ->
            if (task.isSuccessful) {
                if (dunDataStore != null) {
                    dunDataStore!!.fetchDuns {
                        if (investigationDataStore != null) {
                            investigationDataStore!!.fetchInvestigations {
                                view?.hideProgress()
                                view?.navigateTo(VIEW.HOME) }
                        }
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
                if (dunDataStore != null) {
                    dunDataStore!!.fetchDuns {
                        if (investigationDataStore != null) {
                            investigationDataStore!!.fetchInvestigations {
                                view?.hideProgress()
                                view?.navigateTo(VIEW.HOME) }
                        }
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
        if (dunDataStore != null && isUserLoggedIn()) {
            dunDataStore!!.fetchDuns {
                app.db = FirebaseDatabase.getInstance().reference
                app.userId = FirebaseAuth.getInstance().currentUser!!.uid
                app.st = FirebaseStorage.getInstance().reference
                app.db = FirebaseDatabase.getInstance().reference
            }
        }
        if (investigationDataStore != null && isUserLoggedIn()) {
            investigationDataStore!!.fetchInvestigations {  }
        }
    }
}