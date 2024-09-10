package com.vikaspogu.count2date.ui.home

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.glance.appwidget.updateAll
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vikaspogu.count2date.data.model.Event
import com.vikaspogu.count2date.data.repository.EventRepository
import com.vikaspogu.count2date.ui.utils.getSystemTimeInMillsAtMidNight
import com.vikaspogu.count2date.ui.widget.DetailsWidget
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val eventRepository: EventRepository,
    @ApplicationContext private val appContext: Context
) :
    ViewModel() {

    val eventsUiState: StateFlow<EventsUiState> =
        eventRepository.getAllEventsByDate(getSystemTimeInMillsAtMidNight()).map { EventsUiState(it) }.stateIn(
            scope = viewModelScope, started = SharingStarted.WhileSubscribed(
                TIMEOUT_MILLIS
            ), initialValue = EventsUiState()
        )
    private val _eventDate = mutableLongStateOf(getSystemTimeInMillsAtMidNight())
    var eventDate: State<Long> = _eventDate
    fun updateEventDate(date: Long) {
        _eventDate.longValue = date
    }

    private val _eventDescription = mutableStateOf("")
    var eventDescription: State<String> = _eventDescription
    fun updateEventDescription(name: String){
        _eventDescription.value = name
    }

    fun addEvent(event: Event) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                eventRepository.insertEvent(event)
                DetailsWidget().updateAll(context = appContext)
            }
        }
    }

    fun deleteEvent(id: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                eventRepository.deleteEvent(id)
                DetailsWidget().updateAll(context = appContext)
            }
        }
    }

    fun updateEvent(id: Int){
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                eventRepository.updateEvent(Event(id,eventDescription.value,eventDate.value))
                DetailsWidget().updateAll(context = appContext)
            }
        }
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

data class EventsUiState(val eventsList: List<Event> = listOf())