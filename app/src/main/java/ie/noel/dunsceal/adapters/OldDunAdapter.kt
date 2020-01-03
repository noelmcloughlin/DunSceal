package ie.noel.dunsceal.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import ie.noel.dunsceal.R
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import kotlinx.android.synthetic.main.card_dun.view.*
import com.squareup.picasso.Picasso
import ie.noel.dunsceal.models.entity.DunEntity

interface DunListener {
    fun onDunClick(dun: DunEntity)
}

class OldDunAdapter constructor(
    var duns: ArrayList<DunEntity>,
    private val listener: DunListener,
    reportall: Boolean
) : RecyclerView.Adapter<OldDunAdapter.MainHolder>() {

    private val reportAll = reportall

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        return MainHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.card_dun,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val dun = duns[holder.adapterPosition]
        holder.bind(dun, listener, reportAll)
    }

    override fun getItemCount(): Int = duns.size

    fun removeAt(position: Int) {
        duns.removeAt(position)
        notifyItemRemoved(position)
    }

    class MainHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(dun: DunEntity, listener: DunListener, reportAll: Boolean) {
            itemView.tag = dun
            itemView.upvote.text = dun.amount.toString()
            itemView.visited.text = dun.paymenttype

            if (!reportAll)
                itemView.setOnClickListener { listener.onDunClick(dun) }

            if (dun.profilepic!!.isNotEmpty()) {
                Picasso.get().load(dun.profilepic!!.toUri())
                    //.resize(180, 180)
                    .transform(CropCircleTransformation())
                    .into(itemView.imageIcon)
            } else
                itemView.imageIcon.setImageResource(R.mipmap.img_hillfort_default)
        }
    }
}