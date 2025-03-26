package com.android.notes_app.feature_note.domain.util

import androidx.room.TypeConverter
import com.android.notes_app.feature_note.domain.model.NoteStatus

class NoteStatusConverter {
    @TypeConverter
    fun fromNoteStatus(status: NoteStatus): String {
        return status.name
    }

    @TypeConverter
    fun toNoteStatus(status: String): NoteStatus {
        return NoteStatus.valueOf(status)
    }
}