package com.example.fundonotes_mvvm_dao.Model.Dao

import android.content.ContentValues
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.example.fundonotes_mvvm_dao.Model.Note
import com.example.fundonotes_mvvm_dao.Model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class FirebaseNotesDataLayer: NotesDataLayer {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseDatabase: DatabaseReference
    private lateinit var noteArrayList: ArrayList<Note>


    init {
        initService()

    }
    private fun initService() {
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance().getReference("Users")
        noteArrayList = arrayListOf<Note>()
    }

    override fun createNote(note:Note) {
        val uid = firebaseAuth.uid
        val notes = "notes"
        val noteId = "${UUID.randomUUID()}"
        firebaseDatabase.child(uid!!).child(notes).child(noteId).setValue(note)
            .addOnCompleteListener() {
                if (it.isSuccessful) {
                    it.addOnSuccessListener {
                        val hashMap: HashMap<String, Any?> = HashMap()
                        hashMap["userId"] = uid
                        hashMap["noteId"] = noteId
                        firebaseDatabase.child(uid).child(notes).child(noteId).updateChildren(hashMap)
                    }
                } else {
                }
            }
    }

    override fun updateNote(noteId : String, note: Note) {
        val uid = firebaseAuth.uid
        val hashMap: HashMap<String, Any?> = HashMap()
        hashMap["noteTitle"] = note.noteTitle
        hashMap["noteSubtitle"] = note.noteSubtitle
        hashMap["noteContent"] = note.noteContent
        var calendar = Calendar.getInstance()
        var simpleDateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
        hashMap["timeStamp"] = simpleDateFormat.format(calendar.time).toString()
        firebaseDatabase.child(uid!!).child("notes").child(noteId).updateChildren(hashMap)
    }

    override fun readNote(noteId : String) : Note {
        val uid = firebaseAuth.uid
        var title = ""
        var subTitle = ""
        var content = ""
        var label = ""
        var timeStamp = ""
        val note = Note(noteTitle = title, noteSubtitle = subTitle, noteContent = content, noteLabel= label, timeStamp = timeStamp)

        firebaseDatabase.child(uid!!).child("notes").child(noteId).get().addOnSuccessListener{
            if(it.exists()){
                note.noteTitle = it.child("noteTitle").value.toString()
                note.noteSubtitle = it.child("noteSubtitle").value.toString()
                note.noteContent = it.child("noteContent").value.toString()
                note.noteLabel = it.child("noteLabel").value.toString()
                note.timeStamp = it.child("timeStamp").value.toString()
            }
            else{
            }
        }
        return note
    }

    override fun deleteNote(noteId : String) {
        val uid = firebaseAuth.uid
        firebaseDatabase.child(uid!!).child("notes").child(noteId).removeValue()
    }

    override fun getAllNotes():ArrayList<Note> {
        val uid = firebaseAuth.uid
        firebaseDatabase.child(uid!!).child("notes")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    noteArrayList.clear()
                    if (snapshot.exists()) {
                        for (userSnapshot in snapshot.children) {
                            val userNote = userSnapshot.getValue(Note::class.java)
                            noteArrayList.add(userNote!!)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                    Log.d(ContentValues.TAG, "usernote display:failure")
                }
            })
        return noteArrayList
    }
}