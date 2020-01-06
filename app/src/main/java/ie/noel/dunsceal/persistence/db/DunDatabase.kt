package ie.noel.dunsceal.persistence.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ie.noel.dunsceal.models.entity.Dun
import ie.noel.dunsceal.models.entity.DunFts
import ie.noel.dunsceal.models.entity.Investigation
import ie.noel.dunsceal.persistence.db.dao.DunDao

@Database(entities = [Dun::class, DunFts::class, Investigation::class], version = 2,  exportSchema = false)
@TypeConverters(DunTypeConverters::class)
abstract class DunDatabase : RoomDatabase() {

  abstract fun dunDao(): DunDao
}