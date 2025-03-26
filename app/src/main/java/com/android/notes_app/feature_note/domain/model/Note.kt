package com.android.notes_app.feature_note.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.android.notes_app.ui.theme.*

@Entity
data class Note(
    val title: String,
    val content: String,
    val dueDate: String,
    val color: Int,
    @PrimaryKey val id: Int? = null,
    val noteStatus : NoteStatus
) {
    companion object {
        val noteColors = listOf(RedOrange, LightGreen, BabyBlue)
    }
}

class InvalidNoteException(message: String): Exception(message)

enum class NoteStatus {
    PENDING,
    COMPLETED
}