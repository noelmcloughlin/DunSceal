package ie.noel.dunsceal.views.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

import ie.noel.dunsceal.R
import ie.noel.dunsceal.models.entity.DunEntity
import ie.noel.dunsceal.utils.Loader.createLoader
import ie.noel.dunsceal.utils.Loader.hideLoader
import ie.noel.dunsceal.utils.Loader.showLoader
import ie.noel.dunsceal.views.fragment.BaseFragment
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*

import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import java.util.HashMap

open class HomeFragment(private var presenter: HomePresenter, private val user: String)
    : BaseFragment(), AnkoLogger {

    private lateinit var eventListener: ValueEventListener

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val rootView = inflater.inflate(R.layout.fragment_home, container, false)
        //rootView.findViewById<RecyclerView>(R.id.myRecyclerView).layoutManager =
          //  LinearLayoutManager(context, RecyclerView.VERTICAL, false)

        loader = createLoader(activity!!)
        activity?.title = getString(R.string.action_dun)
        rootView.homeTitle.text = getString(R.string.homeTitle)
        rootView.progressBar.max = 10000
        return rootView
    }

    companion object {
        @JvmStatic
        fun newInstance(presenter: HomePresenter, user: String): HomeFragment {
            return HomeFragment(presenter, user).apply {
                arguments = Bundle().apply {}
            }
        }
    }

    override fun onResume() {
        super.onResume()
        getTotalDone(presenter.app.auth.currentUser?.uid)
    }

    override fun onPause() {
        super.onPause()
        val userId = presenter.app.auth.uid
        if (presenter.app.auth.uid != null)
        presenter.app.db.child("users").child(userId!!).child("duns")
                .child(presenter.app.auth.currentUser!!.uid)
                .removeEventListener(eventListener)
    }

    private fun writeNewDun(dun: DunEntity) {
        // Create new dun at /duns & /duns/$uid
        showLoader(loader, "Adding Dun to Firebase")
        info("Firebase DB Reference : ${presenter.app}.database")
        val uid = presenter.app.auth.currentUser!!.uid
        val key = presenter.app.db.child("duns").push().key!!.toInt()
        if (key == null) {
            info("Firebase Error : Key Empty")
            return
        }
        dun.id = key
        val dunValues = dun.toMap()

        val childUpdates = HashMap<String, Any>()
        childUpdates["/duns/$key"] = dunValues
        childUpdates["/user-duns/$uid/$key"] = dunValues

        presenter.app.db.updateChildren(childUpdates)
        hideLoader(loader)
    }

    private fun getTotalDone(userId: String?) {
        eventListener = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                info("Firebase Dun error : ${error.message}")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                totalDone = 0
                val children = snapshot.children
                children.forEach {
                    val dun = it.getValue<DunEntity>(DunEntity::class.java)
                    totalDone += dun!!.amount!!
                }
                if (progressBar != null)
                    progressBar.progress = totalDone
                if (totalSoFar != null)
                    totalSoFar.text = java.lang.String.format("$ ${this@HomeFragment.totalDone}")
            }
        }
        presenter.fetchData()
        if (presenter.isUserLoggedIn()) {
            presenter.app.db.child("users").child(presenter.app.userId).child("duns")
                .addValueEventListener(eventListener)
        }
    }
}