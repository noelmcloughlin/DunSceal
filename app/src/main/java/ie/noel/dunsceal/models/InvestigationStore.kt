package ie.noel.dunsceal.models

import ie.noel.dunsceal.models.entity.InvestigationEntity

interface InvestigationStore {
  fun findAll(): List<InvestigationEntity?>?
  fun create(dun: InvestigationEntity)
  fun update(dun: InvestigationEntity)
  fun delete(dun: InvestigationEntity)
  fun findById(id: Long) : InvestigationEntity?
  fun clear()
}