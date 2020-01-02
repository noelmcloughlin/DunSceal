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
package ie.noel.dunsceal.models.entity

import androidx.room.*
import ie.noel.dunsceal.models.Entrance
import java.util.*

@Entity(tableName = "entrances", foreignKeys = [ForeignKey(entity = DunEntity::class, parentColumns = ["id"], childColumns = ["dunId"], onDelete = ForeignKey.CASCADE)], indices = [Index(value = ["dunId"])])
class EntranceEntity : Entrance {
  @PrimaryKey(autoGenerate = true)
  override var id = 0
  override var dunId = 0
  override var text: String? = null
  override var postedAt: Date? = null

  constructor() // needed

  @Ignore
  constructor(id: Int, dunId: Int, text: String?, postedAt: Date?) {
    this.id = id
    this.dunId = dunId
    this.text = text
    this.postedAt = postedAt
  }
}