package ie.noel.dunsceal.views.home

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import ie.noel.dunsceal.R


class AboutFragment : Fragment() {

  companion object {
    const val TAG = "AboutUsFragment"

    @JvmStatic
    fun newInstance() =
        AboutFragment().apply {
          arguments = Bundle().apply { }
        }
  }

  override fun onCreateView(
      inflater: LayoutInflater,
      container: ViewGroup?,
      savedInstanceState: Bundle?
  ): View? {
    // Inflate the layout for this fragment
    activity?.title = getString(R.string.about_us_title)
    return inflater.inflate(R.layout.fragment_about, container, false)
  }

  // HIDE THE OPTIONS MENU
  override fun onCreate(savedInstanceState: Bundle?) {
    // hide the options menu
    super.onCreate(savedInstanceState)
    setHasOptionsMenu(true)
  }

  override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
    super.onCreateOptionsMenu(menu, inflater!!)
    menu.clear()
  }
}