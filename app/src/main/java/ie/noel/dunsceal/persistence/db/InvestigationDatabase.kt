package ie.noel.dunsceal.persistence.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ie.noel.dunsceal.models.entity.InvestigationEntity
import ie.noel.dunsceal.persistence.db.dao.InvestigationDao

@Database(entities = [InvestigationEntity::class], version = 2,  exportSchema = false)
abstract class InvestigationDatabase : RoomDatabase() {

  abstract fun investigationDao(): InvestigationDao
}