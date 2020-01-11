package ie.noel.dunsceal.models

import androidx.lifecycle.LiveData
import ie.noel.dunsceal.models.entity.DunEntity
import ie.noel.dunsceal.models.entity.InvestigationEntity

interface LiveDataRepository {
  fun ldLoadDun(dunId: Long): LiveData<DunEntity?>?
  fun ldLoadInvestigations(dunId: Long): LiveData<InvestigationEntity>
  fun ldSearchDuns(query: String?): LiveData<List<DunEntity?>?>?
}