package ie.noel.dunsceal.views.dunlist

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

import ie.noel.dunsceal.R
import ie.noel.dunsceal.adapters.DunListener
import ie.noel.dunsceal.models.entity.DunEntity
import ie.noel.dunsceal.views.BaseFragment
import ie.noel.dunsceal.utils.Loader.createLoader
import ie.noel.dunsceal.utils.Loader.hideLoader
import ie.noel.dunsceal.utils.Loader.showLoader
import ie.noel.dunsceal.views.home.HomePresenter
import kotlinx.android.synthetic.main.fragment_report.view.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

open class ReportFragment(open var presenter: HomePresenter) : BaseFragment(), AnkoLogger,
    DunListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.loadDuns()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_report, container, false)
        activity?.title = getString(R.string.action_report)

        root.myRecyclerView.layoutManager = LinearLayoutManager(activity)
        setSwipeRefresh()

        /* val swipeDeleteHandler = object : SwipeToDeleteCallback(activity!!) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = root.myRecyclerView.adapter as OldDunAdapter
                adapter.removeAt(viewHolder.adapterPosition)
                deleteDun((viewHolder.itemView.tag as DunEntity).id.toString())
                deleteUserDun(
                    presenter.app.auth.currentUser!!.uid,
                    (viewHolder.itemView.tag as DunEntity).id.toString()
                )
            }
        }
        val itemTouchDeleteHelper = ItemTouchHelper(swipeDeleteHandler)
        itemTouchDeleteHelper.attachToRecyclerView(root.myRecyclerView)

        val swipeEditHandler = object : SwipeToEditCallback(activity!!) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                onDunClick(viewHolder.itemView.tag as DunEntity)
            }
        }
        val itemTouchEditHelper = ItemTouchHelper(swipeEditHandler)
        itemTouchEditHelper.attachToRecyclerView(root.myRecyclerView)
*/
        return root
    }

    companion object {
        @JvmStatic
        fun newInstance(presenter: HomePresenter) =
            ReportFragment(presenter).apply {
                arguments = Bundle().apply { }
            }
    }

    open fun setSwipeRefresh() {
        root.swipeRefresh.setOnRefreshListener {
            root.swipeRefresh.isRefreshing = true
            getAllDuns(presenter.app.auth.currentUser!!.uid)
        }
    }

    fun checkSwipeRefresh() {
        if (root.swipeRefresh.isRefreshing) root.swipeRefresh.isRefreshing = false
    }

    override fun onResume() {
        super.onResume()
        if (this::class == ReportFragment::class)
            getAllDuns(presenter.app.auth.currentUser!!.uid)
    }

    private fun getAllDuns(userId: String?) {
        loader = createLoader(activity!!)
        showLoader(loader, "Downloading Duns from Firebase")
        val duns = ArrayList<DunEntity>()
       // myRecyclerView.adapter = OldDunAdapter(duns, this, false)

        presenter.app.db.child("users").child(userId!!).child("duns").child(userId)
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    info("Firebase Dun error : ${error.message}")
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    hideLoader(loader)
                    val children = snapshot.children
                    children.forEach {
                        val dun = it.getValue<DunEntity>(DunEntity::class.java)

                        duns.add(dun!!)
                   //     root.myRecyclerView.adapter =
                     //       OldDunAdapter(duns, this@ReportFragment, false)
                        root.myRecyclerView.adapter?.notifyDataSetChanged()
                        checkSwipeRefresh()

                        presenter.app.db.child("users").child(userId).child("duns").child(userId)
                            .removeEventListener(this)
                    }
                }
            })
    }

    override fun onDunClick(dun: DunEntity) {
        presenter.doEditDun(dun)
    }

    fun deleteUserDun(userId: String, uid: String?) {
        presenter.app.db.child("users").child(userId).child("duns").child(userId).child(uid!!)
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
        presenter.app.db.child("duns").child(uid!!)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        presenter.loadDuns()
        super.onActivityResult(requestCode, resultCode, data)
    }
}