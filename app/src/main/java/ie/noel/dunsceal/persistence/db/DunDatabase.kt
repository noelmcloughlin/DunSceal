package ie.noel.dunsceal.persistence.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ie.noel.dunsceal.models.entity.DunEntity
import ie.noel.dunsceal.models.entity.DunFtsEntity
import ie.noel.dunsceal.models.entity.InvestigationEntity
import ie.noel.dunsceal.persistence.db.dao.DunDao

@Database(entities = [DunEntity::class, DunFtsEntity::class, InvestigationEntity::class], version = 2,  exportSchema = false)
@TypeConverters(DunTypeConverters::class)
abstract class DunDatabase : RoomDatabase() {

  abstract fun dunDao(): DunDao
}