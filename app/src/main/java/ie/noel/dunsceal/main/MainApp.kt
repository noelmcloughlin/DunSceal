package ie.noel.dunsceal.main

import android.app.Application
import android.net.Uri
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference
import ie.noel.dunsceal.persistence.AppExecutors
import ie.noel.dunsceal.persistence.DataRepository
import ie.noel.dunsceal.persistence.DunFireStore
import ie.noel.dunsceal.persistence.db.MockDatabase
import ie.noel.dunsceal.models.entity.DunStore
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

/**
 * Android Application class. Used for accessing singletons.
 */

class MainApp : Application(), AnkoLogger {

    private var mAppExecutors: AppExecutors? = null

    lateinit var duns: DunStore
    lateinit var auth: FirebaseAuth
    lateinit var googleSignInClient: GoogleSignInClient
    lateinit var userImage: Uri
    lateinit var db: DatabaseReference
    lateinit var st: StorageReference
    lateinit var userId: String

    override fun onCreate() {
        super.onCreate()
        mAppExecutors = AppExecutors()
        duns = DunFireStore(applicationContext)
        info("Dun started")
    }

    private val databaseDun: MockDatabase
        get() = MockDatabase.getInstance(this, mAppExecutors!!)!!

    val repository: DataRepository
        get() = DataRepository.getInstance(databaseDun)!!
}