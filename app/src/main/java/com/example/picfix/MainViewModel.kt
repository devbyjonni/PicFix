package com.example.picfix

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.picfix.data.NoteEntity
import com.example.picfix.data.SampleDataProvider


class MainViewModel : ViewModel() {

    val notesList = MutableLiveData<List<NoteEntity>>()

    init {
        notesList.value = SampleDataProvider.getNotes()
    }
}