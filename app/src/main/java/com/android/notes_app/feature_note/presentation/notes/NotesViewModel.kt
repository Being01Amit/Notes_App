package com.android.notes_app.feature_note.presentation.notes

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.android.notes_app.feature_note.domain.model.Note
import com.android.notes_app.feature_note.domain.model.NoteStatus
import com.android.notes_app.feature_note.domain.use_case.NoteUseCases
import com.android.notes_app.feature_note.domain.util.NoteOrder
import com.android.notes_app.feature_note.domain.util.OrderType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases
) : ViewModel() {

    private val _state = mutableStateOf(NotesState())
    val state: State<NotesState> = _state

    private var recentlyDeletedNote: Note? = null

    private var getNotesJob: Job? = null

    private val _selectedStatus = mutableStateOf("All")
    val selectedStatus: State<String> = _selectedStatus

    init {
        getNotes(NoteOrder.Date(OrderType.Descending))
    }

    fun onEvent(event: NotesEvent) {
        when (event) {
            is NotesEvent.Order -> {
                if (state.value.noteOrder::class == event.noteOrder::class &&
                    state.value.noteOrder.orderType == event.noteOrder.orderType
                ) {
                    return
                }
                getNotes(event.noteOrder)
            }
            is NotesEvent.DeleteNote -> {
                viewModelScope.launch {
                    noteUseCases.deleteNote(event.note)
                    recentlyDeletedNote = event.note
                }
            }
            is NotesEvent.RestoreNote -> {
                viewModelScope.launch {
                    noteUseCases.addNote(recentlyDeletedNote ?: return@launch)
                    recentlyDeletedNote = null
                }
            }
            is NotesEvent.ToggleOrderSection -> {
                _state.value = state.value.copy(
                    isOrderSectionVisible = !state.value.isOrderSectionVisible
                )
            }

            is NotesEvent.CompleteNote -> {
                viewModelScope.launch {
                    val updatedNote = event.note.copy(noteStatus = NoteStatus.COMPLETED)
                    noteUseCases.addNote.invoke(updatedNote)
                }
            }
        }
    }

    private fun getNotes(noteOrder: NoteOrder) {
        getNotesJob?.cancel()
        getNotesJob = noteUseCases.getNotes(noteOrder)
            .onEach { notes ->
                _state.value = state.value.copy(
                    notes = notes,
                    noteOrder = noteOrder
                )
            }
            .launchIn(viewModelScope)
    }

    fun onStatusSelected(status: String) {
        _selectedStatus.value = status
        getFilteredNotes(status)
    }

    private fun getFilteredNotes(status: String) {
        getNotesJob?.cancel()
        getNotesJob = when (status) {
            "All" -> noteUseCases.getNotes(NoteOrder.Date(OrderType.Descending))
            "Completed" -> noteUseCases.noteStatus.invoke(NoteStatus.COMPLETED)
            "Pending" -> noteUseCases.noteStatus.invoke(NoteStatus.PENDING)
            else -> flowOf(emptyList()) // Default case to return empty list
        }.onEach { notes ->
            _state.value = state.value.copy(notes = notes)
        }.launchIn(viewModelScope)
    }
}