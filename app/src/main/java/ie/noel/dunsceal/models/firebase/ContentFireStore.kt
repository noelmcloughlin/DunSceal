package ie.noel.dunsceal.models.firebase

import android.content.Context
import android.graphics.Bitmap
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import ie.noel.dunsceal.models.Content
import ie.noel.dunsceal.models.ContentStore
import ie.noel.dunsceal.utils.Image.readImageFromPath
import org.jetbrains.anko.AnkoLogger
import java.io.ByteArrayOutputStream
import java.io.File

class ContentFireStore(val context: Context) : ContentStore, AnkoLogger {

  val content = ArrayList<Content>()
  lateinit var userId: String
  lateinit var db: DatabaseReference
  private lateinit var st: StorageReference

  override fun findAll(): List<Content> {
    return content
  }

  override fun findById(id: Int): Content? {
    return content.find { p -> p.id == id }
  }

  override fun create(content: Content) {
    val key = db.child("users").child(userId).child("content").push().key
    key?.let {
      content.fbId = key
      content.add(content)
      db.child("users").child(userId).child("content").child(key).setValue(content)
      updateImage(content)
    }
  }

  override fun update(content: Content) {
    val foundContent: Content? = content.find { p -> p.fbId == content.fbId }
    if (foundContent != null) {
      foundContent.name = content.name
      foundContent.description = content.description
      foundContent.image = content.image
      foundContent.location = content.location
    }

    db.child("users").child(userId).child("content").child(content.fbId!!).setValue(content)
    if ((content.image!!.length) > 0 && (content.image!![0] != 'h')) {
      updateImage(content)
    }
  }

  override fun delete(content: Content) {
    db.child("users").child(userId).child("content").child(content.fbId!!).removeValue()
    content.remove(content)
  }

  override fun clear() {
    content.clear()
  }

  private fun updateImage(content: Content) {
    if (content.image != "") {
      val fileName = File(content.image)
      val imageName = fileName.name

      val imageRef = st.child("$userId/$imageName")
      val baos = ByteArrayOutputStream()
      val bitmap = readImageFromPath(context, content.image!!)

      bitmap?.let {
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()
        val uploadTask = imageRef.putBytes(data)
        uploadTask.addOnFailureListener {
          println(it.message)
        }.addOnSuccessListener { taskSnapshot ->
          taskSnapshot.metadata!!.reference!!.downloadUrl.addOnSuccessListener {
            content.image = it.toString()
            db.child("users").child(userId).child("content").child(content.fbId!!).setValue(content)
          }
        }
      }
    }
  }

  fun fetchContent(contentReady: () -> Unit) {
    val valueEventListener = object : ValueEventListener {
      override fun onCancelled(dataSnapshot: DatabaseError) {
      }
      override fun onDataChange(dataSnapshot: DataSnapshot) {
        dataSnapshot.children.mapNotNullTo(content) { it.getValue<Content>(Content::class.java) }
        contentReady()
      }
    }
    userId = FirebaseAuth.getInstance().currentUser!!.uid
    db = FirebaseDatabase.getInstance().reference
    st = FirebaseStorage.getInstance().reference
    content.clear()
    db.child("users").child(userId).child("content").addListenerForSingleValueEvent(valueEventListener)
  }
}