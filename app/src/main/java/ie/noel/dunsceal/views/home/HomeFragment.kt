package ie.noel.dunsceal.views.home

import android.os.Bundle
import android.view.*
import android.widget.Toast
import ie.noel.dunsceal.R
import ie.noel.dunsceal.utils.Loader.createLoader
import ie.noel.dunsceal.views.BaseFragment
import ie.noel.dunsceal.views.BaseView
import ie.noel.dunsceal.views.dunlist.DunListFragment
import kotlinx.android.synthetic.main.activity_login_screen.view.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import org.jetbrains.anko.AnkoLogger


open class HomeFragment(val presenter: HomePresenter, private val user: String)
  : BaseFragment(), AnkoLogger {

  companion object {
    const val TAG = "HomeFragment"
    @JvmStatic
    fun newInstance(presenter: HomePresenter, user: String): HomeFragment {
      return HomeFragment(presenter, user).apply {
        arguments = Bundle().apply {}
      }
    }
  }

  //enable options menu
  override fun onCreate(savedInstanceState: Bundle?) {
    setHasOptionsMenu(true)
    super.onCreate(savedInstanceState)
  }

  override fun onCreateView(
      inflater: LayoutInflater,
      container: ViewGroup?,
      savedInstanceState: Bundle?
  ): View? {

    super.onCreateView(inflater, container, savedInstanceState)
    val rootView = inflater.inflate(R.layout.fragment_home, container, false)

    loader = createLoader(activity!!)
    presenter.fetchData()

    activity?.title = getString(R.string.action_home)
    if (rootView.progressBar != null) {
      rootView.progressBar.max = 10000   // TODO dynamic calculation
    }
    if (rootView.home_play_btn != null) {
      rootView.home_play_btn.setOnClickListener {
        val fragment: BaseFragment = DunListFragment.newInstance(presenter)
        (activity as BaseView).fragManager.beginTransaction()
            .replace(R.id.content_home_frame, fragment, DunListFragment.TAG).commit()
      }
    }
    return rootView
  }

  fun onClick() {
    // anko toast is not working inside fragment
    // using Toast instead
    Toast.makeText(context, "button clicked", Toast.LENGTH_LONG).show()
  }

  override fun onResume() {
    super.onResume()
    presenter.fetchData()
    // getTotalDone(presenter.app.auth.currentUser?.uid)
  }

  override fun onPause() {
    super.onPause()
    val userId = presenter.app.auth.uid
    /* if (presenter.app.auth.uid != null)
    presenter.app.db.child("users").child(userId!!).child("duns")
            .child(presenter.app.auth.currentUser!!.uid)
            .removeEventListener(eventListener) */
  }

  /*private fun getTotalDone(userId: String?) {
     eventListener = object : ValueEventListener {
          override fun onCancelled(error: DatabaseError) {
              info("Firebase Dun error : ${error.message}")
          }

          override fun onDataChange(snapshot: DataSnapshot) {
              totalDone = 0
              val children = snapshot.children
              children.forEach {
                  val dun = it.getValue<DunEntity>(DunEntity::class.java)
                  totalDone += dun!!.votes
              }
              if (progressBar != null)
                  progressBar.progress = totalDone
              if (home_total_visited_amount != null)
                  home_total_visited_amount.text = java.lang.String.format("$ ${this@HomeFragment.totalDone}")
          }
      }
      presenter.fetchData()
      if (presenter.isUserLoggedIn()) {
          presenter.app.db.child("users").child(presenter.app.userId).child("duns")
              .addValueEventListener(eventListener)
      }
     */
}