package ie.noel.dunsceal.views.fragment.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import ie.noel.dunsceal.R
import ie.noel.dunsceal.main.MainApp
import kotlinx.android.synthetic.main.fragment_base.*
import java.util.*
import ie.noel.dunsceal.utils.Image.getRandomColor

open class BaseFragment : Fragment() {

    private val backgroundColor = getRandomColor()
    private val fragmentNumber = "!"
    //(++fragmentService.fragmentsCount).toString()

    private val dispatcher by lazy { requireActivity().onBackPressedDispatcher }
    lateinit var callback: OnBackPressedCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        callback = dispatcher.addCallback(this) {
            //fragmentService.fragmentsCount--

            //Called when user should be navigated back
            callback.isEnabled = false
            dispatcher.onBackPressed()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_base, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        fragmentFrameConstraintLayout.setBackgroundColor(backgroundColor)

//        fragmentBaseNumberTextView.text = fragmentNumber

/*       fragmentFrameFirstButton.setOnClickListener {
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