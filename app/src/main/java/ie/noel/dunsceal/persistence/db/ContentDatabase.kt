package ie.noel.dunsceal.persistence.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ie.noel.dunsceal.models.Content
import ie.noel.dunsceal.persistence.db.dao.ContentDao

@Database(entities = [Content::class], version = 1,  exportSchema = false)
abstract class ContentDatabase : RoomDatabase() {

  abstract fun contentDao(): ContentDao
}