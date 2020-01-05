package ie.noel.dunsceal.persistence

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import ie.noel.dunsceal.models.entity.DunEntity
import ie.noel.dunsceal.models.entity.InvestigationEntity
import ie.noel.dunsceal.persistence.db.MockDatabase

/**
 * Repository handling the work with duns and investigations.
 */
class DataRepository private constructor(private val mDatabase: MockDatabase) {
  private val mObservableDuns: MediatorLiveData<List<DunEntity?>?>
  /**
   * Get the list of duns from the database and get notified when the data changes.
   */
  val duns: LiveData<List<DunEntity?>?>
    get() = mObservableDuns

  fun loadDun(dunId: Int): LiveData<DunEntity?>? {
    return mDatabase.dunDao().loadDun(dunId)
  }

  fun loadInvestigations(dunId: Int): LiveData<List<InvestigationEntity>?>? {
    return mDatabase.investigationDao().loadInvestigations(dunId)
  }

  fun searchDuns(query: String?): LiveData<List<DunEntity?>?>? {
    return mDatabase.dunDao().searchAllDuns(query)
  }

  companion object {
    private var sInstance: DataRepository? = null
    fun getInstance(database: MockDatabase): DataRepository? {
      if (sInstance == null) {
        synchronized(DataRepository::class.java) {
          if (sInstance == null) {
            sInstance = DataRepository(database)
          }
        }
      }
      return sInstance
    }
  }

  init {
    mObservableDuns = MediatorLiveData()
    mObservableDuns.addSource(mDatabase.dunDao().loadAllDuns()!!
    ) { dunEntities: List<DunEntity?>? ->
      if (mDatabase.databaseCreated.value != null) {
        mObservableDuns.postValue(dunEntities)
      }
    }
  }
}