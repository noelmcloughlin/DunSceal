package ie.noel.dunsceal.persistence.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ie.noel.dunsceal.models.Dun
import ie.noel.dunsceal.persistence.db.dao.DunDao

@Database(entities = [Dun::class], version = 1,  exportSchema = false)
abstract class DunDatabase : RoomDatabase() {

  abstract fun dunDao(): DunDao
}