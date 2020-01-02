/*
 * Copyright 2017, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ie.noel.dunsceal.persistence.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import ie.noel.dunsceal.main.MainApp;
import ie.noel.dunsceal.models.entity.*;
import ie.noel.dunsceal.persistence.DataRepository;

import java.util.List;

public class DunListViewModel extends AndroidViewModel {

    private final DataRepository mRepository;

    // MediatorLiveData can observe other LiveData objects and react on their emissions.
    private final MediatorLiveData<List<DunEntity>> mObservableDuns;

    public DunListViewModel(Application application) {
        super(application);

        mObservableDuns = new MediatorLiveData<>();
        // set by default null, until we get data from the database.
        mObservableDuns.setValue(null);

        mRepository = ((MainApp) application).getRepository();
        LiveData<List<DunEntity>> duns = mRepository.getDuns();

        // observe the changes of the duns from the database and forward them
        mObservableDuns.addSource(duns, mObservableDuns::setValue);
    }

    /**
     * Expose the LiveData Duns query so the UI can observe it.
     */
    public LiveData<List<DunEntity>> getDuns() {
        return mObservableDuns;
    }

    public LiveData<List<DunEntity>> searchDuns(String query) {
        return mRepository.searchDuns(query);
    }
}
