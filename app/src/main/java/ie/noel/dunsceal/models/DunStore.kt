package ie.noel.dunsceal.models

interface DunStore {
  fun findAll(): List<Dun?>?
  fun create(dun: Dun)
  fun update(dun: Dun)
  fun delete(dun: Dun)
  fun findById(id: Int) : Dun?

  fun clear()
}