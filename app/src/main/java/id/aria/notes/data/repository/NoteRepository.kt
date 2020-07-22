package id.aria.notes.data.repository

import androidx.lifecycle.LiveData
import id.aria.notes.data.db.NoteDao
import id.aria.notes.data.models.Note

class NoteRepository(private val noteDao: NoteDao) {

    val getAllNote: LiveData<List<Note>> = noteDao.getAllNotes()

    val sortByHighNotes: LiveData<List<Note>> = noteDao.sortByHighPriority()

    val sortByLowNotes: LiveData<List<Note>> = noteDao.sortByLowPriority()

    fun searchNotes(searchQuery: String): LiveData<List<Note>> = noteDao.searchNotes(searchQuery)

    suspend fun insert(note: Note) {
        noteDao.insert(note)
    }

    suspend fun update(note: Note) {
        noteDao.update(note)
    }

    suspend fun delete(note: Note) {
        noteDao.delete(note)
    }

    suspend fun deleteAll() {
        noteDao.deleteAll()
    }
}