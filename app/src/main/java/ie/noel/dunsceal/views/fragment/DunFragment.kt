package ie.noel.dunsceal.views.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

import ie.noel.dunsceal.R
import ie.noel.dunsceal.models.DunModel
import ie.noel.dunsceal.utils.Loader.createLoader
import ie.noel.dunsceal.utils.Loader.hideLoader
import ie.noel.dunsceal.utils.Loader.showLoader
import ie.noel.dunsceal.views.home.HomePresenter
import kotlinx.android.synthetic.main.fragment_dun.*
import kotlinx.android.synthetic.main.fragment_dun.view.*

import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.toast
import java.util.HashMap

open class DunFragment(private var presenter: HomePresenter) : Fragment(), AnkoLogger {

    var totalDone = 0
    private lateinit var loader: AlertDialog
    private lateinit var eventListener: ValueEventListener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_dun, container, false)
        loader = createLoader(activity!!)
        activity?.title = getString(R.string.action_dun)

        root.progressBar.max = 10000
        root.amountPicker.minValue = 1
        root.amountPicker.maxValue = 1000

        root.amountPicker.setOnValueChangedListener { picker, oldVal, newVal ->
            //Display the newly selected number to paymentAmount
            root.paymentAmount.setText("$newVal")
        }
        setButtonListener(root)
        return root
    }

    companion object {
        @JvmStatic
        fun newInstance(presenter: HomePresenter): DunFragment {
            return DunFragment(presenter).apply {
                arguments = Bundle().apply {}
            }
        }
    }

    private fun setButtonListener(layout: View) {
        layout.donateButton.setOnClickListener {
            val amount = if (layout.paymentAmount.text.isNotEmpty())
                layout.paymentAmount.text.toString().toInt() else layout.amountPicker.value
            if (totalDone >= layout.progressBar.max)
                activity?.toast("Donate Amount Exceeded!")
            else {
                val paymentmethod =
                    if (layout.paymentMethod.checkedRadioButtonId == R.id.Direct) "Direct" else "Paypal"
                writeNewDun(
                    DunModel(
                        paymenttype = paymentmethod,
                        amount = amount,
                        profilepic = presenter.app.userImage.toString(),
                        email = presenter.app.auth.currentUser?.email
                    )
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        getTotalDone(presenter.app.auth.currentUser?.uid)
    }

    override fun onPause() {
        super.onPause()
        if (presenter.app.auth.uid != null)
            presenter.app.db.child("user-duns")
                .child(presenter.app.auth.currentUser!!.uid)
                .removeEventListener(eventListener)
    }

    private fun writeNewDun(dun: DunModel) {
        // Create new dun at /duns & /duns/$uid
        showLoader(loader, "Adding Dun to Firebase")
        info("Firebase DB Reference : ${presenter.app}.database")
        val uid = presenter.app.auth.currentUser!!.uid
        val key = presenter.app.db.child("duns").push().key!!.toLong()
        if (key == null) {
            info("Firebase Error : Key Empty")
            return
        }
        dun.id = key
        val dunValues = dun.toMap()

        val childUpdates = HashMap<String, Any>()
        childUpdates["/duns/$key"] = dunValues
        childUpdates["/user-duns/$uid/$key"] = dunValues

        presenter.app.db.updateChildren(childUpdates)
        hideLoader(loader)
    }

    private fun getTotalDone(userId: String?) {
        eventListener = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                info("Firebase Dun error : ${error.message}")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                totalDone = 0
                val children = snapshot.children
                children.forEach {
                    val dun = it.getValue<DunModel>(DunModel::class.java)
                    totalDone += dun!!.amount
                }
                progressBar.progress = totalDone
                totalSoFar.text = java.lang.String.format("$ ${this@DunFragment.totalDone}")
            }
        }
        presenter.fetchData()
        if (presenter.isUserLoggedIn()) {
            presenter.app.db.child("users").child(presenter.app.userId!!).child("duns")
                .addValueEventListener(eventListener)
        }
    }
}