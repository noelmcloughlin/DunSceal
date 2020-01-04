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

package ie.noel.dunsceal.persistence.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.util.List;

import ie.noel.dunsceal.R;
import ie.noel.dunsceal.databinding.DunFragmentBinding;
import ie.noel.dunsceal.persistence.db.entity.DunEntity;
import ie.noel.dunsceal.persistence.db.entity.InvestigationEntity;
import ie.noel.dunsceal.persistence.model.Investigation;
import ie.noel.dunsceal.persistence.viewmodel.DunViewModel;

public class DunFragment extends Fragment {

    private static final String KEY_DUN_ID = "dun_id";

    private DunFragmentBinding mBinding;

    private InvestigationAdapter mInvestigationAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        // Inflate this data binding layout
        mBinding = DataBindingUtil.inflate(inflater, R.layout.dun_fragment, container, false);

        // Create and set the adapter for the RecyclerView.
        mInvestigationAdapter = new InvestigationAdapter(mInvestigationClickCallback);
        mBinding.investigationList.setAdapter(mInvestigationAdapter);
        return mBinding.getRoot();
    }

    private final InvestigationClickCallback mInvestigationClickCallback = new InvestigationClickCallback() {
        @Override
        public void onClick(Investigation investigation) {
            // no-op

        }
    };

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        DunViewModel.Factory factory = new DunViewModel.Factory(
                requireActivity().getApplication(), getArguments().getInt(KEY_DUN_ID));

        final DunViewModel model = new ViewModelProvider(this, factory)
                .get(DunViewModel.class);

        mBinding.setDunViewModel(model);

        subscribeToModel(model);
    }

    private void subscribeToModel(final DunViewModel model) {

        // Observe dun data
        model.getObservableDun().observe(getViewLifecycleOwner(), new Observer<DunEntity>() {
            @Override
            public void onChanged(@Nullable DunEntity dunEntity) {
                model.setDun(dunEntity);
            }
        });

        // Observe investigations
        model.getInvestigations().observe(this, new Observer<List<InvestigationEntity>>() {
            @Override
            public void onChanged(@Nullable List<InvestigationEntity> investigationEntities) {
                if (investigationEntities != null) {
                    mBinding.setIsLoading(false);
                    mInvestigationAdapter.setInvestigationList(investigationEntities);
                } else {
                    mBinding.setIsLoading(true);
                }
            }
        });
    }

    /** Creates dun fragment for specific dun ID */
    public static DunFragment forDun(int dunId) {
        DunFragment fragment = new DunFragment();
        Bundle args = new Bundle();
        args.putInt(KEY_DUN_ID, dunId);
        fragment.setArguments(args);
        return fragment;
    }
}
