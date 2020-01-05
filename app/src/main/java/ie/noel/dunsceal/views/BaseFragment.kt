package ie.noel.dunsceal.views

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import ie.noel.dunsceal.R

open class BaseFragment : Fragment() {

  lateinit var loader: AlertDialog
  lateinit var root: View
  var totalDone = 0

  override fun onCreateView(
      inflater: LayoutInflater,
      container: ViewGroup?,
      savedInstanceState: Bundle?
  ): View? {
    activity?.title = getString(R.string.title_activity_fragment_default_tag)
    // Inflate a default layout for this fragment
    return inflater.inflate(R.layout.fragment_base, container, false)
  }
}