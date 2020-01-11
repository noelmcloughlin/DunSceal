package ie.noel.dunsceal.models.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LocationEntity(var latitude: Double = 52.245696,
                          var longitude: Double = -7.139102,
                          var zoom: Float = 15f) : Parcelable


/* Not needed ...
@IgnoreExtraProperties
@Parcelize
@Entity(tableName = "locations", foreignKeys = [ForeignKey(entity = DunEntity::class, parentColumns = ["id"], childColumns = ["dunId"], onDelete = ForeignKey.CASCADE)], indices = [Index(value = ["dunId"])])
data class LocationEntity(
    @PrimaryKey(autoGenerate = true) override var id: Long = 0,
    override var dunId: Long = 0L,
    override var latitude: Double = 52.245696,
    override var longitude: Double = -7.139102,
    override var zoom: Float = 15f
) : Parcelable, LocationEntity {
  @Exclude
  fun toMap(): Map<String, Any?> {
    return mapOf(
        "id" to id,
        "dunId" to "dunId",
        "latitude" to latitude,
        "longitude" to longitude,
        "zoom" to zoom
        )
  }
} */