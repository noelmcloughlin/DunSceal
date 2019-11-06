package org.noel.dunsceal.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class DunModel(
    var id: Long = 0,
    var name: String = "",
    var description: String = "",
    var image: String = "",
    var lat: Double = 0.0,
    var lng: Double = 0.0,
    var zoom: Float = 0f
) : Parcelable

@Parcelize
data class Investigation(
    var done: Boolean,
    var date: Date,
    var type: String = "",
    var comments: String = ""
) : Parcelable

/**
 * Future Model
 * Sites [ ID, Name]
 * Investigations [ Date, Type, Comments ]
 * Entrances [ number, type, position, summary, comments ]
 * Dating [ type, comments ]
 **/

@Parcelize
data class Location(
    var lat: Double = 0.0,
    var lng: Double = 0.0,
    var zoom: Float = 0f
) : Parcelable