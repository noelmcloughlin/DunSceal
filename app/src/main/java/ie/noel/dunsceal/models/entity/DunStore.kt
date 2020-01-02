package ie.noel.dunsceal.models.entity

import ie.noel.dunsceal.models.entity.DunEntity

interface DunStore {
  fun findAll(): List<DunEntity?>
  fun createDun(dun: DunEntity)
  fun updateDun(dun: DunEntity)
  fun deleteDun(dun: DunEntity)
  fun findById(id: Int) : DunEntity?
  fun clear()
}