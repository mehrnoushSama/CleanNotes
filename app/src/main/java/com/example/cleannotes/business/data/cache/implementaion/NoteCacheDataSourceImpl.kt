package com.example.cleannotes.business.data.cache.implementaion

import com.example.cleannotes.business.data.cache.abstraction.NoteCacheDataSource
import com.example.cleannotes.business.domain.model.Note
import com.example.cleannotes.framework.datasource.cache.abstraction.NoteDaoService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoteCacheDataSourceImpl @Inject constructor(
    private val noteDaoService: NoteDaoService
) : NoteCacheDataSource {
    override suspend fun insertNote(note: Note): Long = noteDaoService.insertNote(note)

    override suspend fun deleteNote(primary: String) = noteDaoService.deleteNote(primary)

    override suspend fun deleteNotes(notes: List<Note>): Int = noteDaoService.deleteNotes(notes)

    override suspend fun updateNote(
        primaryKey: String,
        newTitle: String,
        newBody: String?,
        timestamp: String?
    ): Int {
        return noteDaoService.updateNote(
            primaryKey,
            newTitle,
            newBody,
            timestamp
        )
    }

    override suspend fun searchNote(query: String, filterAndOrder: String, page: Int): List<Note> =
        noteDaoService.searchNotes()

    override suspend fun searchNoteById(primary: String): Note? =
        noteDaoService.searchNoteById(primary)

    override suspend fun getNumNotes(): Int = noteDaoService.getNumNotes()

    override suspend fun insertNotes(notes: List<Note>): LongArray =
        noteDaoService.insertNotes(notes)
}