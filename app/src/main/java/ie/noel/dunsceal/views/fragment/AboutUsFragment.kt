package ie.noel.dunsceal.views.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ie.noel.dunsceal.R

class AboutUsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        activity?.title = getString(R.string.aboutus_title)
        return inflater.inflate(R.layout.fragment_about_us, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            AboutUsFragment().apply {
                arguments = Bundle().apply { }
            }
    }
}