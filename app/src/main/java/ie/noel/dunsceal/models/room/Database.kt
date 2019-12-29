package ie.noel.dunsceal.models.room

import androidx.room.Database
import androidx.room.RoomDatabase
import ie.noel.dunsceal.models.DunModel

@Database(entities = [DunModel::class], version = 1,  exportSchema = false)
abstract class Database : RoomDatabase() {

  abstract fun dunDao(): DunDao
}