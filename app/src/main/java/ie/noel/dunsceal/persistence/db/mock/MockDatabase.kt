/*
 * Copyright 2017, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ie.noel.dunsceal.persistence.db.mock

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import ie.noel.dunsceal.models.entity.DunEntity
import ie.noel.dunsceal.models.entity.DunFtsEntity
import ie.noel.dunsceal.models.entity.InvestigationEntity
import ie.noel.dunsceal.persistence.AppExecutors
import ie.noel.dunsceal.persistence.db.mock.MockDataGenerator.generateDuns
import ie.noel.dunsceal.persistence.db.mock.MockDataGenerator.generateInvestigationsForDuns
import ie.noel.dunsceal.persistence.db.dao.DunDao
import ie.noel.dunsceal.persistence.db.dao.InvestigationDao
import ie.noel.dunsceal.utils.DateConverter

@Database(entities = [DunEntity::class, DunFtsEntity::class, InvestigationEntity::class], version = 2)
@TypeConverters(DateConverter::class)
abstract class MockDatabase : RoomDatabase() {
  abstract fun dunDao(): DunDao
  abstract fun investigationDao(): InvestigationDao
  private val mIsDatabaseCreated = MutableLiveData<Boolean>()
  /**
   * Check whether the database already exists and expose it via [.getDatabaseCreated]
   */
  private fun updateDatabaseCreated(context: Context) {
    if (context.getDatabasePath(DATABASE_NAME).exists()) {
      setDatabaseCreated()
    }
  }

  private fun setDatabaseCreated() {
    mIsDatabaseCreated.postValue(true)
  }

  val databaseCreated: LiveData<Boolean>
    get() = mIsDatabaseCreated

  companion object {
    private var sInstance: MockDatabase? = null
    @VisibleForTesting
    val DATABASE_NAME = "basic-sample-db"

    fun getInstance(context: Context, executors: AppExecutors): MockDatabase? {
      if (sInstance == null) {
        synchronized(MockDatabase::class.java) {
          if (sInstance == null) {
            sInstance = buildDatabase(context.applicationContext, executors)
            sInstance!!.updateDatabaseCreated(context.applicationContext)
          }
        }
      }
      return sInstance
    }

    /**
     * Build the database. [Builder.build] only sets up the database configuration and
     * creates a new instance of the database.
     * The SQLite database is only created when it's accessed for the first time.
     */
    private fun buildDatabase(appContext: Context,
                              executors: AppExecutors): MockDatabase {
      return Room.databaseBuilder(appContext, MockDatabase::class.java, DATABASE_NAME)
          .addCallback(object : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
              super.onCreate(db)
              executors.diskIO().execute {
                // Add a delay to simulate a long-running operation
                addDelay()
                // Generate the data for pre-population
                val database = getInstance(appContext, executors)
                val duns: List<DunEntity?> = generateDuns()
                val investigations: List<InvestigationEntity?> = generateInvestigationsForDuns(duns as List<DunEntity>)
                insertData(database, duns, investigations)
                // notify that the database was created and it's ready to be used
                database!!.setDatabaseCreated()
              }
            }
          })
          .addMigrations(MIGRATION_1_2)
          .build()
    }

    private fun insertData(database: MockDatabase?,
                           duns: List<DunEntity?>,
                           investigations: List<InvestigationEntity?>) {
      database!!.runInTransaction {
        database.dunDao().insertAll(duns)
        database.investigationDao().insertAll(investigations)
      }
    }

    private fun addDelay() {
      try {
        Thread.sleep(4000)
      } catch (ignored: InterruptedException) {
      }
    }

    private val MIGRATION_1_2: Migration = object : Migration(1, 2) {
      override fun migrate(database: SupportSQLiteDatabase) { // IDE warning about 'TEXT,' can be ignored in Java
        database.execSQL("CREATE VIRTUAL TABLE IF NOT EXISTS `dunsFts` USING FTS4("
            + "`name` TEXT, `description` TEXT, content=`duns`)")
        database.execSQL("INSERT INTO dunsFts (`rowid`, `name`, `description`) "
            + "SELECT `id`, `name`, `description` FROM duns")
      }
    }
  }
}