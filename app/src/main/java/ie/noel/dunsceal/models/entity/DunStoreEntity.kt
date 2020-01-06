package ie.noel.dunsceal.models.entity

interface DunStoreEntity {
  fun findAll(): List<DunEntity?>
  fun create(dun: DunEntity)
  fun update(dun: DunEntity)
  fun delete(dun: DunEntity)
  fun findById(id: Long) : DunEntity?
  fun clear()
}