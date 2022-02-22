package com.example.cleannotes.business.interactors.notelist

import com.example.cleannotes.business.data.cache.CacheResponseHandler
import com.example.cleannotes.business.data.cache.abstraction.NoteCacheDataSource
import com.example.cleannotes.business.data.network.abstraction.NoteNetworkDataSource
import com.example.cleannotes.business.data.util.safeApiCall
import com.example.cleannotes.business.data.util.safeCacheCall
import com.example.cleannotes.business.domain.model.Note
import com.example.cleannotes.business.domain.model.NoteFactory
import com.example.cleannotes.business.domain.state.*
import com.example.cleannotes.framework.presentation.notelist.state.NoteListViewState
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.*

class InsertNewNote(
    private val noteCacheDataSource: NoteCacheDataSource,
    private val noteNetworkDataSource: NoteNetworkDataSource,
    private val noteFactory: NoteFactory
) {

    fun insertNewNote(
        id: String? = null,
        title: String,
        stateEvent: StateEvent
    ): Flow<DataState<NoteListViewState>> = flow {

        val newNote = noteFactory.createSingleNote(
            id = id ?: UUID.randomUUID().toString(),
            title = title, body = ""
        )
        val cacheResult = safeCacheCall(IO)
        {
            noteCacheDataSource.insertNote(newNote)
        }
        //  var cacheResponse: DataState<NoteListViewState>? = null

        val cacheResponse = object : CacheResponseHandler<NoteListViewState, Long>(
            response = cacheResult,
            stateEvent = stateEvent

        ) {
            override fun handleSuccess(resultObj: Long): DataState<NoteListViewState> {
                return if (resultObj > 0) {
                    val viewState = NoteListViewState(newNote = newNote)
                    DataState.data(
                        response = Response(
                            message = INSERT_NOTE_SUCCESS,
                            uiComponentType = UIComponentType.Toast(),
                            messageType = MessageType.Success()

                        ), data = viewState, stateEvent = stateEvent
                    )

                } else {
                    DataState.data(
                        response = Response(
                            message = INSERT_NOTE_FAILED,
                            uiComponentType = UIComponentType.Toast(),
                            messageType = MessageType.Error()

                        ), data = null, stateEvent = stateEvent
                    )
                }
            }
        }.getResult()




        emit(cacheResponse)
        updateNetwork(cacheResponse.stateMessage?.response?.message, newNote)


    }

    private suspend fun updateNetwork(cacheResponse: String?, newNote: Note) {
        if (cacheResponse.equals(INSERT_NOTE_SUCCESS)) {
            safeApiCall(IO)
            {
                noteNetworkDataSource.insertOrUpdateNote(newNote)
            }
        }

    }

    companion object {
        const val INSERT_NOTE_SUCCESS = "successfully inserted new note "
        const val INSERT_NOTE_FAILED = "Failed to inserted new note "
    }

}