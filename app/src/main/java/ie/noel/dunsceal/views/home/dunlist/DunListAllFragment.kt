package ie.noel.dunsceal.views.home.dunlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import ie.noel.dunsceal.R
import ie.noel.dunsceal.adapters.DunAdapter
import ie.noel.dunsceal.databinding.FragmentDunListBinding
import ie.noel.dunsceal.models.entity.DunEntity
import ie.noel.dunsceal.utils.Loader
import ie.noel.dunsceal.utils.Loader.createLoader
import ie.noel.dunsceal.utils.Loader.hideLoader
import ie.noel.dunsceal.utils.Loader.showLoader
import ie.noel.dunsceal.views.BaseFragment
import ie.noel.dunsceal.views.BasePresenter
import ie.noel.dunsceal.views.home.HomePresenter
import kotlinx.android.synthetic.main.fragment_dun_list.view.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

interface DunListener {
  fun onDunClick(dun: DunEntity)
}

class DunListAllFragment(var presenter: BasePresenter) : BaseFragment(), AnkoLogger {

  private var mBinding: FragmentDunListBinding? = null

  companion object {
    @JvmStatic
    fun newInstance(presenter: BasePresenter) =
        DunListAllFragment(presenter).apply {
          arguments = Bundle().apply { }
        }
  }

  override fun onCreateView(
      inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?

  ): View? {

    // Inflate the layout for this fragment
    mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_dun_list, container, false)
    activity?.title = getString(R.string.action_report_all)
    loader = createLoader(activity!!)

    mBinding!!.root.myRecyclerView.layoutManager = LinearLayoutManager(activity)
    setSwipeRefresh()

    return mBinding!!.root
  }

  override fun setSwipeRefresh() {
    mBinding!!.root.swipeRefresh.setOnRefreshListener {
      mBinding!!.root.swipeRefresh.isRefreshing = true
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
    val dunsList = ArrayList<DunEntity>()
    presenter.app.db.child("duns")
        .addValueEventListener(object : ValueEventListener {
          override fun onCancelled(error: DatabaseError) {
            info("Firebase Dun error : ${error.message}")
          }

          override fun onDataChange(snapshot: DataSnapshot) {
            hideLoader(loader)
            val children = snapshot.children
            children.forEach {
              val dun = it.getValue<DunEntity>(DunEntity::class.java)

              dunsList.add(dun!!)
              // root.myRecyclerView.adapter =
              //       OldDunAdapter(dunsList, this@DunListAllFragment, true)
              mBinding!!.root.myRecyclerView.adapter?.notifyDataSetChanged()
              checkSwipeRefresh()

              presenter.app.db.child("duns").removeEventListener(this)
            }
          }
        })
  }
}