package ie.noel.dunsceal.models.entity

interface InvestigationStoreEntity {
  fun findAll(): List<InvestigationEntity?>
  fun create(investigation: InvestigationEntity)
  fun update(investigation: InvestigationEntity)
  fun delete(investigation: InvestigationEntity)
  fun findById(id: Long) : InvestigationEntity?
  fun clear()
}