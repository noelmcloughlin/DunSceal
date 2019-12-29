package ie.noel.dunsceal.models.room

import androidx.room.*
import ie.noel.dunsceal.models.DunModel

@Dao
interface DunDao {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun create(dun: DunModel)

  @Query("SELECT * FROM DunModel")
  fun findAll(): List<DunModel>

  @Query("select * from DunModel where id = :id")
  fun findById(id: Long): DunModel

  @Update
  fun update(dun: DunModel)

  @Delete
  fun deleteDun(dun: DunModel)
}