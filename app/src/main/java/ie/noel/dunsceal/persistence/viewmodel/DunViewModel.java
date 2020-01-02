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
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.databinding.ObservableField;
import androidx.annotation.NonNull;

import ie.noel.dunsceal.main.MainApp;
import ie.noel.dunsceal.models.entity.DunEntity;
import ie.noel.dunsceal.models.entity.InvestigationEntity;
import ie.noel.dunsceal.persistence.DataRepository;

import java.util.List;

public class DunViewModel extends AndroidViewModel {

    private final LiveData<DunEntity> mObservableDun;

    public ObservableField<DunEntity> dunModel = new ObservableField<>();

    private final int mDunId;

    private final LiveData<List<InvestigationEntity>> mObservableInvestigations;

    public DunViewModel(@NonNull Application application, DataRepository repository,
                        final int dunId) {
        super(application);
        mDunId = dunId;

        mObservableInvestigations = repository.loadInvestigations(mDunId);
        mObservableDun = repository.loadDun(mDunId);
    }

    /**
     * Expose the LiveData Investigations query so the UI can observe it.
     */
    public LiveData<List<InvestigationEntity>> getInvestigations() {
        return mObservableInvestigations;
    }

    public LiveData<DunEntity> getObservableDun() {
        return mObservableDun;
    }

    public void setDun(DunEntity dun) {
        this.dunModel.set(dun);
    }

    /**
     * A creator is used to inject the dun ID into the ViewModel
     * <p>
     * This creator is to showcase how to inject dependencies into ViewModels. It's not
     * actually necessary in this case, as the dun ID can be passed in a public method.
     */
    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application mApplication;

        private final int mDunId;

        private final DataRepository mRepository;

        public Factory(@NonNull Application application, int dunId) {
            mApplication = application;
            mDunId = dunId;
            mRepository = ((MainApp) application).getRepository();
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new DunViewModel(mApplication, mRepository, mDunId);
        }
    }
}
