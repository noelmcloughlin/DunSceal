package ie.noel.dunsceal.views.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

import ie.noel.dunsceal.R
import ie.noel.dunsceal.main.MainApp
import ie.noel.dunsceal.models.DunModel
import ie.noel.dunsceal.utils.Loader.createLoader
import ie.noel.dunsceal.utils.Loader.hideLoader
import ie.noel.dunsceal.utils.Loader.showLoader
import ie.noel.dunsceal.utils.SwipeToDeleteCallback
import ie.noel.dunsceal.utils.SwipeToEditCallback
import ie.noel.dunsceal.views.dunlist.DunAdapter
import ie.noel.dunsceal.views.dunlist.DunListener
import ie.noel.dunsceal.views.login.LoginPresenter
import kotlinx.android.synthetic.main.fragment_report.view.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

open class ReportFragment : Fragment(), AnkoLogger, DunListener {

    lateinit var app: MainApp
    lateinit var loader : AlertDialog
    lateinit var root: View
    private lateinit var presenter: LoginPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = activity?.application as MainApp
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_report, container, false)
        activity?.title = getString(R.string.action_report)

        root.recyclerView.layoutManager = LinearLayoutManager(activity)
        setSwipeRefresh()

        val swipeDeleteHandler = object : SwipeToDeleteCallback(activity!!) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = root.recyclerView.adapter as DunAdapter
                adapter.removeAt(viewHolder.adapterPosition)
                deleteDun((viewHolder.itemView.tag as DunModel).id.toString())
                deleteUserDun(app.auth.currentUser!!.uid,
                    (viewHolder.itemView.tag as DunModel).id.toString())
            }
        }
        val itemTouchDeleteHelper = ItemTouchHelper(swipeDeleteHandler)
        itemTouchDeleteHelper.attachToRecyclerView(root.recyclerView)

        val swipeEditHandler = object : SwipeToEditCallback(activity!!) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                onDunClick(viewHolder.itemView.tag as DunModel)
            }
        }
        val itemTouchEditHelper = ItemTouchHelper(swipeEditHandler)
        itemTouchEditHelper.attachToRecyclerView(root.recyclerView)

        return root
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            ReportFragment().apply {
                arguments = Bundle().apply { }
            }
    }

    open fun setSwipeRefresh() {
        root.swiperefresh.setOnRefreshListener {
            root.swiperefresh.isRefreshing = true
            getAllDuns(app.auth.currentUser!!.uid)
        }
    }

    fun checkSwipeRefresh() {
        if (root.swiperefresh.isRefreshing) root.swiperefresh.isRefreshing = false
    }

    fun deleteUserDun(userId: String, uid: String?) {
        app.db.child("user-duns").child(userId).child(uid!!)
            .addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        snapshot.ref.removeValue()
                    }
                    override fun onCancelled(error: DatabaseError) {
                        info("Firebase Dun error : ${error.message}")
                    }
                })
    }

    fun deleteDun(uid: String?) {
        app.db.child("duns").child(uid!!)
            .addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        snapshot.ref.removeValue()
                    }

                    override fun onCancelled(error: DatabaseError) {
                        info("Firebase Dun error : ${error.message}")
                    }
                })
    }

    override fun onDunClick(dun: DunModel) {
        activity!!.supportFragmentManager.beginTransaction()
            .replace(R.id.homeFrame, EditFragment.newInstance(dun))
            .addToBackStack(null)
            .commit()
    }

    override fun onResume() {
        super.onResume()
        if(this::class == ReportFragment::class)
            getAllDuns(app.auth.currentUser!!.uid)
    }

    private fun getAllDuns(userId: String?) {
        loader = createLoader(activity!!)
        showLoader(loader, "Downloading Duns from Firebase")
        val dunsList = ArrayList<DunModel>()
        app.db.child("user-duns").child(userId!!)
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    info("Firebase Dun error : ${error.message}")
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    hideLoader(loader)
                    val children = snapshot.children
                    children.forEach {
                        val dun = it.
                            getValue<DunModel>(DunModel::class.java)

                        dunsList.add(dun!!)
                        root.recyclerView.adapter =
                            DunAdapter(dunsList, this@ReportFragment,false)
                        root.recyclerView.adapter?.notifyDataSetChanged()
                        checkSwipeRefresh()

                        app.db.child("user-duns").child(userId)
                            .removeEventListener(this)
                    }
                }
            })
    }
}