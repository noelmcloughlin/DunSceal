package ie.noel.dunsceal.views.home.dun

import android.content.Intent
import android.os.Bundle
import android.view.*
import com.bumptech.glide.Glide
import ie.noel.dunsceal.R
import ie.noel.dunsceal.models.entity.DunEntity
import ie.noel.dunsceal.models.entity.LocationEntity
import ie.noel.dunsceal.utils.Loader
import ie.noel.dunsceal.views.BaseFragment
import kotlinx.android.synthetic.main.fragment_dun_add.*
import org.jetbrains.anko.AnkoLogger

class DunAddFragment(val presenter: DunPresenter, private val user: String)
  : BaseFragment(), AnkoLogger {

  var dun = DunEntity()

  companion object {
    const val TAG = "DunAddFragment"
    @JvmStatic
    fun newInstance(presenter: DunPresenter, user: String): DunAddFragment {
      return DunAddFragment(presenter, user).apply {
        arguments = Bundle().apply {}
      }
    }
  }

  override fun onCreateView(
      inflater: LayoutInflater,
      container: ViewGroup?,
      savedInstanceState: Bundle?
  ): View? {
    super.onCreateView(inflater, container, savedInstanceState)
    val rootView = inflater.inflate(R.layout.fragment_dun_add, container, false)

    loader = Loader.createLoader(activity!!)
    activity?.title = getString(R.string.action_dun)

    mapView.onCreate(savedInstanceState)
    mapView.getMapAsync {
      presenter.doConfigureMap(it)
      it.setOnMapClickListener { presenter.doSetLocation() }
    }
    chooseImage.setOnClickListener { presenter.doSelectImage() }
    return rootView
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

  fun showLocation(location: LocationEntity) {
    dunLatitude.text = ("%.6f".format(location.latitude))
    dunLongitude.text = ("%.6f".format(location.longitude))
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    if (data != null) {
      presenter.doActivityResult(requestCode, resultCode, data)
    }
  }


  override fun onDestroy() {
    super.onDestroy()
    mapView.onDestroy()
  }

  override fun onLowMemory() {
    super.onLowMemory()
    mapView.onLowMemory()
  }

  override fun onPause() {
    super.onPause()
    mapView.onPause()
  }

  override fun onResume() {
    super.onResume()
    mapView.onResume()
    presenter.doRestartLocationUpdates()
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    mapView.onSaveInstanceState(outState)
  }
}


