package ie.noel.dunsceal.views

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import ie.noel.dunsceal.R
import ie.noel.dunsceal.models.entity.ContentEntity
import ie.noel.dunsceal.utils.Loader
import kotlinx.android.synthetic.main.fragment_report.view.*

open class BaseFragment(var presenter: BasePresenter) : Fragment() {

  lateinit var loader: AlertDialog
  lateinit var root: View
  var totalDone = 0

  override fun onCreateView(
      inflater: LayoutInflater,
      container: ViewGroup?,
      savedInstanceState: Bundle?
  ): View? {

    // Inflate a default layout for this fragment
    activity?.title = getString(R.string.title_activity_fragment_default_tag)
    root = inflater.inflate(R.layout.fragment_base, container, false)
    loader = Loader.createLoader(activity!!)
    setSwipeRefresh()
    return root
  }

  // Swipe support
  open fun setSwipeRefresh() {
    root.swipeRefresh.setOnRefreshListener {
      root.swipeRefresh.isRefreshing = true
    }
  }

  fun checkSwipeRefresh() {
    if (root.swipeRefresh.isRefreshing) root.swipeRefresh.isRefreshing = false
  }

  fun onClick(dun: ContentEntity) {
    presenter.doEditContent(dun)
  }

}