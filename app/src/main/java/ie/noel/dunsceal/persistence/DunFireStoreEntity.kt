package ie.noel.dunsceal.persistence

import android.content.Context
import android.graphics.Bitmap
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import ie.noel.dunsceal.models.entity.DunEntity
import ie.noel.dunsceal.models.entity.DunStoreEntity
import ie.noel.dunsceal.utils.Image.readImageFromPath
import org.jetbrains.anko.AnkoLogger
import java.io.ByteArrayOutputStream
import java.io.File

class DunFireStoreEntity(val context: Context) : DunStoreEntity, AnkoLogger {

  val duns = ArrayList<DunEntity>()
  private lateinit var userId: String
  lateinit var db: DatabaseReference
  private lateinit var st: StorageReference

  override fun findAll(): List<DunEntity> {
    return duns
  }

  override fun findById(id: Long): DunEntity? {
    return duns.find { p -> p.id == id }
  }

  override fun create(dun: DunEntity) {
    val key = db.child("users").child(userId).child("duns").push().key
    key?.let {
      dun.fbId = key
      duns.add(dun)
      db.child("users").child(userId).child("duns").child(key).setValue(dun)
      updateImage(dun)
    }
  }

  override fun update(dun: DunEntity) {
    val foundDun: DunEntity? = duns.find { p -> p.fbId == dun.fbId }
    if (foundDun != null) {
      foundDun.name = dun.name
      foundDun.description = dun.description
      foundDun.image = dun.image
      foundDun.location = dun.location
    }

    db.child("users").child(userId).child("duns").child(dun.fbId).setValue(dun)
    if ((dun.image.length) > 0 && (dun.image[0] != 'h')) {
      updateImage(dun)
    }
  }

  override fun delete(dun: DunEntity) {
    db.child("users").child(userId).child("duns").child(dun.fbId).removeValue()
    duns.remove(dun)
  }

  override fun clear() {
    duns.clear()
  }

  private fun updateImage(dun: DunEntity) {
    if (dun.image != "") {
      val fileName = File(dun.image)
      val imageName = fileName.name

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
        dataSnapshot.children.mapNotNullTo(duns) { it.getValue<DunEntity>(DunEntity::class.java) }
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