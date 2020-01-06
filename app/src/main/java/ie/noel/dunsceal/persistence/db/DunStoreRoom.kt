package ie.noel.dunsceal.persistence.db

import android.content.Context
import androidx.room.Room
import ie.noel.dunsceal.models.entity.DunEntity
import ie.noel.dunsceal.models.entity.DunStoreEntity
import ie.noel.dunsceal.persistence.db.dao.DunDao


class DunStoreRoom(val context: Context) : DunStoreEntity {

  private var dao: DunDao

  init {
    val database = Room.databaseBuilder(context, DunDatabase::class.java, "room_dun.db")
        .fallbackToDestructiveMigration()
        .build()
    dao = database.dunDao()
  }

  override fun findAll(): List<DunEntity?> {
    return dao.findAll()!!
  }

  override fun findById(id: Long): DunEntity? {
    return dao.findById(id)
  }

  override fun create(dun: DunEntity) {
    dao.create(dun)
  }

  override fun update(dun: DunEntity) {
    dao.updateDun(dun)
  }

  override fun delete(dun: DunEntity) {
    dao.deleteDun(dun)
  }

  override fun clear() {
  }
}