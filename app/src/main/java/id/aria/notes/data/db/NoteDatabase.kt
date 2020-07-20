package id.aria.notes.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import id.aria.notes.data.models.Note
import id.aria.notes.utils.Converter

@Database(entities = [Note::class], version = 1)
@TypeConverters(Converter::class)
abstract class NoteDatabase: RoomDatabase() {

    abstract fun noteDao(): NoteDao

    companion object {
        @Volatile
        private var INSTANCE: NoteDatabase? = null

        fun getInstance(context: Context): NoteDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }

            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    NoteDatabase::class.java,
                    "note_database"
                ).build()

                INSTANCE = instance
                return instance
            }
        }
    }

}