package com.example.cleannotes.business.data.network.implementation

import com.example.cleannotes.business.data.network.abstraction.NoteNetworkDataSource
import com.example.cleannotes.business.domain.model.Note
import com.example.cleannotes.framework.datasource.network.abstraction.NoteFireStoreService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoteNetworkDatasourceImpl @Inject constructor(
    private val fireStoreService: NoteFireStoreService
) : NoteNetworkDataSource {
    override suspend fun insertOrUpdateNote(note: Note) = fireStoreService.insertOrUpdateNote(note)

    override suspend fun deleteNote(primaryKey: String) = fireStoreService.deleteNote(primaryKey)

    override suspend fun insertDeleteNote(note: Note) = fireStoreService.insertDeleteNote(note)

    override suspend fun insertDeleteNotes(notes: List<Note>) =
        fireStoreService.insertDeleteNotes(notes)

    override suspend fun deleteDeletedNote(note: Note) = fireStoreService.deleteDeletedNote(note)

    override suspend fun getDeletedNote(): List<Note> = fireStoreService.getDeletedNote()

    override suspend fun deleteAllNotes() = fireStoreService.deleteAllNotes()

    override suspend fun searchNotes(note: Note): Note? = fireStoreService.searchNotes(note)

    override suspend fun getAllNotes(): List<Note> = fireStoreService.getAllNotes()

    override suspend fun insertOrUpdateNotes(notes: List<Note>) =
        fireStoreService.insertOrUpdateNotes(notes)
}