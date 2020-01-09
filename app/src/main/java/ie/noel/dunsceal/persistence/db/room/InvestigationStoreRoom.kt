package ie.noel.dunsceal.persistence.db.room

import android.content.Context
import androidx.room.Room
import ie.noel.dunsceal.models.entity.InvestigationEntity
import ie.noel.dunsceal.models.entity.InvestigationStoreEntity
import ie.noel.dunsceal.persistence.db.InvestigationDatabase
import ie.noel.dunsceal.persistence.db.dao.InvestigationDao


class InvestigationStoreRoom(val context: Context) : InvestigationStoreEntity {

  private var dao: InvestigationDao

  init {
    val database = Room.databaseBuilder(context, InvestigationDatabase::class.java, "room_investigation.db")
        .fallbackToDestructiveMigration()
        .build()
    dao = database.investigationDao()
  }

  override fun findAll(): List<InvestigationEntity?> {
    return dao.findAll()!!
  }

  override fun findById(id: Long): InvestigationEntity? {
    return dao.findById(id)
  }

  override fun create(investigation: InvestigationEntity) {
    dao.create(investigation)
  }

  override fun update(investigation: InvestigationEntity) {
    dao.updateInvestigation(investigation)
  }

  override fun delete(investigation: InvestigationEntity) {
    dao.deleteInvestigation(investigation)
  }

  override fun clear() {
  }
}