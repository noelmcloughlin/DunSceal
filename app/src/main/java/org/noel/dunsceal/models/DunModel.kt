package org.noel.dunsceal.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import org.noel.dunsceal.R

@Parcelize
data class DunModel(
    var id: Long = 0,
    var title: String = "",
    var description: String = "",
    var isCompleted: Boolean = false,
    var image: String = "",
    var url: String = "",
    var lat: Double = 0.0,
    var lng: Double = 0.0,
    var zoom: Float = 0f
) : Parcelable

@Parcelize
data class Location(var lat: Double = 0.0,
                    var lng: Double = 0.0,
                    var zoom: Float = 0f) : Parcelable

data class DunData(
    val names: List<Int> = listOf(R.string.tab1_title, R.string.tab2_title, R.string.tab3_title),
    val urls: List<Int> = listOf(R.string.tab1_url, R.string.tab2_url, R.string.tab3_url),
    val description: List<Int> = listOf(R.string.tab1_desc, R.string.tab2_desc, R.string.tab3_desc)
)
