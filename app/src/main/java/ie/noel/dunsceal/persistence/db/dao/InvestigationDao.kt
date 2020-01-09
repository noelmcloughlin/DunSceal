package ie.noel.dunsceal.persistence.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import ie.noel.dunsceal.models.entity.DunEntity
import ie.noel.dunsceal.models.entity.InvestigationEntity

@Dao
interface InvestigationDao {
  @Query("SELECT * FROM investigations where dunId = :dunId")
  fun loadAll(dunId: Int): LiveData<List<InvestigationEntity?>?>?

  @Query("SELECT * FROM investigations where dunId = :dunId")
  fun loadAllSync(dunId: Int): List<InvestigationEntity?>?

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun insertAll(investigations: List<InvestigationEntity?>?)

  @Query("select * from investigations where id = :investigationId")
  fun loadInvestigations(investigationId: Int): LiveData<InvestigationEntity>

  @Query("select * from investigations where id = :investigationId")
  fun loadSync(investigationId: Int): InvestigationEntity?

  // Room DB integration

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun create(investigationEntity: InvestigationEntity?)

  @Query("SELECT * FROM investigations")
  fun findAll(): List<InvestigationEntity?>?

  @Query("select * from investigations where id = :id")
  fun findById(id: Long): InvestigationEntity?

  @Update
  fun updateInvestigation(investigationEntity: InvestigationEntity?)

  @Delete
  fun deleteInvestigation(investigationEntity: InvestigationEntity?)
}