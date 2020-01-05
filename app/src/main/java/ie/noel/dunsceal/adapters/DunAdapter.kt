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
import ie.noel.dunsceal.databinding.DunCardBinding
import ie.noel.dunsceal.models.Dun
import ie.noel.dunsceal.models.entity.DunEntity
import ie.noel.dunsceal.views.BaseClickCallback
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import kotlinx.android.synthetic.main.dun_card.view.*

class DunAdapter constructor(private val mBaseClickCallback: BaseClickCallback?,
                             reportAll: Boolean

) : RecyclerView.Adapter<DunAdapter.DunViewHolder>() {

  private val reportAll = reportAll
  var mDunList: MutableList<Dun>? = null

  fun setDunList(dunList: MutableList<Dun>) {
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

        override fun areContentTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
          val newDun = dunList[newItemPosition]
          val oldDun = mDunList!![oldItemPosition]
          return (newDun.id == oldDun.id && newDun.description == oldDun.description
              && newDun.name == oldDun.name
              && newDun.votes === oldDun.votes)
        }
      })
      mDunList = dunList
      result.dispatchUpdatesTo(this)
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DunViewHolder {
    val binding = DataBindingUtil
        .inflate<DunCardBinding>(LayoutInflater.from(parent.context), R.layout.dun_card,
            parent, false)
    if (!this.reportAll) {
      binding.callback = mBaseClickCallback
    }
    return DunViewHolder(binding)
  }

  override fun onBindViewHolder(holder: DunViewHolder, position: Int) {
    holder.binding.dun = mDunList!![position]
    holder.binding.executePendingBindings()
  }

  override fun getItemCount(): Int { return if (mDunList == null) 0 else mDunList!!.size }

  //swipe delete feature
  fun removeAt(position: Int) {
    mDunList!!.removeAt(position)
    notifyItemRemoved(position)
  }

  override fun getItemId(position: Int): Long {
    return mDunList!![position].id.toLong()
  }

  class DunViewHolder constructor(val binding: DunCardBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(dun: DunEntity) {
      binding.root.tag = dun
      binding.root.visited.text = dun.visited.toString()

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