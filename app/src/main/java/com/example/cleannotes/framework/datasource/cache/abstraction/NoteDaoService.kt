package com.example.cleannotes.framework.datasource.cache.abstraction

import com.example.cleannotes.business.domain.model.Note
import com.example.cleannotes.framework.datasource.cache.database.NOTE_PAGINATION_PAGE_SIZE

interface NoteDaoService {
    suspend fun insertNote(note: Note): Long
    suspend fun deleteNote(primary: String): Int
    suspend fun deleteNotes(note: List<Note>): Int
    suspend fun updateNote(primary: String, newTitle: String, newBody: String): Int
    suspend fun searchNotes(): List<Note>
    suspend fun searchNotesOrderByDateDESC(
        query: String,
        page: Int,
        pageSize: Int = NOTE_PAGINATION_PAGE_SIZE
    ): List<Note>

    suspend fun searchNotesOrderByDateASC(
        query: String,
        page: Int,
        pageSize: Int = NOTE_PAGINATION_PAGE_SIZE
    ): List<Note>

    suspend fun searchNotesOrderByTitleDESC(
        query: String,
        page: Int,
        pageSize: Int = NOTE_PAGINATION_PAGE_SIZE
    ): List<Note>

    suspend fun searchNotesOrderByTitleASC(
        query: String,
        page: Int,
        pageSize: Int = NOTE_PAGINATION_PAGE_SIZE
    ): List<Note>

    suspend fun searchNoteById(primary: String): Int
    suspend fun getNumNotes(): Int
    suspend fun insertNotes(note: List<Note>): Long


    suspend fun returnOrderedQuery(
        query: String,
        filterAndOrder: String,
        page: Int
    ): List<Note>
}