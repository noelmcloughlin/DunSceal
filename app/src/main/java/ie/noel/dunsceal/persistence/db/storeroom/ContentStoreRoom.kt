package ie.noel.dunsceal.persistence.db.storeroom

import android.content.Context
import androidx.room.Room
import ie.noel.dunsceal.models.Content
import ie.noel.dunsceal.models.ContentStore
import ie.noel.dunsceal.persistence.db.ContentDatabase
import ie.noel.dunsceal.persistence.db.dao.ContentDao


class ContentStoreRoom(val context: Context) : ContentStore {

  var dao: ContentDao

  init {
    val database = Room.databaseBuilder(context, ContentDatabase::class.java, "room_content.db")
        .fallbackToDestructiveMigration()
        .build()
    dao = database.contentDao()
  }

  override fun findAll(): List<Content?>? {
    return dao.findAll()
  }

  override fun findById(id: Int): Content? {
    return dao.findById(id)
  }

  override fun create(content: Content) {
    dao.create(content)
  }

  override fun update(content: Content) {
    dao.update(content)
  }

  override fun delete(content: Content) {
    dao.delete(content)
  }

  override fun clear() {
  }
}