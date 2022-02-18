package com.example.cleannotes.business.data.network.abstraction

import com.example.cleannotes.business.domain.model.Note

interface NoteNetworkDataSource {

    suspend fun insertOrUpdateNote(note: Note)
    suspend fun deleteNote(primaryKey: String)
    suspend fun insertDeleteNote(note: Note)
    suspend fun insertDeleteNotes(notes: List<Note>)
    suspend fun deleteDeletedNote(note: Note)
    suspend fun getDeletedNote(): List<Note>
    suspend fun deleteAllNotes()
    suspend fun searchNotes(note: Note): Note?
    suspend fun getAllNotes(): List<Note>
    suspend fun insertOrUpdateNotes(notes: List<Note>)

}