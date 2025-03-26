package com.android.notes_app.feature_note.presentation.util

import kotlinx.serialization.Serializable

object Screen{
    @Serializable
    data object NotesScreen
    @Serializable
    data class AddEditNoteScreen(var noteId: Int = -1 ,var noteColor : Int = -1 )
}
