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
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ie.noel.dunsceal.R
import ie.noel.dunsceal.databinding.DunItemBinding
import ie.noel.dunsceal.models.Dun
import ie.noel.dunsceal.models.entity.DunEntity
import ie.noel.dunsceal.views.dun.DunClickCallback

interface DunListener {
  fun onDunClick(dun: DunEntity)
}

class DunAdapter(private val mDunClickCallback: DunClickCallback?) : RecyclerView.Adapter<DunAdapter.DunViewHolder>() {

  var mDunList: List<Dun>? = null
  //private val reportAll = reportall

  fun setDunList(dunList: List<Dun>) {
    if (mDunList == null) {
      mDunList = dunList
      notifyItemRangeInserted(0, dunList.size)
    } else {
      val result = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
        override fun getOldListSize(): Int {
          return mDunList!!.size
        }

        override fun getNewListSize(): Int {
          return dunList.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
          return mDunList!![oldItemPosition].id ==
              dunList[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
          val newDun = dunList[newItemPosition]
          val oldDun = mDunList!![oldItemPosition]
          return (newDun.id == oldDun.id && newDun.description == oldDun.description
              && newDun.name == oldDun.name
              && newDun.price === oldDun.price)
        }
      })
      mDunList = dunList
      result.dispatchUpdatesTo(this)
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DunViewHolder {
    val binding = DataBindingUtil
        .inflate<DunItemBinding>(LayoutInflater.from(parent.context), R.layout.dun_item,
            parent, false)
    binding.callback = mDunClickCallback
    return DunViewHolder(binding)
  }

  override fun onBindViewHolder(holder: DunViewHolder, position: Int) {
    holder.binding.dun = mDunList!![position]
    //     holder.bind(dun, listener, reportAll)
    holder.binding.executePendingBindings()
  }

  override fun getItemCount(): Int {
    return if (mDunList == null) 0 else mDunList!!.size
  }

  /* // swipe delete feature
  fun removeAt(position: Int) {
    mDunList.removeAt(position)
    notifyItemRemoved(position)
  } */

  override fun getItemId(position: Int): Long {
    return mDunList!![position].id.toLong()
  }

  class DunViewHolder(val binding: DunItemBinding) : RecyclerView.ViewHolder(binding.root)

  init {
    setHasStableIds(true)
  }
}