package ie.noel.dunsceal.persistence.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import ie.noel.dunsceal.models.entity.NoteEntity

@Dao
interface NoteDao {
  @Query("SELECT * FROM notes where dunId = :dunId")
  fun load(dunId: Int): LiveData<List<NoteEntity>?>?

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun insertAll(noteEntities: List<NoteEntity?>?)

  // Room DB integration

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun create(noteEntity: NoteEntity?)

  @Query("SELECT * FROM notes")
  fun findAll(): List<NoteEntity?>?

  @Query("select * from notes where id = :id")
  fun findById(id: Long): NoteEntity?

  @Update
  fun update(noteEntity: NoteEntity?)

  @Delete
  fun delete(noteEntity: NoteEntity?)
}