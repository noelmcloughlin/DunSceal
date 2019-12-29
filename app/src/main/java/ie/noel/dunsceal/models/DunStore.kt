package ie.noel.dunsceal.models

interface DunStore {
  fun findAll(): List<DunModel>
  fun create(dun: DunModel)
  fun update(dun: DunModel)
  fun delete(dun: DunModel)
  fun findById(id:Long) : DunModel?
  fun clear()
}