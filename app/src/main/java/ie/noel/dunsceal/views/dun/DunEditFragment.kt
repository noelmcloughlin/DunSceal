package ie.noel.dunsceal.views.dunlist

/* import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

import ie.noel.dunsceal.R
import ie.noel.dunsceal.main.MainApp
import ie.noel.dunsceal.models.entity.Dun
import ie.noel.dunsceal.utils.Loader.createLoader
import ie.noel.dunsceal.utils.Loader.hideLoader
import ie.noel.dunsceal.utils.Loader.showLoader
import ie.noel.dunsceal.views.BaseFragment
import ie.noel.dunsceal.views.nav_drawer_home.HomePresenter
import kotlinx.android.synthetic.main.fragment_edit.view.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class EditFragment(override var presenter: HomePresenter) : BaseFragment(presenter), AnkoLogger {

  lateinit var app: MainApp
  private var editDun: DunEntity? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    arguments?.let {
      editDun = it.getParcelable("editdun")
    }
  }

  override fun onCreateView(
      inflater: LayoutInflater,
      container: ViewGroup?,
      savedInstanceState: Bundle?
  ): View? {
    // Inflate the layout for this fragment
    root = inflater.inflate(R.layout.fragment_edit, container, false)

    activity?.title = getString(R.string.action_edit)
    loader = createLoader(activity!!)

    root.editAmount.setText(editDun!!.amount.toString())
    root.editPaymenttype.text = editDun!!.visited
    root.editMessage.setText(editDun!!.message)
    root.editUpvotes.setText(editDun!!.votes.toString())
    root.editUpdateButton.setOnClickListener {
      showLoader(loader, "Updating Dun on Server...")
      updateDunData()
      updateDun(editDun!!.id.toString(), editDun!!)
      updateUserDun(app.auth.currentUser!!.uid,
          editDun!!.id.toString(), editDun!!)
    }
    return root
  }

  companion object {
    @JvmStatic
    fun newInstance(presenter: HomePresenter, dun: DunEntity) =
        EditFragment(presenter).apply {
          arguments = Bundle().apply {
            putParcelable("editdun",dun)
          }
        }
  }

  private fun updateDunData() {
    editDun!!.amount = root.editAmount.text.toString().toInt()
    editDun!!.message = root.editMessage.text.toString()
    editDun!!.votes = root.editUpvotes.text.toString().toInt()
  }

  private fun updateUserDun(userId: String, uid: String?, dun: DunEntity) {
    app.db.child("users").child(userId).child("duns").child(userId).child(uid!!)
        .addListenerForSingleValueEvent(
            object : ValueEventListener {
              override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.ref.setValue(dun)
                activity!!.supportFragmentManager.beginTransaction()
                    .replace(R.id.content_home_frame,
                        DunListFragment.newInstance(presenter)
                    )
                    .addToBackStack(null)
                    .commit()
                hideLoader(loader)
              }

              override fun onCancelled(error: DatabaseError) {
                info("Firebase Dun error : ${error.message}")
              }
            })
  }

  private fun updateDun(uid: String?, dun: DunEntity) {
    app.db.child("duns").child(uid!!)
        .addListenerForSingleValueEvent(
            object : ValueEventListener {
              override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.ref.setValue(dun)
                hideLoader(loader)
              }

              override fun onCancelled(error: DatabaseError) {
                info("Firebase Dun error : ${error.message}")
              }
            })
  }
} */