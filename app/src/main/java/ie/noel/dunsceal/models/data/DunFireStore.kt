package ie.noel.dunsceal.models.data

import android.content.Context
import android.graphics.Bitmap
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import org.jetbrains.anko.AnkoLogger
import ie.noel.dunsceal.models.DunModel
import ie.noel.dunsceal.models.DunStore
import ie.noel.dunsceal.utils.Image.readImageFromPath
import java.io.ByteArrayOutputStream
import java.io.File

class DunFireStore(val context: Context) : DunStore, AnkoLogger {

    val duns = ArrayList<DunModel>()
    private lateinit var userId: String
    lateinit var db: DatabaseReference
    lateinit var st: StorageReference

    override fun findAll(): List<DunModel> {
        return duns
    }

    override fun findById(id: Long): DunModel? {
        return duns.find { p -> p.id == id }
    }

    override fun create(dun: DunModel) {
        val key = db.child("users").child(userId).child("duns").push().key
        key?.let {
            dun.fbId = key
            duns.add(dun)
            db.child("users").child(userId).child("duns").child(key).setValue(dun)
            updateImage(dun)
        }
    }

    override fun update(dun: DunModel) {
        val foundDun: DunModel? = duns.find { p -> p.fbId == dun.fbId }
        if (foundDun != null) {
            foundDun.title = dun.title
            foundDun.description = dun.description
            foundDun.image = dun.image
            foundDun.location = dun.location
        }

        db.child("users").child(userId).child("duns").child(dun.fbId).setValue(dun)
        if ((dun.image.length) > 0 && (dun.image[0] != 'h')) {
            updateImage(dun)
        }
    }

    override fun delete(dun: DunModel) {
        db.child("users").child(userId).child("duns").child(dun.fbId).removeValue()
        duns.remove(dun)
    }

    override fun clear() {
        duns.clear()
    }

    private fun updateImage(dun: DunModel) {
        if (dun.image != "") {
            val fileName = File(dun.image)
            val imageName = fileName.getName()

            val imageRef = st.child("$userId/$imageName")
            val baos = ByteArrayOutputStream()
            val bitmap = readImageFromPath(context, dun.image)

            bitmap?.let {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                val data = baos.toByteArray()
                val uploadTask = imageRef.putBytes(data)
                uploadTask.addOnFailureListener {
                    println(it.message)
                }.addOnSuccessListener { taskSnapshot ->
                    taskSnapshot.metadata!!.reference!!.downloadUrl.addOnSuccessListener {
                        dun.image = it.toString()
                        db.child("users").child(userId).child("duns").child(dun.fbId).setValue(dun)
                    }
                }
            }
        }
    }

    fun fetchDuns(dunsReady: () -> Unit) {
        val valueEventListener = object : ValueEventListener {
            override fun onCancelled(dataSnapshot: DatabaseError) {
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataSnapshot!!.children.mapNotNullTo(duns) { it.getValue<DunModel>(DunModel::class.java) }
                dunsReady()
            }
        }
        userId = FirebaseAuth.getInstance().currentUser!!.uid
        db = FirebaseDatabase.getInstance().reference
        st = FirebaseStorage.getInstance().reference
        db.child("users").child(userId).child("duns")
            .addListenerForSingleValueEvent(valueEventListener)
        duns.clear()
    }
}