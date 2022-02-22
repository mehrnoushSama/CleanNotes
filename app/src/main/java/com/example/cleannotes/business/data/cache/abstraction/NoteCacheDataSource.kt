package com.example.cleannotes.business.data.cache.abstraction

import com.example.cleannotes.business.domain.model.Note

interface NoteCacheDataSource {

    suspend fun insertNote(note: Note): Long
    suspend fun deleteNote(primary: String): Int
    suspend fun deleteNotes(note: List<Note>): Int
    suspend fun updateNote(
        primaryKey: String,
        newTitle: String,
        newBody: String?,
        timestamp: String?
    ): Int

    suspend fun searchNote(query: String, filterAndOrder: String, page: Int): List<Note>
    suspend fun searchNoteById(primary: String):  Note?
    suspend fun getNumNotes(): Int
    suspend fun insertNotes(note: List<Note>): LongArray

}