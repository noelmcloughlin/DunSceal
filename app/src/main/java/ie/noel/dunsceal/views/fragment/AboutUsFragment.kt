package ie.noel.dunsceal.views.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ie.noel.dunsceal.R
import ie.noel.dunsceal.views.BaseFragment

class AboutUsFragment : BaseFragment() {

    companion object {
        @JvmStatic
        fun newInstance() =
            AboutUsFragment().apply {
                arguments = Bundle().apply { }
            }
    }
}