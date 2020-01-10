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
package ie.noel.dunsceal.persistence.db.room

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
import ie.noel.dunsceal.main.AppExecutors
import ie.noel.dunsceal.models.entity.DunEntity
import ie.noel.dunsceal.models.entity.DunFtsEntity
import ie.noel.dunsceal.models.entity.InvestigationEntity
import ie.noel.dunsceal.persistence.db.dao.DunDao
import ie.noel.dunsceal.persistence.db.dao.InvestigationDao
import ie.noel.dunsceal.persistence.db.mock.MockDataGenerator


@Database(entities = [DunEntity::class, DunFtsEntity::class, InvestigationEntity::class], version = 2)
@TypeConverters(DunTypeConverters::class)
abstract class DunDatabase : RoomDatabase() {

  abstract fun dunDao(): DunDao
  abstract fun investigationDao(): InvestigationDao





  /********** TESTING ONLY **************/
  /********** TESTING ONLY **************/
  /********** TESTING ONLY **************/

  private val mIsDatabaseCreated = MutableLiveData<Boolean>()
  /**
   * Check whether the database already exists and expose it via [.getDatabaseCreated]
   */
  private fun updateDatabaseCreated(context: Context) {
    if (context.getDatabasePath(DUN_DATABASE_NAME).exists()) {
      setDatabaseCreated()
    }
  }

  private fun setDatabaseCreated() {
    mIsDatabaseCreated.postValue(true)
  }

  val databaseCreated: LiveData<Boolean>
    get() = mIsDatabaseCreated

  companion object {
    private var sInstance: DunDatabase? = null
    @VisibleForTesting
    val DUN_DATABASE_NAME = "dunsceal-db"

    fun getInstance(context: Context, executors: AppExecutors): DunDatabase? {
      if (sInstance == null) {
        synchronized(DunDatabase::class.java) {
          if (sInstance == null) {
            sInstance = buildDatabase(context.applicationContext, executors)
            sInstance!!.updateDatabaseCreated(context.applicationContext)
          }
        }
      }
      return sInstance
    }

    /**
     * Build the database. Builder.build only sets up the database configuration and
     * creates a new instance of the database.
     * The SQLite database is only created when it's accessed for the first time.
     */
    private fun buildDatabase(appContext: Context,
                              executors: AppExecutors): DunDatabase {
      return Room.databaseBuilder(appContext, DunDatabase::class.java, DUN_DATABASE_NAME)
          .addCallback(object : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
              super.onCreate(db)
              executors.diskIO().execute {
                // Add a delay to simulate a long-running operation
                addDelay()
                // Generate the data for pre-population
                val database = getInstance(appContext, executors)
                val duns: List<DunEntity> = MockDataGenerator.generateDuns()
                val investigationEntities: List<InvestigationEntity> = MockDataGenerator.generateInvestigationsForDuns(duns)
                insertData(database, duns, investigationEntities)
                // notify that the database was created and it's ready to be used
                database!!.setDatabaseCreated()
              }
            }
          })
          .addMigrations(MIGRATION_1_2)
          .build()
    }


    private fun insertData(database: DunDatabase?, duns: List<DunEntity>,
                           investigations: List<InvestigationEntity>) {
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
      override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("CREATE VIRTUAL TABLE IF NOT EXISTS `dunsFts` USING FTS4("
            + "`name` TEXT, `description` TEXT, content=`duns`)")
        database.execSQL("INSERT INTO dunsFts (`rowid`, `name`, `description`) "
            + "SELECT `id`, `name`, `description` FROM duns")
      }
    }
  }
}