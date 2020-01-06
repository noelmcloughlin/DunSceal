package ie.noel.dunsceal.models

import ie.noel.dunsceal.models.entity.DunEntity

interface DunStore {
  fun findAll(): List<DunEntity?>?
  fun create(dun: DunEntity)
  fun update(dun: DunEntity)
  fun delete(dun: DunEntity)
  fun findById(id: Long) : DunEntity?
  fun clear()
}