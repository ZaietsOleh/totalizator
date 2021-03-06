package com.kvad.totalizator.event.feed

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kvad.totalizator.event.data.EventRepository
import com.kvad.totalizator.event.data.model.Event
import com.kvad.totalizator.tools.ErrorState
import com.kvad.totalizator.tools.safeapicall.ApiResultWrapper
import com.kvad.totalizator.tools.State
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

typealias EventState = State<List<Event>, ErrorState>

class EventsViewModel @Inject constructor(
    private val eventRepository: EventRepository
) : ViewModel() {
    private val _eventsLiveData = MutableLiveData<EventState>()
    val eventsLiveData = _eventsLiveData

    init {
        _eventsLiveData.value = State.Loading
        viewModelScope.launch {
            updateEvents()
        }
    }

    private suspend fun updateEvents() {
        eventRepository.line.collect {
            it.doOnResult(
                onSuccess = ::onSuccess,
                onError = ::onError
            )
        }
    }

    fun isEventsLoaded() = _eventsLiveData.value is State.Content

    private fun onSuccess(line: List<Event>){
        _eventsLiveData.value = State.Content(line)
    }

    @Suppress("UNUSED_PARAMETER")
    private fun onError(error: ApiResultWrapper.Error){
        _eventsLiveData.value = State.Error(ErrorState.LOADING_ERROR)
    }
}
