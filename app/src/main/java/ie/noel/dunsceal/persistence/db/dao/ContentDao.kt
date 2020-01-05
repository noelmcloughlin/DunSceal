
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
import ie.noel.dunsceal.models.Content
import ie.noel.dunsceal.models.entity.ContentEntity

@Dao
interface ContentDao {
  @Query("SELECT * FROM content")
  fun loadAll(): LiveData<List<ContentEntity?>?>?

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun insertAll(content: List<ContentEntity?>?)

  @Query("select * from content where id = :contentId")
  fun load(contentId: Int): LiveData<ContentEntity?>?

  @Query("select * from content where id = :contentId")
  fun loadSync(contentId: Int): ContentEntity?

  @Query("SELECT content.* FROM content JOIN contentFts ON (content.id = contentFts.rowid) "
      + "WHERE contentFts MATCH :query")
  fun searchAll(query: String?): LiveData<List<ContentEntity?>?>?

  // Room DB integration without LiveData

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun create(content: Content?)

  @Query("SELECT * FROM content")
  fun findAll(): List<Content?>?

  @Query("select * from content where id = :id")
  fun findById(id: Int): Content?

  @Update
  fun update(content: Content?)

  @Delete
  fun delete(content: Content?)
}