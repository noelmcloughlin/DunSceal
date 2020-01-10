
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
import ie.noel.dunsceal.models.entity.DunEntity

@Dao
interface DunDao {

  // LiveData list
  @Query("SELECT * FROM duns")
  fun ldLoadAll(): LiveData<List<DunEntity?>?>?

  @Query("select * from duns where id = :dunId")
  fun ldLoadDun(dunId: Int): LiveData<DunEntity?>?

  @Query("SELECT duns.* FROM duns JOIN dunsFts ON (duns.id = dunsFts.rowid) "
      + "WHERE dunsFts MATCH :query")
  fun ldSearchDuns(query: String?): LiveData<List<DunEntity?>?>?


  // Standard list
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun insertAll(duns: List<DunEntity?>?)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun create(dunEntity: DunEntity?)

  @Query("SELECT * FROM duns")
  fun findAll(): List<DunEntity?>?

  @Query("select * from duns where id = :dunId")
  fun findById(dunId: Long): DunEntity?

  @Update
  fun updateDun(dunEntity: DunEntity?)

  @Delete
  fun deleteDun(dunEntity: DunEntity?)
}