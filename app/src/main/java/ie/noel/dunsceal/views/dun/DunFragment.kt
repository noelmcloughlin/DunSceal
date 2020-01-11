package ie.noel.dunsceal.views.dun

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import ie.noel.dunsceal.R
import ie.noel.dunsceal.adapters.InvestigationAdapter
import ie.noel.dunsceal.databinding.FragmentDunBinding
import ie.noel.dunsceal.models.entity.DunEntity
import ie.noel.dunsceal.models.entity.LocationEntity
import ie.noel.dunsceal.utils.Loader
import ie.noel.dunsceal.views.BaseFragment
import ie.noel.dunsceal.views.BaseView
import ie.noel.dunsceal.views.dunlist.DunListFragment
import ie.noel.dunsceal.views.home.HomeView
import kotlinx.android.synthetic.main.fragment_dun.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import java.text.SimpleDateFormat
import java.util.*

class DunFragment(val presenter: DunPresenter,
                  private val dun: DunEntity,
                  private val mode: String)
  : BaseFragment(), AnkoLogger {

  private var mBinding: FragmentDunBinding? = null
  private var mInvestigationAdapter: InvestigationAdapter? = null
  private var editDun: DunEntity? = null
  val cal = Calendar.getInstance()
  val sdf = SimpleDateFormat("MM/dd/yyyy", Locale.US)

  companion object {
    const val TAG = "DunViewFragment"
    private const val KEY_DUN_ID = "dun_id"

    /** Creates dun fragment for specific dun ID  */
    fun forDun(presenter: DunPresenter, dun: DunEntity): DunFragment {
      val fragment = DunFragment(presenter, dun, "")
      val args = Bundle().also {
        it.putInt(KEY_DUN_ID, dun.id.toInt())
      }
      fragment.arguments = args
      return fragment
    }

    /** Creates empty dun fragment  */
    @JvmStatic
    fun newInstance(presenter: DunPresenter, dun: DunEntity, mode: String): DunFragment {
      return DunFragment(presenter, dun, mode).apply {
        arguments = Bundle().apply {
          putParcelable(mode, dun)
        }
      }
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    arguments?.let {
      editDun = it.getParcelable("edit_dun")
    }
    // We need new options menu
    setHasOptionsMenu(true)
  }

  override fun onCreateView(
      inflater: LayoutInflater,
      container: ViewGroup?,
      savedInstanceState: Bundle?
  ): View? {

    super.onCreateView(inflater, container, savedInstanceState)

    // Inflate this data binding layout
    mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_dun, container, false)

    // Create and set the adapter for the RecyclerView.
    mInvestigationAdapter = InvestigationAdapter(mInvestigationClickCallback = null)
    mBinding!!.investigationList.adapter = mInvestigationAdapter

    // Setup the map view
    if (mBinding!!.mapView != null) {

      mBinding!!.mapView.onCreate(savedInstanceState)
      mBinding!!.mapView.getMapAsync {
        presenter.doConfigureMap(it)
        it.setOnMapClickListener { presenter.doSetLocation() }
      }
    }

    // and date picker
    mBinding!!.buttonDatePicker.text = this.button_date_picker?.text.toString()

    // create an OnDateSetListener
    val dateSetListener =
        DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
          cal.set(Calendar.YEAR, year)
          cal.set(Calendar.MONTH, monthOfYear)
          cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
          mBinding!!.buttonDatePicker.text = sdf.format(cal.time)
        }

      // onclick, show DatePickerDialog for OnDateSetListener
    mBinding!!.buttonDatePicker.setOnClickListener {
      DatePickerDialog(
          context,
          dateSetListener,
          // set DatePickerDialog to point to today's date when it loads up
          cal.get(Calendar.YEAR),
          cal.get(Calendar.MONTH),
          cal.get(Calendar.DAY_OF_MONTH)
      ).show()
    }

    // setup the image chooser
    if (mBinding!!.chooseImage != null) {
      mBinding!!.chooseImage.setOnClickListener { presenter.doSelectImage() }
    }

    // setup title
    if (mode == "edit") {
      activity?.title = getString(R.string.action_edit)
    } else {
      activity?.title = getString(R.string.action_dun)
    }

    return mBinding!!.root
  }

  fun showDun(dun: DunEntity) {
    name.setText(dun.name)
    description.setText(dun.description)
    Glide.with(this).load(dun.image).into(dunImage)
    if (dun.image != null) {
      chooseImage.setText(R.string.change_dun_image)
    }
    this.showLocation(dun.location)
  }

  private fun updateUserDun(uid: String?, dun: DunEntity) {
    presenter.app.db.child("users").child(presenter.app.userId).child("duns").child(presenter.app.userId).child(uid!!)
        .addListenerForSingleValueEvent(
            object : ValueEventListener {
              override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.ref.setValue(dun)
                activity!!.supportFragmentManager.beginTransaction()
                    .replace(R.id.content_home_frame,
                        DunListFragment.newInstance(presenter)
                    )
                    .addToBackStack(null)
                    .commit()
                Loader.hideLoader(loader)
              }

              override fun onCancelled(error: DatabaseError) {
                info("Firebase Dun error : ${error.message}")
              }
            })
  }

  private fun updateDun(dun: DunEntity) {
    presenter.app.db.child("duns").child(presenter.app.userId)
        .addListenerForSingleValueEvent(
            object : ValueEventListener {
              override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.ref.setValue(dun)
                Loader.hideLoader(loader)
              }

              override fun onCancelled(error: DatabaseError) {
                info("Firebase Dun error : ${error.message}")
              }
            })
  }

  private fun showLocation(location: LocationEntity) {
    dunLatitude.text = ("%.6f".format(location.latitude))
    dunLongitude.text = ("%.6f".format(location.longitude))
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    if (data != null) {
      presenter.doActivityResult(requestCode, resultCode, data)
    }
  }

  /*override fun onBackPressed() {
    presenter.doCancel()
  }*/

  override fun onDestroy() {
    super.onDestroy()
    mBinding!!.mapView.onDestroy()
  }

  override fun onLowMemory() {
    super.onLowMemory()
    mBinding!!.mapView.onLowMemory()
  }

  override fun onPause() {
    super.onPause()
    mBinding!!.mapView.onPause()
  }

  override fun onResume() {
    super.onResume()
    mBinding!!.mapView.onResume()
    presenter.doRestartLocationUpdates()
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    mBinding!!.mapView.onSaveInstanceState(outState)
  }

  /** Shows dun detail fragment  */
  // TODO utilise this method of showing a fragment
  fun showDunFragment(dun: DunEntity) {
    val dunFragment = forDun(presenter, dun)
    (activity as BaseView).fragManager
        .beginTransaction()
        .addToBackStack("dun")
        .replace(R.id.content_home_frame,
            dunFragment, null).commit()
  }


  // OPTIONS MENU
  override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
    inflater.inflate(R.menu.menu_dun, menu)
    super.onCreateOptionsMenu(menu, inflater)
  }

  //handle item clicks
  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when (item.itemId) {
      R.id.menu_dun_item_delete -> {
        presenter.doDelete()
      }
      R.id.menu_dun_item_save -> {
        if (name.text.toString().isEmpty()) {
          // anko toast is not working inside fragment!!!
          // use Toast instead!!!!
          Toast.makeText(
              context, R.string.enter_dun_title,
              Toast.LENGTH_SHORT
          ).show()
        } else {
          presenter.doAddOrSave(name.text.toString(), description.text.toString())
        }
      }
    }
    presenter.dunDataStore!!.fetchDuns {
      if (activity != null) {
        (activity as HomeView).fragManager.popBackStackImmediate()
      }
    }
    return true
  }
}