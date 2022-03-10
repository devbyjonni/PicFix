package com.example.picfix

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.picfix.data.AppDatabase
import com.example.picfix.data.NoteEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EditorViewModel(app: Application) : AndroidViewModel(app) {

    private val database = AppDatabase.getInstance(app)
    val currentNote = MutableLiveData<NoteEntity>()

    fun getNoteById(noteId: Int) {
        // I'll be retrieving data from the database so I need to do this inside a background thread.
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val note =
                    if (noteId != NEW_NOTE_ID) {
                        database?.noteDao()?.getNoteById(noteId)
                    } else {
                        NoteEntity()
                    }
                currentNote.postValue(note)
            }
        }
    }

    fun updateNote() {
        //it in this context refers to the note entity object that's wrapped inside that mutable live data object.
        currentNote.value?.let {
            it.text = it.text.trim()
            if (it.id == NEW_NOTE_ID && it.text.isEmpty()) {
                return
            }

            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    if (it.text.isEmpty()) {
                        database?.noteDao()?.deleteNote(it)
                    } else {
                        database?.noteDao()?.insertNote(it)
                    }
                }
            }
        }
    }
}