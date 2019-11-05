package org.noel.dunsceal.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.dun_card.view.*
import org.noel.dunsceal.R
import org.noel.dunsceal.helpers.readImageFromPath
import org.noel.dunsceal.model.DunModel

interface DunListener {
    fun onDunClick(dun: DunModel)
}

class DunAdapter constructor(private var duns: List<DunModel>,
                             private val listener: DunListener
) : RecyclerView.Adapter<DunAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        return MainHolder(
            LayoutInflater.from(parent?.context).inflate(
                R.layout.dun_card,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val dun = duns[holder.adapterPosition]
        holder.bind(dun, listener)
    }

    override fun getItemCount(): Int = duns.size

    class MainHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(dun: DunModel, listener: DunListener) {
            itemView.dunTitle.text = dun.title
            itemView.description.text = dun.description
            itemView.imageIcon.setImageBitmap(readImageFromPath(itemView.context, dun.image))
            itemView.setOnClickListener { listener.onDunClick(dun) }
        }
    }
}