package org.noel.dunsceal.activity

import android.os.AsyncTask
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.startActivityForResult
import org.noel.dunsceal.R
import org.noel.dunsceal.adapters.auth.UsersRecycleViewAdapter
import org.noel.dunsceal.models.DunUser
import org.noel.dunsceal.datasource.local.UserDatabase

class DunUserActivity : AppCompatActivity() {

    private val activity = this@DunUserActivity
    private lateinit var textViewName: AppCompatTextView
    private lateinit var recyclerViewUsers: RecyclerView
    private lateinit var listUsers: MutableList<DunUser>
    private lateinit var usersRecycleViewAdapter: UsersRecycleViewAdapter
    private lateinit var userDatabase: UserDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_list_activity)
        if (supportActionBar != null)
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
        usersRecycleViewAdapter = UsersRecycleViewAdapter(listUsers)

        val mLayoutManager = LinearLayoutManager(applicationContext)
        recyclerViewUsers.layoutManager = mLayoutManager
        recyclerViewUsers.itemAnimator = DefaultItemAnimator()
        recyclerViewUsers.setHasFixedSize(true)
        recyclerViewUsers.adapter = usersRecycleViewAdapter
        userDatabase = UserDatabase(activity)

        val emailFromIntent = intent.getStringExtra("EMAIL")
        textViewName.text = emailFromIntent

        var getDataFromSQLite = GetDataFromSQLite()
        getDataFromSQLite.execute()
    }

    /**
     * This class is to fetch all user records from SQLite
     */
    inner class GetDataFromSQLite : AsyncTask<Void, Void, List<DunUser>>() {

        override fun doInBackground(vararg p0: Void?): List<DunUser> {
            return userDatabase.getAllUser()
        }

        override fun onPostExecute(result: List<DunUser>?) {
            super.onPostExecute(result)
            listUsers.clear()
            listUsers.addAll(result!!)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_dun_cancel, menu)
        menuInflater.inflate(R.menu.menu_dun_list, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_dun_add -> startActivityForResult<DunActivity>(0)
            R.id.action_dun_list-> startActivity<DunListActivity>()
            R.id.action_dun_users -> startActivity<DunUserActivity>()
            else -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}