package ie.noel.dunsceal.persistence.db.storeroom

import android.content.Context
import androidx.room.Room
import ie.noel.dunsceal.models.Dun
import ie.noel.dunsceal.models.DunStore
import ie.noel.dunsceal.persistence.db.DunDatabase
import ie.noel.dunsceal.persistence.db.dao.DunDao


class DunStoreRoom(val context: Context) : DunStore {

  var dao: DunDao

  init {
    val database = Room.databaseBuilder(context, DunDatabase::class.java, "room_dun.db")
        .fallbackToDestructiveMigration()
        .build()
    dao = database.dunDao()
  }

  override fun findAll(): List<Dun?>? {
    return dao.findAll()
  }

  override fun findById(id: Int): Dun? {
    return dao.findById(id)
  }

  override fun create(dun: Dun) {
    dao.create(dun)
  }

  override fun update(dun: Dun) {
    dao.updateDun(dun)
  }

  override fun delete(dun: Dun) {
    dao.deleteDun(dun)
  }

  override fun clear() {
  }
}