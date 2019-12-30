package ie.noel.dunsceal.views.dunlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import ie.noel.dunsceal.adapters.DunAdapter
import ie.noel.dunsceal.R
import ie.noel.dunsceal.adapters.DunListener
import ie.noel.dunsceal.models.DunModel
import ie.noel.dunsceal.utils.Loader.createLoader
import ie.noel.dunsceal.utils.Loader.hideLoader
import ie.noel.dunsceal.utils.Loader.showLoader
import kotlinx.android.synthetic.main.fragment_report.view.*
import org.jetbrains.anko.info

class ReportAllFragment(override var presenter: HomePresenter) : ReportFragment(presenter),
    DunListener {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_report, container, false)
        activity?.title = getString(R.string.menu_report_all)
        root.myRecyclerView.layoutManager = LinearLayoutManager(activity)
        setSwipeRefresh()

        return root
    }

    companion object {
        @JvmStatic
        fun newInstance(presenter: HomePresenter) =
            ReportAllFragment(presenter).apply {
                arguments = Bundle().apply { }
            }
    }

    override fun setSwipeRefresh() {
        root.swipeRefresh.setOnRefreshListener {
            root.swipeRefresh.isRefreshing = true
            getAllUsersDuns()
        }
    }

    override fun onResume() {
        super.onResume()
        getAllUsersDuns()
    }

    private fun getAllUsersDuns() {
        loader = createLoader(activity!!)
        showLoader(loader, "Downloading All Users Duns from Firebase")
        val dunsList = ArrayList<DunModel>()
        presenter.app.db.child("duns")
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    info("Firebase Dun error : ${error.message}")
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    hideLoader(loader)
                    val children = snapshot.children
                    children.forEach {
                        val dun = it.getValue<DunModel>(DunModel::class.java)

                        dunsList.add(dun!!)
                        root.myRecyclerView.adapter =
                            DunAdapter(dunsList, this@ReportAllFragment, true)
                        root.myRecyclerView.adapter?.notifyDataSetChanged()
                        checkSwipeRefresh()

                        presenter.app.db.child("duns").removeEventListener(this)
                    }
                }
            })
    }
}