package ie.noel.dunsceal.views.home

import android.app.Activity
import android.net.Uri
import android.util.Log
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.fragment.app.FragmentTransaction
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import ie.noel.dunsceal.R
import ie.noel.dunsceal.main.MainApp
import ie.noel.dunsceal.models.UserPhoto
import ie.noel.dunsceal.models.entity.DunEntity
import ie.noel.dunsceal.utils.Image.convertImageToBytes
import ie.noel.dunsceal.views.BaseFragment
import ie.noel.dunsceal.views.VIEW
import ie.noel.dunsceal.views.home.dunlist.DunListFragment
import ie.noel.dunsceal.views.login.LoginPresenter
import ie.noel.dunsceal.views.login.LoginView
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import kotlinx.android.synthetic.main.home.*
import kotlinx.android.synthetic.main.nav_header_home.view.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

import java.lang.Exception
import java.util.HashMap

open class HomePresenter(view: LoginView) : LoginPresenter(view) {

    lateinit var ft: FragmentTransaction

    fun validatePhoto(activity: Activity) {

        var imageUri: Uri? = null
        val imageExists = app.userImage.toString().isNotEmpty()
        val googlePhotoExists = app.auth.currentUser?.photoUrl != null

        if (imageExists)
            imageUri = app.userImage
        else
            if (googlePhotoExists)
                imageUri = app.auth.currentUser?.photoUrl!!

        if (googlePhotoExists || imageExists) {
            if (!app.auth.currentUser?.displayName.isNullOrEmpty())
                activity.navView.getHeaderView(0)
                    .nav_header_name.text = app.auth.currentUser?.displayName
            else
                activity.navView.getHeaderView(0)
                    .nav_header_name.text = activity.getText(R.string.nav_header_title)

            Picasso.get().load(imageUri)
                .resize(180, 180)
                .transform(CropCircleTransformation())
                .into(activity.navView.getHeaderView(0).imageView, object : Callback {
                    override fun onSuccess() {
                        // Drawable is ready
                        uploadImageView(activity.navView.getHeaderView(0).imageView)
                    }

                    override fun onError(e: Exception) {}
                })
        } else    // New Regular User, upload default pic of homer
        {
            activity.navView.getHeaderView(0)
                .imageView.setImageResource(R.mipmap.ic_launcher_homer_round)
            uploadImageView(activity.navView.getHeaderView(0).imageView)
        }
    }

    fun checkExistingPhoto(activity: Activity) {

        if (dataStore != null && isUserLoggedIn()) {
            fetchData()
            app.userImage = "".toUri()
            Log.v("Dun", "checkExistingPhoto 1 app.userImage : ${app.userImage}")

            app.db.child("user-photos").orderByChild("uid")
                .equalTo(app.auth.currentUser!!.uid)
                .addListenerForSingleValueEvent(object : ValueEventListener {

                    override fun onDataChange(snapshot: DataSnapshot) {
                        snapshot.children.forEach {
                            val user =
                                it.getValue<UserPhoto>(UserPhoto::class.java)
                            app.userImage = user!!.image.toUri()
                            Log.v(
                                "Dun",
                                "checkExistingPhoto 2 app.userImage : ${app.userImage}"
                            )
                        }
                        Log.v("Dun", "validatePhoto 3 app.userImage : ${app.userImage}")
                        validatePhoto(activity)
                    }

                    override fun onCancelled(databaseError: DatabaseError) {}
                })

        }
    }

    fun uploadImageView(imageView: ImageView) {
        val uid = app.auth.currentUser!!.uid
        val imageRef = app.st.child("photos").child("${uid}.jpg")
        val uploadTask = imageRef.putBytes(convertImageToBytes(imageView))

        uploadTask.addOnFailureListener {
            OnFailureListener { error -> Log.v("Dun", "uploadTask.exception$error") }
        }.addOnSuccessListener {
            uploadTask.continueWithTask {
                imageRef.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    app.userImage = task.result!!.toString().toUri()
                    updateAllDuns(app)
                    writeImageRef(app.userImage.toString())
                    Picasso.get().load(app.userImage)
                        .resize(180, 180)
                        .transform(CropCircleTransformation())
                        .into(imageView)
                }
            }
        }
    }

    fun writeImageRef(imageRef: String) {
        val userId = app.auth.currentUser!!.uid
        val values = UserPhoto(userId, imageRef).toMap()
        val childUpdates = HashMap<String, Any>()

        childUpdates["/user-photos/$userId"] = values
        app.db.updateChildren(childUpdates)
    }

    private fun updateAllDuns(app: MainApp) {
        val userId = app.auth.currentUser!!.uid
        val userEmail = app.auth.currentUser!!.email
        val dunRef = app.db.ref.child("duns")
            .orderByChild("email")
        val userDonationRef = app.db.ref.child("users").child(userId).child("duns")
            .child(userId).orderByChild("uid")

        dunRef.equalTo(userEmail).addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {}
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.children.forEach {
                        it.ref.child("image")
                            .setValue(app.userImage.toString())
                    }
                }
            })

        userDonationRef.addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {}
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.children.forEach {
                        it.ref.child("image")
                            .setValue(app.userImage.toString())
                    }
                }
            })

        writeImageRef(app.userImage.toString())
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

    fun loadDuns() {
        doAsync {
            val duns = app.duns.findAll()
            uiThread {
                view?.getAllDuns(duns as ArrayList<DunEntity>)
            }
        }
    }
}