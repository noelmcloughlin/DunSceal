import androidx.lifecycle.LiveData
import androidx.room.*
import ie.noel.dunsceal.models.entity.LocationEntity

@Dao
interface LocationDao {
  @Query("SELECT * FROM locations where dunId = :dunId")
  fun loadLocations(dunId: Int): LiveData<List<LocationEntity>?>?

  @Query("SELECT * FROM locations where dunId = :dunId")
  fun loadLocationsSync(dunId: Int): List<LocationEntity?>?

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun insertAll(locationEntities: List<LocationEntity?>?)

// Room DB integration

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun createDun(locationEntity: LocationEntity?)

  @Query("SELECT * FROM locations")
  fun findAll(): List<LocationEntity?>?

  @Query("select * from locations where id = :id")
  fun findById(id: Long): LocationEntity?

  @Update
  fun updateLocation(locationEntity: LocationEntity?)

  @Delete
  fun deleteLocation(locationEntity: LocationEntity?)
}