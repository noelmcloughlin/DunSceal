package org.noel.dunsceal.adapters.auth

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import org.noel.dunsceal.R
import org.noel.dunsceal.model.DunUser

class UsersRecyclerAdapter(private val listUsers: List<DunUser>) : RecyclerView.Adapter<UsersRecyclerAdapter.UserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        // inflating recycler item view
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.user_item_recycler, parent, false)

        return UserViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.textViewName.text = listUsers[position].name
        holder.textViewEmail.text = listUsers[position].email
        holder.textViewPassword.text = listUsers[position].password
    }

    override fun getItemCount(): Int {
        return listUsers.size
    }


    /**
     * ViewHolder class
     */
    inner class UserViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var textViewName: AppCompatTextView
        var textViewEmail: AppCompatTextView
        var textViewPassword: AppCompatTextView

        init {
            textViewName = view.findViewById<View>(R.id.textViewName) as AppCompatTextView
            textViewEmail = view.findViewById<View>(R.id.textViewEmail) as AppCompatTextView
            textViewPassword = view.findViewById<View>(R.id.textViewPassword) as AppCompatTextView
        }
    }


}