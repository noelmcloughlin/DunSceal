package org.noel.dunsceal.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.card_item.view.*
import kotlinx.android.synthetic.main.card_item.view.dunTitle
import org.noel.dunsceal.R
import org.noel.dunsceal.helpers.readImageFromPath
import org.noel.dunsceal.models.DunModel

interface DunListener {
    fun onDunClick(dun: DunModel)
}

class DunRecycleViewAdapter constructor(
    private var context: Context,
    private var duns: List<DunModel>,
    private val listener: DunListener
) : RecyclerView.Adapter<DunRecycleViewAdapter.DunHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DunHolder {
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

    override fun onBindViewHolder(holder: DunHolder, position: Int) {
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

    override fun getItemCount(): Int = duns.size

    class DunHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(dun: DunModel, listener: DunListener) {
            itemView.dunTitle.text = dun.title
            itemView.imageIcon.setImageBitmap(readImageFromPath(itemView.context, dun.image))
            itemView.setOnClickListener { listener.onDunClick(dun) }
        }
    }
}