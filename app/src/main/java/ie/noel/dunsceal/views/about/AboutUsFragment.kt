package ie.noel.dunsceal.views.about

import android.os.Bundle
import android.view.View
import ie.noel.dunsceal.views.BaseFragment

class AboutUsFragment : BaseFragment() {

    companion object {
        @JvmStatic
        fun newInstance() =
            AboutUsFragment().apply {
                arguments = Bundle().apply { }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    }
}