package ie.noel.dunsceal.models

import ie.noel.dunsceal.models.entity.DunEntity

interface DunStore {
  // list
  fun findAll(): List<DunEntity?>
  fun create(dun: DunEntity)
  fun update(dun: DunEntity)
  fun delete(dun: DunEntity)
  fun findById(dunId: Long) : DunEntity?
  fun clear()
}