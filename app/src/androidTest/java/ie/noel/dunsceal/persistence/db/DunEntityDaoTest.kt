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

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import ie.noel.dunsceal.persistence.db.dao.DunDao
import ie.noel.dunsceal.persistence.db.mock.MockDatabase
import org.junit.*

/**
 * Test the implementation of [DunDao]
 */
class DunEntityDaoTest {
  @Rule
  var instantTaskExecutorRule = InstantTaskExecutorRule()
  private var mDatabase: MockDatabase? = null
  private var mDunDao: DunDao? = null
  @Before
  @Throws(Exception::class)
  fun initDb() { // using an in-memory database because the information stored here disappears when the
// process is killed
    mDatabase = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(),
        MockDatabase::class.java) // allowing main thread queries, just for testing
        .allowMainThreadQueries()
        .build()
    mDunDao = mDatabase!!.dunDao()
  }

  @After
  @Throws(Exception::class)
  fun closeDb() {
    mDatabase!!.close()
  }

}