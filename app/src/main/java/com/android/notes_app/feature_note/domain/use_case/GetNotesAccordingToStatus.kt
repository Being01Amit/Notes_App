package com.android.notes_app.feature_note.domain.use_case

import com.android.notes_app.feature_note.domain.model.Note
import com.android.notes_app.feature_note.domain.model.NoteStatus
import com.android.notes_app.feature_note.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetNotesAccordingToStatus(
    private val noteRepository: NoteRepository
) {
    operator fun invoke(
        noteStatus: NoteStatus
    ): Flow<List<Note>> {
        return noteRepository.getNotes().map { notes ->
            when (noteStatus) {
                NoteStatus.PENDING -> notes.filter { it.noteStatus == NoteStatus.PENDING }
                NoteStatus.COMPLETED -> notes.filter { it.noteStatus == NoteStatus.COMPLETED }
            }
        }
    }
}