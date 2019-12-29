package ie.noel.dunsceal.models.room

import android.content.Context
import androidx.room.Room
import ie.noel.dunsceal.models.DunModel
import ie.noel.dunsceal.models.DunStore

class DunStoreRoom(val context: Context) : DunStore {

  var dao: DunDao

  init {
    val database = Room.databaseBuilder(context, Database::class.java, "room_sample.db")
        .fallbackToDestructiveMigration()
        .build()
    dao = database.dunDao()
  }

  override fun findAll(): List<DunModel> {
    return dao.findAll()
  }

  override fun findById(id: Long): DunModel? {
    return dao.findById(id)
  }

  override fun create(dun: DunModel) {
    dao.create(dun)
  }

  override fun update(dun: DunModel) {
    dao.update(dun)
  }

  override fun delete(dun: DunModel) {
    dao.deleteDun(dun)
  }

  override fun clear() {
  }
}