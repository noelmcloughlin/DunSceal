package ie.noel.dunsceal.views.fragment

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
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        activity?.title = getString(R.string.aboutus_title)
        return inflater.inflate(R.layout.fragment_about_us, container, false)
    }

}