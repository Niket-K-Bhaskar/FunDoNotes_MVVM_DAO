package com.example.fundonotes_mvvm_dao.Model.Dao

import com.example.fundonotes_mvvm_dao.Model.Note

interface NotesDataLayer {
    abstract fun createNote(note: Note)

    abstract fun updateNote(noteId : String, note: Note)

    abstract fun readNote(noteId : String) : Note

    abstract fun deleteNote(noteId : String)

    abstract fun getAllNotes():ArrayList<Note>
}