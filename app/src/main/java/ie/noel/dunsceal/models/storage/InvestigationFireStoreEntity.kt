package ie.noel.dunsceal.models.storage

import android.content.Context
import android.graphics.Bitmap
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import ie.noel.dunsceal.models.entity.InvestigationEntity
import ie.noel.dunsceal.models.entity.InvestigationStoreEntity
import ie.noel.dunsceal.utils.Image.readImageFromPath
import org.jetbrains.anko.AnkoLogger
import java.io.ByteArrayOutputStream
import java.io.File

class InvestigationFireStoreEntity(val context: Context) : InvestigationStoreEntity, AnkoLogger {

  val locations = ArrayList<InvestigationEntity>()
  lateinit var userId: String
  lateinit var db: DatabaseReference
  private lateinit var st: StorageReference

  override fun findAll(): List<InvestigationEntity> {
    return locations
  }

  override fun findById(id: Long): InvestigationEntity? {
    return locations.find { p -> p.id == id }
  }

  override fun create(location: InvestigationEntity) {
    val key = db.child("users").child(userId).child("locations").push().key
    key?.let {
      location.fbId = key
      locations.add(location)
      db.child("users").child(userId).child("locations").child(key).setValue(location)
      updateImage(location)
    }
  }

  override fun update(location: InvestigationEntity) {
    val foundInvestigation: InvestigationEntity? = locations.find { p -> p.fbId == location.fbId }
    if (foundInvestigation != null) {
      foundInvestigation.image = location.image
      foundInvestigation.text = location.text
      foundInvestigation.postedAt = location.postedAt
    }

    db.child("users").child(userId).child("locations").child(location.fbId).setValue(location)
    if ((location.image.length) > 0 && (location.image[0] != 'h')) {
      updateImage(location)
    }
  }

  override fun delete(location: InvestigationEntity) {
    db.child("users").child(userId).child("locations").child(location.fbId).removeValue()
    locations.remove(location)
  }

  override fun clear() {
    locations.clear()
  }

  private fun updateImage(location: InvestigationEntity) {
    if (location.image != "") {
      val fileName = File(location.image)
      val imageName = fileName.name

      val imageRef = st.child("$userId/$imageName")
      val baos = ByteArrayOutputStream()
      val bitmap = readImageFromPath(context, location.image)

      bitmap?.let {
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()
        val uploadTask = imageRef.putBytes(data)
        uploadTask.addOnFailureListener {
          println(it.message)
        }.addOnSuccessListener { taskSnapshot ->
          taskSnapshot.metadata!!.reference!!.downloadUrl.addOnSuccessListener {
            location.image = it.toString()
            db.child("users").child(userId).child("locations").child(location.fbId).setValue(location)
          }
        }
      }
    }
  }

  fun fetchInvestigations(locationsReady: () -> Unit) {
    val valueEventListener = object : ValueEventListener {
      override fun onCancelled(dataSnapshot: DatabaseError) {
      }
      override fun onDataChange(dataSnapshot: DataSnapshot) {
        dataSnapshot.children.mapNotNullTo(locations) { it.getValue<InvestigationEntity>(InvestigationEntity::class.java) }
        locationsReady()
      }
    }
    userId = FirebaseAuth.getInstance().currentUser!!.uid
    db = FirebaseDatabase.getInstance().reference
    st = FirebaseStorage.getInstance().reference
    locations.clear()
    db.child("users").child(userId).child("locations").addListenerForSingleValueEvent(valueEventListener)
  }
}