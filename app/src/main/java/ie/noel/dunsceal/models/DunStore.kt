package ie.noel.dunsceal.models

import ie.noel.dunsceal.models.entity.Dun

interface DunStore {
  fun findAll(): List<Dun?>?
  fun create(dun: Dun)
  fun update(dun: Dun)
  fun delete(dun: Dun)
  fun findById(id: Long) : Dun?
  fun clear()
}