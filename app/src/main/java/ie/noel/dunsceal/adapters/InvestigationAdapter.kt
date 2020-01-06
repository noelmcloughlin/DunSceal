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
package ie.noel.dunsceal.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ie.noel.dunsceal.R
import ie.noel.dunsceal.databinding.InvestigationItemBinding
import ie.noel.dunsceal.models.entity.InvestigationEntity
import ie.noel.dunsceal.views.home.dun.InvestigationClickCallback

class InvestigationAdapter(private val mInvestigationClickCallback: InvestigationClickCallback?

) : RecyclerView.Adapter<InvestigationAdapter.InvestigationViewHolder>() {

  private var mInvestigationList: List<ie.noel.dunsceal.models.Investigation>? = null

  fun setInvestigationList(investigationEntities: List<ie.noel.dunsceal.models.entity.InvestigationEntity>) {
    if (mInvestigationList == null) {
      mInvestigationList = investigationEntities
      notifyItemRangeInserted(0, investigationEntities.size)
    } else {
      val diffResult = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
        override fun getOldListSize(): Int {
          return mInvestigationList!!.size
        }

        override fun getNewListSize(): Int {
          return investigationEntities.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
          val old = mInvestigationList!![oldItemPosition]
          val investigation = investigationEntities[newItemPosition]
          return old.id == investigation.id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
          val old = mInvestigationList!![oldItemPosition]
          val investigation: ie.noel.dunsceal.models.Investigation = investigationEntities[newItemPosition]
          return old.id == investigation.id
              && old.image === investigation.image
              && old.postedAt === investigation.postedAt
              && old.dunId == investigation.dunId
              && old.text == investigation.text
        }
      })
      mInvestigationList = investigationEntities
      diffResult.dispatchUpdatesTo(this)
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InvestigationViewHolder {
    val binding = DataBindingUtil
        .inflate<InvestigationItemBinding>(LayoutInflater.from(parent.context), R.layout.investigation_item,
            parent, false)
    binding.callback = mInvestigationClickCallback
    return InvestigationViewHolder(binding)
  }

  override fun onBindViewHolder(holder: InvestigationViewHolder, position: Int) {
    holder.binding.investigation = mInvestigationList!![position] as InvestigationEntity?
    holder.binding.executePendingBindings()
  }

  override fun getItemCount(): Int {
    return if (mInvestigationList == null) 0 else mInvestigationList!!.size
  }

  class InvestigationViewHolder(val binding: InvestigationItemBinding) : RecyclerView.ViewHolder(binding.root)

}