package id.aria.notes.data.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import id.aria.notes.data.db.NoteDatabase
import id.aria.notes.data.models.Note
import id.aria.notes.data.repository.NoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NoteViewModel(application: Application): AndroidViewModel(application) {

    private val noteDao = NoteDatabase.getInstance(application).noteDao()
    private val repository: NoteRepository

    val getAllNotes: LiveData<List<Note>>

    val getNotesByHigh: LiveData<List<Note>>
    val getNotesByLow: LiveData<List<Note>>

    init {
        repository = NoteRepository(noteDao)
        getAllNotes = repository.getAllNote
        getNotesByHigh = repository.sortByHighNotes
        getNotesByLow = repository.sortByLowNotes
    }

    fun insertNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insert(note)
        }
    }

    fun updateNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.update(note)
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.delete(note)
        }
    }

    fun deleteAll() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAll()
        }
    }

    fun searchNote(searchQuery: String): LiveData<List<Note>> {
        return repository.searchNotes(searchQuery)
    }
}