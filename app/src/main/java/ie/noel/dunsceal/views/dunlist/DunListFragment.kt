/*
 * Copyright 2017, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ie.noel.dunsceal.views.dunlist

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.annotation.Nullable
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.*
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import ie.noel.dunsceal.R
import ie.noel.dunsceal.adapters.DunAdapter
import ie.noel.dunsceal.databinding.FragmentDunListBinding
import ie.noel.dunsceal.models.entity.DunEntity
import ie.noel.dunsceal.models.viewmodel.DunListViewModel
import ie.noel.dunsceal.utils.Loader
import ie.noel.dunsceal.utils.SwipeToDeleteCallback
import ie.noel.dunsceal.utils.SwipeToEditCallback
import ie.noel.dunsceal.views.BaseFragment
import ie.noel.dunsceal.views.BaseView
import ie.noel.dunsceal.views.home.HomePresenter
import ie.noel.dunsceal.views.dun.DunClickCallback
import ie.noel.dunsceal.views.dun.DunView
import ie.noel.dunsceal.views.home.HomeView
import kotlinx.android.synthetic.main.fragment_dun_list.view.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class DunListFragment(val presenter: HomePresenter) : BaseFragment(), AnkoLogger {

  private var mDunAdapter: DunAdapter? = null
  private var mBinding: FragmentDunListBinding? = null

  companion object {
    const val TAG = "DunListFragment"

    @JvmStatic
    fun newInstance(presenter: HomePresenter) =
        DunListFragment(presenter).apply {
          arguments = Bundle().apply { }
        }
  }

  override fun onCreateView(
      inflater: LayoutInflater,
      container: ViewGroup?,
      savedInstanceState: Bundle?
  ): View? {

    super.onCreateView(inflater, container, savedInstanceState)

    // Inflate the layout for this fragment
    mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_dun_list, container, false)
    setHasOptionsMenu(true)

    activity?.title = getString(R.string.action_report)
    loader = Loader.createLoader(activity!!)

    mBinding!!.root.myRecyclerView.layoutManager = LinearLayoutManager(activity)
    setSwipeRefresh()

    // Swipe support
    val swipeDeleteHandler = object : SwipeToDeleteCallback(activity!!) {
      override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val adapter = mBinding!!.root.myRecyclerView.adapter as DunAdapter
        adapter.removeAt(viewHolder.adapterPosition)
        deleteDun((viewHolder.itemView.tag as DunEntity).id.toString())
        deleteUserDun(
            presenter.app.auth.currentUser!!.uid,
            (viewHolder.itemView.tag as DunEntity).id.toString()
        )
      }
    }

    val itemTouchDeleteHelper = ItemTouchHelper(swipeDeleteHandler)
    itemTouchDeleteHelper.attachToRecyclerView(mBinding!!.root.myRecyclerView)

    val swipeEditHandler = object : SwipeToEditCallback(activity!!) {
      override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        onClick(viewHolder.itemView.tag as DunEntity)
      }
    }
    val itemTouchEditHelper = ItemTouchHelper(swipeEditHandler)
    itemTouchEditHelper.attachToRecyclerView(mBinding!!.root.myRecyclerView)

    // with LiveData binding
    mDunAdapter = DunAdapter(mDunClickCallback, false)
    mBinding!!.myRecyclerView.adapter = mDunAdapter

    presenter.loadDuns()

    return mBinding!!.root
  }

  override fun onResume() {
    super.onResume()
    if (this::class == DunListFragment::class)
      getAllDuns(presenter.app.auth.currentUser!!.uid)
  }

  override fun setSwipeRefresh() {
    mBinding!!.root.swipeRefresh.setOnRefreshListener {
      mBinding!!.root.swipeRefresh.isRefreshing = true
      getAllDuns(presenter.app.auth.currentUser!!.uid)
    }
  }

  // Swipe support
  override fun checkSwipeRefresh() {
    if ((root.swipeRefresh != null) && (root.swipeRefresh.isRefreshing))
      root.swipeRefresh.isRefreshing = false
  }

  private fun getAllDuns(userId: String?) {
    loader = Loader.createLoader(activity!!)
    Loader.showLoader(loader, "Downloading Duns from Firebase")
    val duns = ArrayList<DunEntity>()
    // myRecyclerView.adapter = OldDunAdapter(duns, this, false)

    presenter.app.db.child("users").child(userId!!).child("duns").child(userId)
        .addValueEventListener(object : ValueEventListener {
          override fun onCancelled(error: DatabaseError) {
            info("Firebase Dun error : ${error.message}")
          }

          override fun onDataChange(snapshot: DataSnapshot) {
            Loader.hideLoader(loader)
            val children = snapshot.children
            children.forEach {
              val dun = it.getValue<DunEntity>(DunEntity::class.java)

              duns.add(dun!!)
              //     mBinding!!.root.myRecyclerView.adapter =
              //       DunAdapter(duns, this@DunListFragment, false)
              mBinding!!.root.myRecyclerView.adapter?.notifyDataSetChanged()
              checkSwipeRefresh()

              presenter.app.db.child("users").child(userId).child("duns").child(userId)
                  .removeEventListener(this)
            }
          }
        })
  }

  fun onClick(dun: DunEntity) {
    presenter.doEdit(dun)
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

  @Override
  override fun onActivityCreated(@Nullable savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    val viewModel = ViewModelProvider(this).get(DunListViewModel::class.java)

    mBinding!!.dunsSearchBtn.setOnClickListener {
      val query = mBinding!!.dunsSearchBox.text
      if (query == null || query.toString().isEmpty()) {
        subscribeUi(viewModel.duns as LiveData<List<DunEntity>>)
      } else {
        subscribeUi(viewModel.searchDuns("*$query*") as LiveData<List<DunEntity>>)
      }
    }
    subscribeUi(viewModel.duns as LiveData<List<DunEntity>>)
  }

  private fun subscribeUi(liveData: LiveData<List<DunEntity>>) { // Update the list when the data changes
    liveData.observe(viewLifecycleOwner, Observer { myDuns: List<DunEntity>? ->
      if (myDuns != null) {
        mBinding!!.isLoading = false
        mDunAdapter!!.setDunList(myDuns as MutableList<DunEntity>)
      } else {
        mBinding!!.isLoading = true
      }
      // espresso does not know how to wait for data binding's loop so we execute changes
// sync.
      mBinding!!.executePendingBindings()
    })
  }

  private val mDunClickCallback = object : DunClickCallback {
    override fun onClick(dun: DunEntity?) {
      if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
        (activity as HomeView?)!!.showDunFragment(dun!!)
      }
    }
  }
}
