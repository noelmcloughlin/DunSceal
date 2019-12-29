package ie.noel.dunsceal.main

import android.app.Application
import android.net.Uri
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference
import ie.noel.dunsceal.models.DunStore
import ie.noel.dunsceal.models.data.DunFireStore
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class MainApp : Application(), AnkoLogger {

  lateinit var duns: DunStore
  lateinit var auth: FirebaseAuth
  lateinit var googleSignInClient: GoogleSignInClient
  lateinit var userImage: Uri
  lateinit var db: DatabaseReference
  lateinit var st: StorageReference
  lateinit var userId: String

  override fun onCreate() {
    super.onCreate()
    duns = DunFireStore(applicationContext)
    info("Dun started")
  }
}