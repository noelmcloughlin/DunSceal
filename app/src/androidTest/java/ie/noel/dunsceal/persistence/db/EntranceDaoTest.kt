
/*
 * Copyright (C) 2017 The Android Open Source Project
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
package ie.noel.dunsceal.persistence.db

import android.database.sqlite.SQLiteConstraintException
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.runner.AndroidJUnit4
import ie.noel.dunsceal.persistence.LiveDataTestUtil
import ie.noel.dunsceal.persistence.db.dao.DunDao
import ie.noel.dunsceal.persistence.db.dao.EntranceDao
import junit.framework.Assert
import org.hamcrest.Matchers
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Test the implementation of [EntranceDao]
 */
@RunWith(AndroidJUnit4::class)
class EntranceDaoTest {
  @Rule
  var instantTaskExecutorRule = InstantTaskExecutorRule()
  private var mDatabase: MockDatabase? = null
  private var mEntranceDao: EntranceDao? = null
  private var mDunDao: DunDao? = null
  @Before
  @Throws(Exception::class)
  fun initDb() { // using an in-memory database because the information stored here disappears when the
// process is killed
    mDatabase = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(),
        MockDatabase::class.java) // allowing main thread queries, just for testing
        .allowMainThreadQueries()
        .build()
    mEntranceDao = mDatabase!!.EntranceDao()
    mDunDao = mDatabase!!.dunDao()
  }

  @After
  @Throws(Exception::class)
  fun closeDb() {
    mDatabase!!.close()
  }

  @get:Throws(InterruptedException::class)
  @get:Test
  val entrancesWhenNoEntranceInserted: Unit
    get() {
      val entrances = LiveDataTestUtil.getValue(mEntranceDao!!.loadEntrances(TestData.ENTRANCE_ENTITY.dunId))
      Assert.assertTrue(entrances.isEmpty())
    }

  @Test
  @Throws(InterruptedException::class)
  fun cantInsertEntranceWithoutDun() {
    try {
      mEntranceDao!!.insertAll(TestData.ENTRANCES)
      Assert.fail("SQLiteConstraintException expected")
    } catch (ignored: SQLiteConstraintException) {
    }
  }

  @get:Throws(InterruptedException::class)
  @get:Test
  val entrancesAfterInserted: Unit
    get() {
      mDunDao!!.insertAll(TestData.DUNS)
      mEntranceDao!!.insertAll(TestData.ENTRANCES)
      val entrances = LiveDataTestUtil.getValue(mEntranceDao!!.loadEntrances(TestData.ENTRANCE_ENTITY.dunId))
      org.junit.Assert.assertThat(entrances.size, Matchers.`is`(1))
    }

  @get:Throws(InterruptedException::class)
  @get:Test
  val entranceByDunId: Unit
    get() {
      mDunDao!!.insertAll(TestData.DUNS)
      mEntranceDao!!.insertAll(TestData.ENTRANCES)
      val entrances = LiveDataTestUtil.getValue(mEntranceDao!!.loadEntrances(
          TestData.ENTRANCE_ENTITY.dunId))
      org.junit.Assert.assertThat(entrances.size, Matchers.`is`(1))
    }
}