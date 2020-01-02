package ie.noel.dunsceal.persistence;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import ie.noel.dunsceal.models.entity.DunEntity;
import ie.noel.dunsceal.models.entity.InvestigationEntity;
import ie.noel.dunsceal.persistence.db.MockDatabase;

import java.util.List;

/**
 * Repository handling the work with duns and investigations.
 */
public class DataRepository {

    private static DataRepository sInstance;

    private final MockDatabase mDatabase;
    private MediatorLiveData<List<DunEntity>> mObservableDuns;

    private DataRepository(final MockDatabase database) {
        mDatabase = database;
        mObservableDuns = new MediatorLiveData<>();

        mObservableDuns.addSource(mDatabase.dunDao().loadAllDuns(),
                dunEntities -> {
                    if (mDatabase.getDatabaseCreated().getValue() != null) {
                        mObservableDuns.postValue(dunEntities);
                    }
                });
    }

    public static DataRepository getInstance(final MockDatabase database) {
        if (sInstance == null) {
            synchronized (DataRepository.class) {
                if (sInstance == null) {
                    sInstance = new DataRepository(database);
                }
            }
        }
        return sInstance;
    }

    /**
     * Get the list of duns from the database and get notified when the data changes.
     */
    public LiveData<List<DunEntity>> getDuns() {
        return mObservableDuns;
    }

    public LiveData<DunEntity> loadDun(final int dunId) {
        return mDatabase.dunDao().loadDun(dunId);
    }

    public LiveData<List<InvestigationEntity>> loadInvestigations(final int dunId) {
        return mDatabase.InvestigationDao().loadInvestigations(dunId);
    }

    public LiveData<List<DunEntity>> searchDuns(String query) {
        return mDatabase.dunDao().searchAllDuns(query);
    }
}
