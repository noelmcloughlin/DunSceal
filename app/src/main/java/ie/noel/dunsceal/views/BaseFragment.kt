package ie.noel.dunsceal.views

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import ie.noel.dunsceal.R
import ie.noel.dunsceal.models.DunModel
import ie.noel.dunsceal.views.dunlist.DunAdapter
import kotlinx.android.synthetic.main.activity_dun_list.*

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
/*       someButton.setOnClickListener {
            parentFragmentManager.commit {
                setCustomAnimations(
                    R.anim.enter_from_right,
                    R.anim.exit_to_left,
                    R.anim.enter_from_left,
                    R.anim.exit_to_right
                )
                replace<BaseFragment>(
                    R.id.fragment
                )
                addToBackStack(null)
            }
        } */
    }
}