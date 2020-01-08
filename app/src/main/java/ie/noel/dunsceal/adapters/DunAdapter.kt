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
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ie.noel.dunsceal.R
import ie.noel.dunsceal.R.mipmap.img_hillfort_default_round
import ie.noel.dunsceal.databinding.DunItemBinding
import ie.noel.dunsceal.models.entity.DunEntity
import ie.noel.dunsceal.views.home.dun.DunClickCallback
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import kotlinx.android.synthetic.main.dun_item.view.*

class DunAdapter constructor(private val mDunClickCallback: DunClickCallback?,
                             private val reportAll: Boolean

) : RecyclerView.Adapter<DunAdapter.DunViewHolder>() {

  var mDunList: MutableList<DunEntity>? = null

  fun setDunList(dunList: MutableList<DunEntity>) {
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
          return (newDun.id == oldDun.id
              && newDun.description == oldDun.description
              && newDun.name == oldDun.name
              && newDun.votes == oldDun.votes
              && newDun.visited == oldDun.visited
              && newDun.image === oldDun.image)
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
    if (!this.reportAll) {
      binding.callback = mDunClickCallback
    }
    return DunViewHolder(binding)
  }

  override fun onBindViewHolder(holder: DunViewHolder, position: Int) {
    holder.binding.dun = mDunList!![position]
    with(holder) {
      bind(binding.dun!!)
      //binding.executePendingBindings()
    }
  }

  override fun getItemCount(): Int { return if (mDunList == null) 0 else mDunList!!.size }

  //swipe delete feature
  fun removeAt(position: Int) {
    mDunList!!.removeAt(position)
    notifyItemRemoved(position)
  }

  override fun getItemId(position: Int): Long {
    return mDunList!![position].id
  }

  class DunViewHolder constructor(val binding: DunItemBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(dun: DunEntity) {
      binding.root.tag = dun
      binding.root.name.text = dun.name
      binding.root.description.text = dun.description
      binding.root.votes.text = dun.votes.toString()
      if (dun.visited > 0) {
        binding.root.visited.text = R.string.yes.toString()
      } else {
        binding.root.visited.text = R.string.no.toString()
      }

      if(dun.image.isNotEmpty()) {
        Picasso.get().load(dun.image.toUri())
            //.resize(180, 180)
            .transform(CropCircleTransformation())
            .into(binding.root.imageIcon)
      }
      else
        itemView.imageIcon.setImageResource(img_hillfort_default_round)
    }
  }

  init {
    setHasStableIds(true)
  }
}