package org.noel.dunsceal.models

interface DunStore {
  fun findAll(): List<DunModel>
  fun create(dun: DunModel)
  fun update(dun: DunModel)
}