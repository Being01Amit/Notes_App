package com.android.notes_app.feature_note.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.android.notes_app.feature_note.domain.model.Note
import com.android.notes_app.feature_note.domain.util.NoteStatusConverter

@Database(
    entities = [Note::class],
    version = 1
)
@TypeConverters(NoteStatusConverter::class)
abstract class NoteDatabase: RoomDatabase() {

    abstract val noteDao: NoteDao

    companion object {
        const val DATABASE_NAME = "notes_db"
    }
}