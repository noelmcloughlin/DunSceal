/*
 * Copyright (c) 2019 Razeware LLC
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  Notwithstanding the foregoing, you may not use, copy, modify, merge, publish,
 *  distribute, sublicense, create a derivative work, and/or sell copies of the
 *  Software in any work that is designed, intended, or marketed for pedagogical or
 *  instructional purposes related to programming, coding, application development,
 *  or information technology.  Permission for such use, copying, modification,
 *  merger, publication, distribution, sublicensing, creation of derivative works,
 *  or sale is expressly withheld.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 */

package org.noel.dunsceal.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.card_item.view.*
import org.noel.dunsceal.R
import org.noel.dunsceal.fragments.DunViewFragment
import org.noel.dunsceal.helpers.readImageFromPath
import org.noel.dunsceal.models.DunModel

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
class DunFragmentPagerAdapter(
    private val context: Context,
    private val duns: List<DunModel>,
    private val listener: DunListener,
    fm: FragmentManager
) : FragmentPagerAdapter(fm) {

    fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DunHolder {
        return DunHolder(
            LayoutInflater.from(context).inflate(
                R.layout.card_item,
                parent,
                false
            )
        )
    }

    fun showCheckboxState() {
        var data = ""
        for (j in this.duns.indices) {
            if (this.duns[j].isCompleted) {
                data = data + "\n" + this.duns[j].title
            }
        }
        Toast.makeText(context, "Selected DunModel : \n $data", Toast.LENGTH_SHORT).show()
    }

    fun onBindViewHolder(holder: DunHolder, position: Int) {
        val dun = duns[holder.adapterPosition]
        holder.bind(dun, listener)

        holder.itemView.dunTitle.text = dun.title
        holder.itemView.checkBox.isChecked = dun.isCompleted
        holder.itemView.checkBox.tag = this.duns[position]

        holder.itemView.checkBox.setOnClickListener {
            val duns1 = holder.itemView.checkBox.tag as DunModel

            duns1.isCompleted = holder.itemView.checkBox.isChecked
            this.duns[position].isCompleted = holder.itemView.checkBox.isChecked
            showCheckboxState()
        }
    }

    override fun getItem(position: Int): Fragment {
        // getItem is called to instantiate the fragment for the given page.
        // Return a DunViewFragment (defined as a static inner class below).
        return DunViewFragment.newInstance(duns[position])
    }

    override fun getPageTitle(position: Int): CharSequence? {
        //return context.resources.getString(duns[position])
        return duns[position].title
    }

    override fun getCount(): Int {
        return duns.size
    }

    class DunHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(dun: DunModel, listener: DunListener) {
            itemView.dunTitle.text = dun.title
            itemView.imageIcon.setImageBitmap(readImageFromPath(itemView.context, dun.image))
            itemView.setOnClickListener { listener.onDunClick(dun) }
        }
    }
}