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

import ie.noel.dunsceal.models.entity.DunEntity
import ie.noel.dunsceal.models.entity.InvestigationEntity
import ie.noel.dunsceal.models.entity.LocationEntity
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Generates data to pre-populate the database
 */

object MockDataGenerator {
  private val FIRST = arrayOf(
      "Dun", "Killy", "Knock", "Bally", "Derry")
  private val SECOND = arrayOf(
      "Bollog", "Farnham", "More", "Hoo", "Cairn")
  private val DESCRIPTION = arrayOf(
      "is a very large dun", "is recommended by Stan S. Stanman",
      "is the best  dun in Ireland", "is \uD83D\uDCAF", "is ❤️", "is fine")
  private val INVESTIGATIONS = arrayOf(
      "Investigation 1", "Investigation 2", "Investigation 3", "Investigation 4", "Investigation 5", "Investigation 6")
  private val VISITED = arrayOf(1, 0, 1, 0, 1)
  private const val IMAGE = "https://firebasestorage.googleapis.com/v0/b/dunsceal.appspot.com/o/hillfort-default.bmp?alt=media&token=d9c756f3-0cc9-4c5a-b6dc-9f13bd1d53d9"
  //private const val IMAGE = ""

  private val LATITUDES = arrayOf(52.245696, 52.245696, 52.245696, 52.245696, 52.245696, 52.245696)
  private val LONGITUDES = arrayOf(-7.139102, -7.139102, -7.139102, -7.139102, -7.139102, -7.139102)

  fun generateDuns(): List<DunEntity> {
    val duns: MutableList<DunEntity> = ArrayList(FIRST.size * SECOND.size)
    val rnd = Random()
    for (i in FIRST.indices) {
      for (j in SECOND.indices) {
        val dun = DunEntity()
        dun.name = FIRST[i] + " " + SECOND[j]
        dun.description = dun.name + " " + DESCRIPTION[j]
        dun.votes = rnd.nextInt(240)
        dun.visited = VISITED[i]
        dun.image = IMAGE[i].toString()
        dun.location.latitude = LATITUDES[i]
        dun.location.longitude = LONGITUDES[i]
        dun.location.zoom = 0f
        dun.id = (FIRST.size * i + j + 1).toLong()
        dun.image = IMAGE
        duns.add(dun)
      }
    }
    return duns
  }

  fun generateInvestigationsForDuns(
      duns: List<DunEntity>): List<InvestigationEntity> {
    val investigationEntities: MutableList<InvestigationEntity> = ArrayList()
    val rnd = Random()
    for ((id, name) in duns) {
      val investigationsNumber = rnd.nextInt(5) + 1
      for (i in 0 until investigationsNumber) {
        val investigation = InvestigationEntity()
        investigation.dunId = id
        investigation.text = INVESTIGATIONS[i] + " for " + name
        investigation.image = IMAGE[i].toString()
        investigation.postedAt = Date(System.currentTimeMillis()
            - TimeUnit.DAYS.toMillis(investigationsNumber - i.toLong()) + TimeUnit.HOURS.toMillis(i.toLong()))
        investigationEntities.add(investigation)
      }
    }
    return investigationEntities
  }
}