package com.example.fundonotes_mvvm_dao.Model.Dao

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.fundonotes_mvvm_dao.Model.Note

class SQLiteNotesDataLayer(context: Context)  : NotesDataLayer, SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION)  {

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = ("CREATE TABLE " + TABLENAME + "(" + NOTE_ID + " VARCHAR(500)," + USER_ID + " VARCHAR(500)," + NOTE_TITLE + " VARCHAR(500)," +
                NOTE_SUBTITLE + " VARCHAR(500)," + NOTE_CONTENT + " VARCHAR(2000)," + TIMESTAMP + " VARCHAR(500)" +")")
        db?.execSQL(createTable)
    }
    companion object {
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "EmployeeDatabase"
        private val TABLENAME = "Notes"
        private val NOTE_ID = "noteId"
        private const val USER_ID = "userId"
        private val NOTE_TITLE = "noteTitle"
        private val NOTE_SUBTITLE = "noteSubtitle"
        private val NOTE_CONTENT = "noteContent"
        private val TIMESTAMP = "timeStamp"

    }
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }

    override fun createNote(note: Note) {
        val database = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(NOTE_ID, note.noteId)
        contentValues.put(NOTE_TITLE, note.noteTitle)
        contentValues.put(NOTE_SUBTITLE, note.noteSubtitle)
        contentValues.put(NOTE_CONTENT, note.noteContent)
        contentValues.put(TIMESTAMP, note.timeStamp)
        database.insert(TABLENAME, null, contentValues)

    }

    override fun updateNote(noteId: String, note: Note) {
        TODO("Not yet implemented")
    }

    override fun readNote(noteId: String): Note {
        TODO("Not yet implemented")
    }

    override fun deleteNote(noteId: String) {
        TODO("Not yet implemented")
    }

    override fun getAllNotes(): ArrayList<Note> {
        val list: ArrayList<Note> = ArrayList()
        val db = this.readableDatabase
        val query = "Select * from $TABLENAME"
        val result = db.rawQuery(query, null)
        if (result.moveToFirst()) {
            do {
                val note = Note()
                note.noteId = result.getString(0)
                note.noteId = result.getString(0)
                note.userId = result.getString(0)
                note.noteTitle = result.getString(0)
                note.noteSubtitle = result.getString(0)
                note.noteContent = result.getString(0)
                note.timeStamp = result.getString(0)
                list.add(note)
            }
            while (result.moveToNext())
        }
        return list
    }
}