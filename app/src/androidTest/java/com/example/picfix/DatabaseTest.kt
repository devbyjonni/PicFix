package com.example.picfix

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.picfix.data.AppDatabase
import com.example.picfix.data.NoteDao
import com.example.picfix.data.NoteEntity
import com.example.picfix.data.SampleDataProvider
import org.junit.After

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before

@RunWith(AndroidJUnit4::class)
class DatabaseTest {
    private lateinit var dao: NoteDao
    private lateinit var database: AppDatabase

    @Before
    fun createDb() {
        // inMemoryDatabaseBuilder, This ensures that you're not storing the database in persistent storage during the test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.inMemoryDatabaseBuilder(appContext, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = database.noteDao()!!
    }

    @Test
    fun createNotes() {
        dao.insertAll(SampleDataProvider.getNotes())
        val count = dao.getCount()
        assertEquals(count, SampleDataProvider.getNotes().size)
    }

    @Test
    fun insertNote() {
        val note = NoteEntity()
        note.text = "some text"
        dao.insertNote(note)
        val savedNote = dao.getNoteById(1)
        assertEquals(savedNote?.id ?: 0, 1)
    }

    @After
    fun closeDb() {
        database.close()
    }
}