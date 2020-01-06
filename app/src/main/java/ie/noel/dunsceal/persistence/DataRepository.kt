package ie.noel.dunsceal.persistence

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import ie.noel.dunsceal.models.entity.Dun
import ie.noel.dunsceal.models.entity.Investigation
import ie.noel.dunsceal.persistence.db.mock.MockDatabase

/**
 * Repository handling the work with duns and investigations.
 */
class DataRepository private constructor(private val mDatabase: MockDatabase) {
  private val mObservableDuns: MediatorLiveData<List<Dun?>?>
  /**
   * Get the list of duns from the database and get notified when the data changes.
   */
  val duns: LiveData<List<Dun?>?>
    get() = mObservableDuns

  fun loadDun(dunId: Int): LiveData<Dun?>? {
    return mDatabase.dunDao().load(dunId)
  }

  fun loadInvestigations(dunId: Int): LiveData<List<Investigation>?>? {
    return mDatabase.investigationDao().loadInvestigations(dunId)
  }

  fun searchDuns(query: String?): LiveData<List<Dun?>?>? {
    return mDatabase.dunDao().searchAll(query)
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
    ) { dunEntities: List<Dun?>? ->
      if (mDatabase.databaseCreated.value != null) {
        mObservableDuns.postValue(dunEntities)
      }
    }
  }
}