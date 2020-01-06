package ie.noel.dunsceal.models.entity

interface DunStore {
  fun findAll(): List<Dun?>
  fun create(dun: Dun)
  fun update(dun: Dun)
  fun delete(dun: Dun)
  fun findById(id: Long) : Dun?
  fun clear()
}