
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
import ie.noel.dunsceal.models.Dun
import ie.noel.dunsceal.models.entity.Dun

@Dao
interface DunDao {
  @Query("SELECT * FROM duns")
  fun loadAll(): LiveData<List<Dun?>?>?

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun insertAll(duns: List<Dun?>?)

  @Query("select * from duns where id = :dunId")
  fun load(dunId: Int): LiveData<Dun?>?

  @Query("select * from duns where id = :dunId")
  fun loadSync(dunId: Int): Dun?

  @Query("SELECT duns.* FROM duns JOIN dunsFts ON (duns.id = dunsFts.rowid) "
      + "WHERE dunsFts MATCH :query")
  fun searchAll(query: String?): LiveData<List<Dun?>?>?


  // Room DB integration without liveData

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun create(dun: Dun?)

  @Query("SELECT * FROM duns")
  fun findAll(): List<Dun?>?

  @Query("select * from duns where id = :id")
  fun findById(id: Long): Dun?

  @Update
  fun updateDun(dun: Dun?)

  @Delete
  fun deleteDun(dun: Dun?)
}