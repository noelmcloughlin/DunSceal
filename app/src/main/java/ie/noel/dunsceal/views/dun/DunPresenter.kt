package ie.noel.dunsceal.views.dun

import android.annotation.SuppressLint
import android.content.Intent
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import ie.noel.dunsceal.utils.Location.checkLocationPermissions
import ie.noel.dunsceal.utils.Location.createDefaultLocationRequest
import ie.noel.dunsceal.utils.Location.isPermissionGranted
import ie.noel.dunsceal.utils.Image.showImagePicker
import ie.noel.dunsceal.models.Location
import ie.noel.dunsceal.models.DunModel
import ie.noel.dunsceal.views.*

class DunPresenter(view: BaseView) : BasePresenter(view) {

    var map: GoogleMap? = null
    var dun = DunModel()
    var defaultLocation = Location(52.245696, -7.139102, 15f)
    var edit = false
    var locationService: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(view)
    val locationRequest = createDefaultLocationRequest()

    init {
        if (view.intent.hasExtra("dun_edit")) {
            edit = true
            dun = view.intent.extras?.getParcelable<DunModel>("dun_edit")!!
            view.showDun(dun)
        } else {
            if (checkLocationPermissions(view)) {
                doSetCurrentLocation()
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun doSetCurrentLocation() {
        locationService.lastLocation.addOnSuccessListener {
            if (it != null)
                locationUpdate(Location(it.latitude, it.longitude))
        }
    }

    @SuppressLint("MissingPermission")
    fun doRestartLocationUpdates() {
        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                if (locationResult != null && locationResult.locations != null) {
                    val l = locationResult.locations.last()
                    if (l != null)
                        locationUpdate(Location(l.latitude, l.longitude))
                }
            }
        }
        if (!edit) {
            locationService.requestLocationUpdates(locationRequest, locationCallback, null)
        }
    }

    override fun doRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (isPermissionGranted(requestCode, grantResults)) {
            doSetCurrentLocation()
        } else {
            locationUpdate(defaultLocation)
        }
    }

    fun doConfigureMap(m: GoogleMap) {
        map = m
        locationUpdate(dun.location)
    }

    fun locationUpdate(location: Location) {
        dun.location = location
        dun.location.zoom = 15f
        map?.clear()
        val options =
            MarkerOptions().title(dun.title).position(LatLng(dun.location.lat, dun.location.lng))
        map?.addMarker(options)
        map?.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(
                    dun.location.lat,
                    dun.location.lng
                ), dun.location.zoom
            )
        )
        view?.showLocation(dun.location)
    }

    fun doAddOrSave(title: String, description: String) {
        dun.title = title
        dun.description = description
        doAsync {
            if (edit) {
                app.duns.update(dun)
            } else {
                app.duns.create(dun)
            }
            uiThread {
                view?.finish()
            }
        }
    }

    fun doCancel() {
        view?.finish()
    }

    fun doDelete() {
        doAsync {
            app.duns.delete(dun)
            uiThread {
                view?.finish()
            }
        }
    }

    fun doSelectImage() {
        view?.let {
            showImagePicker(view!!, IMAGE_REQUEST)
        }
    }

    fun doSetLocation() {
        view?.navigateTo(
            VIEW.LOCATION,
            LOCATION_REQUEST,
            "location",
            Location(dun.location.lat, dun.location.lng, dun.location.zoom)
        )
    }

    override fun doActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        when (requestCode) {
            IMAGE_REQUEST -> {
                dun.image = data.data.toString()
                view?.showDun(dun)
            }
            LOCATION_REQUEST -> {
                val location = data.extras?.getParcelable<Location>("location")!!
                dun.location = location
                locationUpdate(location)
            }
        }
    }
}