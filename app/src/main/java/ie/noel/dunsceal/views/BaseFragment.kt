package ie.noel.dunsceal.views

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import ie.noel.dunsceal.R
import ie.noel.dunsceal.utils.Loader
import kotlinx.android.synthetic.main.fragment_dun_list.view.*

open class BaseFragment : Fragment() {

  lateinit var loader: AlertDialog
  lateinit var root: View

  // Swipe support
  open fun setSwipeRefresh() {
    if (root.swipeRefresh != null) {
      root.swipeRefresh.setOnRefreshListener {
        root.swipeRefresh.isRefreshing = true
      }
    }
  }

  open fun checkSwipeRefresh() {
    if ((root.swipeRefresh != null) && (root.swipeRefresh.isRefreshing))
      root.swipeRefresh.isRefreshing = false
  }
}