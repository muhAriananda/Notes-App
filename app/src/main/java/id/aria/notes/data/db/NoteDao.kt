package id.aria.notes.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import id.aria.notes.data.models.Note

@Dao
interface NoteDao {

    @Query("SELECT * FROM note_table")
    fun getAllNotes(): LiveData<List<Note>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(note: Note)

    @Update
    suspend fun update(note: Note)

    @Delete
    suspend fun delete(note: Note)

    @Query("DELETE FROM note_table")
    suspend fun deleteAll()
}