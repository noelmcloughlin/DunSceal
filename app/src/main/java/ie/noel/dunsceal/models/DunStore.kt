package ie.noel.dunsceal.models

import androidx.lifecycle.LiveData
import ie.noel.dunsceal.models.entity.DunEntity
import ie.noel.dunsceal.models.entity.InvestigationEntity

interface DunStore {
  // list
  fun findAll(): List<DunEntity?>
  fun create(dun: DunEntity)
  fun update(dun: DunEntity)
  fun delete(dun: DunEntity)
  fun findById(dunId: Long) : DunEntity?
  fun clear()

  // livedata list
  fun ldLoadDun(dunId: Long): LiveData<DunEntity?>?
  fun ldLoadInvestigations(dunId: Long): LiveData<InvestigationEntity>
  fun ldSearchDuns(query: String?): LiveData<List<DunEntity?>?>?
}