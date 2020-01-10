package ie.noel.dunsceal.persistence

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import ie.noel.dunsceal.models.entity.DunEntity
import ie.noel.dunsceal.models.entity.InvestigationEntity
import ie.noel.dunsceal.persistence.room.DunDatabase

/**
 * Repository handling the work with duns and investigations.
 */
class LiveDataRepository private constructor(private val mDatabase: DunDatabase) {
  private val mObservableDuns: MediatorLiveData<List<DunEntity?>?> = MediatorLiveData()
  /**
   * Get the list of duns from the database and get notified when the data changes.
   */
  val liveduns: LiveData<List<DunEntity?>?>
    get() = mObservableDuns

  fun ldLoadDun(dunId: Long): LiveData<DunEntity?>? {
    return mDatabase.dunDao().ldLoadDun(dunId.toInt())
  }
  fun ldLoadInvestigations(dunId: Long): LiveData<InvestigationEntity> {
    return mDatabase.investigationDao().ldLoadInvestigations(dunId.toInt())
  }
  fun ldSearchDuns(query: String?): LiveData<List<DunEntity?>?>? {
    return mDatabase.dunDao().ldSearchDuns(query)
  }

  companion object {
    private var sInstance: LiveDataRepository? = null
    fun getInstance(database: DunDatabase): LiveDataRepository? {
      if (sInstance == null) {
        synchronized(DunDatabase::class.java) {
          if (sInstance == null) {
            sInstance = LiveDataRepository(database)
          }
        }
      }
      return sInstance
    }
  }

  init {
    mObservableDuns.addSource(mDatabase.dunDao().ldLoadAll()!!
    ) { dunEntityEntities: List<DunEntity?>? ->
      if (mDatabase.databaseCreated.value != null) {
        mObservableDuns.postValue(dunEntityEntities)
      }
    }
  }
}