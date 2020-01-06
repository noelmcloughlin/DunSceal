
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

@Dao
interface DunDao {
  @Query("SELECT * FROM duns")
  fun loadAll(): LiveData<List<ie.noel.dunsceal.models.entity.DunEntity?>?>?

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun insertAll(duns: List<ie.noel.dunsceal.models.entity.DunEntity?>?)

  @Query("select * from duns where id = :dunId")
  fun load(dunId: Int): LiveData<ie.noel.dunsceal.models.entity.DunEntity?>?

  @Query("select * from duns where id = :dunId")
  fun loadSync(dunId: Int): ie.noel.dunsceal.models.entity.DunEntity?

  @Query("SELECT duns.* FROM duns JOIN dunsFts ON (duns.id = dunsFts.rowid) "
      + "WHERE dunsFts MATCH :query")
  fun searchAll(query: String?): LiveData<List<ie.noel.dunsceal.models.entity.DunEntity?>?>?


  // Room DB integration without liveData

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun create(dunEntity: ie.noel.dunsceal.models.entity.DunEntity?)

  @Query("SELECT * FROM duns")
  fun findAll(): List<ie.noel.dunsceal.models.entity.DunEntity?>?

  @Query("select * from duns where id = :id")
  fun findById(id: Long): ie.noel.dunsceal.models.entity.DunEntity?

  @Update
  fun updateDun(dunEntity: ie.noel.dunsceal.models.entity.DunEntity?)

  @Delete
  fun deleteDun(dunEntity: ie.noel.dunsceal.models.entity.DunEntity?)
}