package com.noel.dunsceal.activities

import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.noel.dunsceal.R
import com.noel.dunsceal.adapters.UsersRecyclerAdapter
import com.noel.dunsceal.model.User
import com.noel.dunsceal.sql.DatabaseHelper


class UsersListActivity : AppCompatActivity() {

    private val activity = this@UsersListActivity
    private lateinit var textViewName: AppCompatTextView
    private lateinit var recyclerViewUsers: RecyclerView
    private lateinit var listUsers: MutableList<User>
    private lateinit var usersRecyclerAdapter: UsersRecyclerAdapter
    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_users_list)
        supportActionBar!!.title = ""
        initViews()
        initObjects()

    }

    /**
     * This method is to initialize views
     */
    private fun initViews() {
        textViewName = findViewById<View>(R.id.textViewName) as AppCompatTextView
        recyclerViewUsers = findViewById<View>(R.id.recyclerViewUsers) as RecyclerView
    }

    /**
     * This method is to initialize objects to be used
     */
    private fun initObjects() {
        listUsers = ArrayList()
        usersRecyclerAdapter = UsersRecyclerAdapter(listUsers)

        val mLayoutManager = LinearLayoutManager(applicationContext)
        recyclerViewUsers.layoutManager = mLayoutManager
        recyclerViewUsers.itemAnimator = DefaultItemAnimator()
        recyclerViewUsers.setHasFixedSize(true)
        recyclerViewUsers.adapter = usersRecyclerAdapter
        databaseHelper = DatabaseHelper(activity)

        val emailFromIntent = intent.getStringExtra("EMAIL")
        textViewName.text = emailFromIntent

        var getDataFromSQLite = GetDataFromSQLite()
        getDataFromSQLite.execute()
    }

    /**
     * This class is to fetch all user records from SQLite
     */
    inner class GetDataFromSQLite : AsyncTask<Void, Void, List<User>>() {

        override fun doInBackground(vararg p0: Void?): List<User> {
            return databaseHelper.getAllUser()
        }

        override fun onPostExecute(result: List<User>?) {
            super.onPostExecute(result)
            listUsers.clear()
            listUsers.addAll(result!!)
        }

    }
}