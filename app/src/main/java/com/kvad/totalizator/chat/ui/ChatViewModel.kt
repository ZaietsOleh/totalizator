package com.kvad.totalizator.chat.ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kvad.totalizator.chat.data.ChatRepository
import com.kvad.totalizator.chat.model.UserMessage
import com.kvad.totalizator.tools.ErrorState
import com.kvad.totalizator.tools.State
import com.kvad.totalizator.tools.safeapicall.ApiResultWrapper
import com.kvad.totalizator.tools.safeapicall.mapSuccess
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

typealias EventState = State<List<UserMessage>, ErrorState>

class ChatViewModel @Inject constructor(
    private val chatRepository: ChatRepository
) : ViewModel() {

    private val _chatLiveData = MutableLiveData<EventState>()
    val chatLiveData = _chatLiveData

    init {
        _chatLiveData.value = State.Loading
        viewModelScope.launch {
            updateChat()
        }
    }

    private suspend fun updateChat() {
        chatRepository.getMessage()
            //.map { it.mapSuccess () }
            .collect {
                it.doOnResult(
                    onSuccess = ::doOnSuccess,
                    onError = ::doOnError
                )
            }
    }

    private fun doOnError(error: ApiResultWrapper.Error) {
        Log.d("ERROR_TAG", error.msg)
        _chatLiveData.value = State.Error(ErrorState.LOADING_ERROR)
    }

    private fun doOnSuccess(messageList: List<UserMessage>) {
        _chatLiveData.value = State.Content(messageList)
    }
}