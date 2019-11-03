package org.noel.dunsceal.activities

import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.noel.dunsceal.R
import org.noel.dunsceal.adapters.UsersRecyclerAdapter
import org.noel.dunsceal.model.DunScealUser
import org.noel.dunsceal.helpers.DatabaseHelper


class UsersListActivity : AppCompatActivity() {

    private val activity = this@UsersListActivity
    private lateinit var textViewName: AppCompatTextView
    private lateinit var recyclerViewUsers: RecyclerView
    private lateinit var listUsers: MutableList<DunScealUser>
    private lateinit var usersRecyclerAdapter: org.noel.dunsceal.adapters.UsersRecyclerAdapter
    private lateinit var databaseHelper: org.noel.dunsceal.helpers.DatabaseHelper

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
        usersRecyclerAdapter = org.noel.dunsceal.adapters.UsersRecyclerAdapter(listUsers)

        val mLayoutManager = LinearLayoutManager(applicationContext)
        recyclerViewUsers.layoutManager = mLayoutManager
        recyclerViewUsers.itemAnimator = DefaultItemAnimator()
        recyclerViewUsers.setHasFixedSize(true)
        recyclerViewUsers.adapter = usersRecyclerAdapter
        databaseHelper = org.noel.dunsceal.helpers.DatabaseHelper(activity)

        val emailFromIntent = intent.getStringExtra("EMAIL")
        textViewName.text = emailFromIntent

        var getDataFromSQLite = GetDataFromSQLite()
        getDataFromSQLite.execute()
    }

    /**
     * This class is to fetch all user records from SQLite
     */
    inner class GetDataFromSQLite : AsyncTask<Void, Void, List<DunScealUser>>() {

        override fun doInBackground(vararg p0: Void?): List<DunScealUser> {
            return databaseHelper.getAllUser()
        }

        override fun onPostExecute(result: List<DunScealUser>?) {
            super.onPostExecute(result)
            listUsers.clear()
            listUsers.addAll(result!!)
        }

    }
}