package com.example.cleannotes.business.data.network

import com.example.cleannotes.business.data.cache.CacheErrors
import com.example.cleannotes.business.data.network.NetworkErrors.NETWORK_DATA_NULL
import com.example.cleannotes.business.data.network.NetworkErrors.NETWORK_ERROR
import com.example.cleannotes.business.domain.state.*

abstract class ApiResponseHandler<ViewState, Data>(
    private val response: ApiResult<Data>?,
    private val stateEvent: StateEvent
) {
    suspend fun getResult(): DataState<ViewState> {
        return when (response) {
            is ApiResult.GenericError -> {
                DataState.error(
                    response = Response(
                        message = "${stateEvent?.errorInfo()}\n\nReason: ${response.errorMessage}",
                        uiComponentType = UIComponentType.Dialog(),
                        messageType = MessageType.Error()
                    ),
                    stateEvent = stateEvent
                )
            }
            is ApiResult.NetworkError -> {
                DataState.error(
                    response = Response(
                        message = "${stateEvent?.errorInfo()}\n\nReason: ${NETWORK_ERROR}",
                        uiComponentType = UIComponentType.Dialog(),
                        messageType = MessageType.Error()
                    ),
                    stateEvent = stateEvent
                )
            }
            is ApiResult.Success -> {
                if (response.value == null) {
                    DataState.error(
                        response = Response(
                            message = "${stateEvent?.errorInfo()}\n\nReason:${NETWORK_DATA_NULL}",
                            uiComponentType = UIComponentType.Dialog(),
                            messageType = MessageType.Error()
                        ),
                        stateEvent = stateEvent
                    )
                } else {
                    handleSuccess(resultObj = response.value)
                }
            }

            else -> {
                DataState.error(
                    response = Response(
                        message = "${stateEvent?.errorInfo()}\n\nReason: ${NETWORK_ERROR}",
                        uiComponentType = UIComponentType.Dialog(),
                        messageType = MessageType.Error()
                    ),
                    stateEvent = stateEvent
                )
            }
        }
    }

    abstract fun handleSuccess(resultObj: Data): DataState<ViewState>
}