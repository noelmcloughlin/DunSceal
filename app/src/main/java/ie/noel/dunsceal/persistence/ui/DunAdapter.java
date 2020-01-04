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
import ie.noel.dunsceal.databinding.DunItemBinding;
import ie.noel.dunsceal.persistence.model.Dun;

import java.util.List;
import java.util.Objects;

public class DunAdapter extends RecyclerView.Adapter<DunAdapter.DunViewHolder> {

    List<? extends Dun> mDunList;

    @Nullable
    private final DunClickCallback mDunClickCallback;

    public DunAdapter(@Nullable DunClickCallback clickCallback) {
        mDunClickCallback = clickCallback;
        setHasStableIds(true);
    }

    public void setDunList(final List<? extends Dun> dunList) {
        if (mDunList == null) {
            mDunList = dunList;
            notifyItemRangeInserted(0, dunList.size());
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return mDunList.size();
                }

                @Override
                public int getNewListSize() {
                    return dunList.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return mDunList.get(oldItemPosition).getId() ==
                            dunList.get(newItemPosition).getId();
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    Dun newDun = dunList.get(newItemPosition);
                    Dun oldDun = mDunList.get(oldItemPosition);
                    return newDun.getId() == oldDun.getId()
                            && Objects.equals(newDun.getDescription(), oldDun.getDescription())
                            && Objects.equals(newDun.getName(), oldDun.getName())
                            && newDun.getPrice() == oldDun.getPrice();
                }
            });
            mDunList = dunList;
            result.dispatchUpdatesTo(this);
        }
    }

    @Override
    public DunViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        DunItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.dun_item,
                        parent, false);
        binding.setCallback(mDunClickCallback);
        return new DunViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(DunViewHolder holder, int position) {
        holder.binding.setDun(mDunList.get(position));
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return mDunList == null ? 0 : mDunList.size();
    }

    @Override
    public long getItemId(int position) {
        return mDunList.get(position).getId();
    }

    static class DunViewHolder extends RecyclerView.ViewHolder {

        final DunItemBinding binding;

        public DunViewHolder(DunItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
