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
import ie.noel.dunsceal.databinding.ContentItemBinding
import ie.noel.dunsceal.models.Content
import ie.noel.dunsceal.models.entity.ContentEntity
import ie.noel.dunsceal.views.BaseClickCallback

open class ContentAdapter(var content: ArrayList<ContentEntity>,
                          private val mBaseClickCallback: BaseClickCallback?,
                          reportAll: Boolean

) : RecyclerView.Adapter<ContentAdapter.ContentViewHolder>() {

  open val reportAll = reportAll
  open var mContentList: List<Content>? = null

  fun setContentList(content: List<ContentEntity>) {
    if (mContentList == null) {
      mContentList = content
      notifyItemRangeInserted(0, content.size)
    } else {
      val diffResult = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
        override fun getOldListSize(): Int {
          return mContentList!!.size
        }

        override fun getNewListSize(): Int {
          return content.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
          val old = mContentList!![oldItemPosition]
          val content = content[newItemPosition]
          return old.id == content.id
        }

        override fun areContentTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
          val old = mContentList!![oldItemPosition]
          val content: Content = content[newItemPosition]
          return old.id == content.id && old.postedAt === content.postedAt && old.parentId == content.parentId && old.text == content.text
        }
      })
      mContentList = content
      diffResult.dispatchUpdatesTo(this)
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContentViewHolder {
    val binding = DataBindingUtil
        .inflate<ContentItemBinding>(LayoutInflater.from(parent.context), R.layout.content_item,
            parent, false)
    binding.callback = mBaseClickCallback
    return ContentViewHolder(binding)
  }

  override fun onBindViewHolder(holder: ContentViewHolder, position: Int) {
    holder.binding.content = mContentList!![position]
    holder.binding.executePendingBindings()
  }

  override fun getItemCount(): Int {
    return if (mContentList == null) 0 else mContentList!!.size
  }

  class ContentViewHolder(val binding: ContentItemBinding) : RecyclerView.ViewHolder(binding.root)

}