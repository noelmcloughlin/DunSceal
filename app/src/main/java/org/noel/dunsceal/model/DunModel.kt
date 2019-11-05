package org.noel.dunsceal.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DunModel(var id: Long = 0,
                    var title: String = "",
                    var description: String = "",
                    var image: String = "",
                    var lat : Double = 0.0,
                    var lng: Double = 0.0,
                    val paymentmethod: String = "N/A",
                    val amount: Int = 0,
                    var zoom: Float = 0f) : Parcelable

@Parcelize
data class Location(var lat: Double = 0.0,
                    var lng: Double = 0.0,
                    var zoom: Float = 0f) : Parcelable

@Parcelize
data class DonationModel(var id: Long = 0,
                         val paymentmethod: String = "N/A",
                         val amount: Int = 0) : Parcelable