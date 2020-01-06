
package ie.noel.dunsceal.models.entity

import android.os.Parcelable
import androidx.room.Ignore
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LocationEntity(var latitude: Double = 52.245696,
                          var longitude: Double = -7.139102,
                          var county: String? = null,
                          var zoom: Float = 15f) : Parcelable