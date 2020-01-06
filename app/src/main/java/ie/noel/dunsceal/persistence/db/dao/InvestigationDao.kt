package ie.noel.dunsceal.persistence.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import ie.noel.dunsceal.models.entity.Investigation

@Dao
interface InvestigationDao {
  @Query("SELECT * FROM investigations where dunId = :dunId")
  fun loadInvestigations(dunId: Int): LiveData<List<Investigation>?>?

  @Query("SELECT * FROM investigations where dunId = :dunId")
  fun loadInvestigationsSync(dunId: Int): List<Investigation?>?

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun insertAll(investigations: List<Investigation?>?)

  // Room DB integration

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun createDun(investigation: Investigation?)

  @Query("SELECT * FROM investigations")
  fun findAll(): List<Investigation?>?

  @Query("select * from investigations where id = :id")
  fun findById(id: Long): Investigation?

  @Update
  fun updateInvestigation(investigation: Investigation?)

  @Delete
  fun deleteInvestigation(investigation: Investigation?)
}