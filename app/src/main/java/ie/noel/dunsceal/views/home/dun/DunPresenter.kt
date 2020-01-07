package ie.noel.dunsceal.views.home.dun

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
import ie.noel.dunsceal.models.entity.DunEntity
import ie.noel.dunsceal.models.entity.LocationEntity
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import ie.noel.dunsceal.utils.Permissions.checkLocationPermissions
import ie.noel.dunsceal.utils.Permissions.createDefaultLocationRequest
import ie.noel.dunsceal.utils.Permissions.isPermissionGranted
import ie.noel.dunsceal.utils.Image.showImagePicker
import ie.noel.dunsceal.views.*
import ie.noel.dunsceal.views.home.HomePresenter
import ie.noel.dunsceal.views.home.HomeView
import ie.noel.dunsceal.views.login.LoginView

class DunPresenter(view: DunView) : HomePresenter(view) {

    var map: GoogleMap? = null
    var dun = DunEntity()
    private var defaultLocation = LocationEntity(52.245696, -7.139102, "Waterford", 15f)
    private var edit = false
    private var locationService: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(view)
    private val locationRequest = createDefaultLocationRequest()

    init {
        if (view.intent.hasExtra("dun_edit")) {
            edit = true
            dun = view.intent.extras?.getParcelable("dun_edit")!!
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
                locationUpdate(LocationEntity(it.latitude, it.longitude))
        }
    }

    @SuppressLint("MissingPermission")
    fun doRestartLocationUpdates() {
        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                if (locationResult != null && locationResult.locations != null) {
                    val l = locationResult.locations.last()
                    if (l != null)
                        locationUpdate(LocationEntity(l.latitude, l.longitude))
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

    fun locationUpdate(locationEntity: LocationEntity) {
        dun.location = locationEntity
        dun.location.zoom = 15f
        map?.clear()
        val options =
            MarkerOptions().title(dun.name).position(LatLng(dun.location.latitude, dun.location.longitude))
        map?.addMarker(options)
        map?.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(
                    dun.location.latitude,
                    dun.location.longitude
                ), dun.location.zoom
            )
        )
        view?.showLocation(dun.location)
    }

    fun doAddOrSave(title: String, description: String) {
        dun.name = title
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
            LocationEntity(dun.location.latitude, dun.location.longitude, dun.location.county, dun.location.zoom)
        )
    }

    override fun doActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        when (requestCode) {
            IMAGE_REQUEST -> {
                if (data.data != null)
                    dun.image = data.data!!.toString()
                view?.showDun(dun)
            }
            LOCATION_REQUEST -> {
                val location = data.extras?.getParcelable<LocationEntity>("location")!!
                dun.location = location
                locationUpdate(location)
            }
        }
    }
}