
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
package ie.noel.dunsceal.persistence.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import ie.noel.dunsceal.models.entity.EntranceEntity
import java.util.*

@Dao
interface EntranceDao {
  @Query("SELECT * FROM entrances where dunId = :dunId")
  fun loadEntrances(dunId: Int): LiveData<List<EntranceEntity>?>?

  @Query("SELECT * FROM entrances where dunId = :dunId")
  fun loadEntrancesSync(dunId: Int): List<EntranceEntity?>?

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun insertAll(entrances: List<EntranceEntity?>?)

  // DunSceal Room DB integration

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun createDun(entrance: EntranceEntity?)

  @Query("SELECT * FROM entrances")
  fun findAll(): ArrayList<EntranceEntity?>?

  @Query("select * from entrances where id = :id")
  fun findById(id: Int): EntranceEntity?

  @Update
  fun updateEntrance(entrance: EntranceEntity?)

  @Delete
  fun deleteEntrance(entrance: EntranceEntity?)
}