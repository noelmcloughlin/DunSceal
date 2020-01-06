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
import ie.noel.dunsceal.databinding.NoteItemBinding
import ie.noel.dunsceal.models.entity.NoteEntity
import ie.noel.dunsceal.views.home.dun.NoteClickCallback

class NoteAdapter(private val mNoteClickCallback: NoteClickCallback?) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

  private var mNoteList: List<ie.noel.dunsceal.models.Note>? = null

  fun setNoteList(noteEntities: List<NoteEntity>) {
    if (mNoteList == null) {
      mNoteList = noteEntities
      notifyItemRangeInserted(0, noteEntities.size)
    } else {
      val diffResult = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
        override fun getOldListSize(): Int {
          return mNoteList!!.size
        }

        override fun getNewListSize(): Int {
          return noteEntities.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
          val old = mNoteList!![oldItemPosition]
          val note = noteEntities[newItemPosition]
          return old.id == note.id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
          val old = mNoteList!![oldItemPosition]
          val note: ie.noel.dunsceal.models.Note = noteEntities[newItemPosition]
          return old.id == note.id
              && old.image === note.image
              && old.postedAt === note.postedAt
              && old.dunId == note.dunId
              && old.text == note.text
        }
      })
      mNoteList = noteEntities
      diffResult.dispatchUpdatesTo(this)
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
    val binding = DataBindingUtil
        .inflate<NoteItemBinding>(LayoutInflater.from(parent.context), R.layout.note_item,
            parent, false)
    binding.callback = mNoteClickCallback
    return NoteViewHolder(binding)
  }

  override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
    holder.binding.note = mNoteList!![position] as NoteEntity?
    holder.binding.executePendingBindings()
  }

  override fun getItemCount(): Int {
    return if (mNoteList == null) 0 else mNoteList!!.size
  }

  class NoteViewHolder(val binding: NoteItemBinding) : RecyclerView.ViewHolder(binding.root)

}