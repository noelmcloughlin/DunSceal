package ie.noel.dunsceal.models

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import java.util.Date

@Parcelize
@Entity
data class PlacemarkModel(
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
    var fbId: String = "",
    var title: String = "",
    var description: String = "",
    var image: String = "",
    @Embedded var location: Location = Location()
) : Parcelable

@Parcelize
data class Location(
    var lat: Double = 0.0,
    var lng: Double = 0.0,
    var zoom: Float = 0f
) : Parcelable

@Parcelize
@Entity
data class DunModel1(@PrimaryKey(autoGenerate = true) var id: Long = 0,
                     var fbId : String = "",
                     var title: String = "",
                     var description: String = "",
                     var image: String = "",
                     @Embedded var location : Location = Location()): Parcelable

@Parcelize
data class Location1(var lat: Double = 0.0,
                     var lng: Double = 0.0,
                     var zoom: Float = 0f) : Parcelable

@IgnoreExtraProperties
@Parcelize
@Entity
data class DunModel(
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
    var fbId: String = "",
    var title: String = "",
    var description: String = "",
    var visited: Boolean = false,
    var upvotes: Int = 0,
    var image: String = "",
    @Embedded var images: ArrayList<String> = arrayListOf(),
    @Embedded var location: Location = Location(),
    @Embedded var investigations: ArrayList<Investigation> = arrayListOf(),
    @Embedded var entrances: ArrayList<Entrance> = arrayListOf(),
    @Embedded var datingEvidences: ArrayList<DatingEvidence> = arrayListOf(),
    var paymenttype: String = "N/A",
    var amount: Int = 0,
    var message: String = "a message",
    var profilepic: String = "",
    var email: String? = "joe@bloggs.com"
) : Parcelable {
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "id" to id,
            "fbId" to fbId,
            "title" to title,
            "description" to description,
            "visited" to visited,
            "upvotes" to upvotes,
            "image" to image,
            "images" to images,
            "location" to location,
            "investigations" to investigations,
            "entrances" to entrances,
            "datingEvidences" to datingEvidences,
            "paymenttype" to paymenttype,
            "amount" to amount,
            "message" to message,
            "profilepic" to profilepic,
            "email" to email
        )
    }
}


@Parcelize
data class Investigation(
    var date: Date,
    var type: String,
    var comments: String
) : Parcelable

@Parcelize
data class Entrance(
    var number: Short,
    var type: String,
    var position: String,
    var comments: String
) : Parcelable

@Parcelize
data class DatingEvidence(
    var type: String,
    var comments: String
) : Parcelable

@IgnoreExtraProperties
@Parcelize
data class User(
    var uid: String? = "",
    var paymenttype: String = "N/A",
    var amount: Int = 0,
    var message: String = "a message",
    var upvotes: Int = 0,
    var profilepic: String = "",
    var email: String? = "joe@bloggs.com"
) : Parcelable {
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "uid" to uid,
            "paymenttype" to paymenttype,
            "amount" to amount,
            "message" to message,
            "upvotes" to upvotes,
            "profilepic" to profilepic,
            "email" to email
        )
    }
}