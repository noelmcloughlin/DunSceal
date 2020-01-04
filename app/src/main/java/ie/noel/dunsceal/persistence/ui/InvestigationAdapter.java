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

import androidx.databinding.DataBindingUtil;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import ie.noel.dunsceal.R;
import ie.noel.dunsceal.databinding.InvestigationItemBinding;
import ie.noel.dunsceal.persistence.db.entity.InvestigationEntity;
import ie.noel.dunsceal.persistence.model.Investigation;

import java.util.List;
import java.util.Objects;

public class InvestigationAdapter extends RecyclerView.Adapter<InvestigationAdapter.InvestigationViewHolder> {

    private List<? extends Investigation> mInvestigationList;

    @Nullable
    private final InvestigationClickCallback mInvestigationClickCallback;

    public InvestigationAdapter(@Nullable InvestigationClickCallback investigationClickCallback) {
        mInvestigationClickCallback = investigationClickCallback;
    }

    public void setInvestigationList(final List<? extends InvestigationEntity> investigations) {
        if (mInvestigationList == null) {
            mInvestigationList = investigations;
            notifyItemRangeInserted(0, investigations.size());
        } else {
            DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return mInvestigationList.size();
                }

                @Override
                public int getNewListSize() {
                    return investigations.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    Investigation old = mInvestigationList.get(oldItemPosition);
                    InvestigationEntity investigation = investigations.get(newItemPosition);
                    return old.getId() == investigation.getId();
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    Investigation old = mInvestigationList.get(oldItemPosition);
                    Investigation investigation = investigations.get(newItemPosition);
                    return old.getId() == investigation.getId()
                            && old.getPostedAt() == investigation.getPostedAt()
                            && old.getDunId() == investigation.getDunId()
                            && Objects.equals(old.getText(), investigation.getText());
                }
            });
            mInvestigationList = investigations;
            diffResult.dispatchUpdatesTo(this);
        }
    }

    @Override
    public InvestigationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        InvestigationItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.investigation_item,
                        parent, false);
        binding.setCallback(mInvestigationClickCallback);
        return new InvestigationViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(InvestigationViewHolder holder, int position) {
        holder.binding.setInvestigation(mInvestigationList.get(position));
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return mInvestigationList == null ? 0 : mInvestigationList.size();
    }

    static class InvestigationViewHolder extends RecyclerView.ViewHolder {

        final InvestigationItemBinding binding;

        InvestigationViewHolder(InvestigationItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
