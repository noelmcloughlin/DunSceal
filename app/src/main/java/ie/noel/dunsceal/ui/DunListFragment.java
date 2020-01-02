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

package ie.noel.dunsceal.ui;

import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import org.jetbrains.annotations.NotNull;

import ie.noel.dunsceal.R;
import ie.noel.dunsceal.adapters.DunViewDataAdapter;
import ie.noel.dunsceal.databinding.ListFragmentBinding;

import ie.noel.dunsceal.main.MainActivity;
import ie.noel.dunsceal.models.entity.DunEntity;
import ie.noel.dunsceal.persistence.viewmodel.DunListViewModel;

import java.util.List;

public class DunListFragment extends Fragment {

    public static final String TAG = "DunListFragment";

    private DunViewDataAdapter mDunViewDataAdapter;

    private ListFragmentBinding mBinding;

    @Nullable
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.list_fragment, container, false);

        mDunViewDataAdapter = new DunViewDataAdapter(mDunClickCallback);
        mBinding.dunsList.setAdapter(mDunViewDataAdapter);

        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final DunListViewModel viewModel =
                new ViewModelProvider(this).get(DunListViewModel.class);

        mBinding.dunsSearchBtn.setOnClickListener(v -> {
            Editable query = mBinding.dunsSearchBox.getText();
            if (query == null || query.toString().isEmpty()) {
                subscribeUi(viewModel.getDuns());
            } else {
                subscribeUi(viewModel.searchDuns("*" + query + "*"));
            }
        });

        subscribeUi(viewModel.getDuns());
    }

    private void subscribeUi(LiveData<List<DunEntity>> liveData) {
        // Update the list when the data changes
        liveData.observe(getViewLifecycleOwner(), myDuns -> {
            if (myDuns != null) {
                mBinding.setIsLoading(false);
                mDunViewDataAdapter.setDunList(myDuns);
            } else {
                mBinding.setIsLoading(true);
            }
            // espresso does not know how to wait for data binding's loop so we execute changes
            // sync.
            mBinding.executePendingBindings();
        });
    }

    private final DunClickCallback mDunClickCallback = dunModel -> {

        if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
            ((MainActivity) getActivity()).show(dunModel);
        }
    };
}
