package ie.noel.dunsceal.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ie.noel.dunsceal.R
import ie.noel.dunsceal.models.DunModel
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import kotlinx.android.synthetic.main.card_dun.view.*

interface DunListener {
    fun onDunClick(dun: DunModel)
}

class DunAdapter constructor(
    var duns: ArrayList<DunModel>,
    private val listener: DunListener,
    reportall: Boolean
) : RecyclerView.Adapter<DunAdapter.MainHolder>() {

    private val reportAll = reportall

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        return MainHolder(
            LayoutInflater.from(parent?.context).inflate(
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

        fun bind(dun: DunModel, listener: DunListener, reportAll: Boolean) {
            itemView.tag = dun
            itemView.paymentamount.text = dun.amount.toString()
            itemView.paymentmethod.text = dun.paymenttype

            if (!reportAll)
                itemView.setOnClickListener { listener.onDunClick(dun) }

            if (!dun.profilepic.isEmpty()) {
                Picasso.get().load(dun.profilepic.toUri())
                    //.resize(180, 180)
                    .transform(CropCircleTransformation())
                    .into(itemView.imageIcon)
            } else
                itemView.imageIcon.setImageResource(R.mipmap.ic_launcher_homer_round)
        }
    }
}