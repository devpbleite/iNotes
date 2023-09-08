package app.kotlin.devpbleite.inotes

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import app.kotlin.devpbleite.inotes.Util.showToast
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference

class NoteDetailsActivity : AppCompatActivity() {
    
    private var titleEditText: EditText? = null
    private var contentEditText: EditText? = null
    private var saveNoteBtn: ImageButton? = null
    private var pageTitleText: TextView? = null
    private var isEditMode = false
    private var deleteNoteBtn: TextView? = null
    
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_detail)
        titleEditText = findViewById(R.id.note_title_text)
        contentEditText = findViewById(R.id.note_content_text)
        saveNoteBtn = findViewById(R.id.btn_save_note)
        pageTitleText = findViewById(R.id.page_title)
        deleteNoteBtn = findViewById(R.id.delete_note_text)
        
        val title = intent.getStringExtra("title")
        val content = intent.getStringExtra("content")
        val docID = intent.getStringExtra("docID")
        
        if (!docID.isNullOrEmpty()) {
            isEditMode = true
        }
        
        titleEditText!!.setText(title)
        contentEditText!!.setText(content)
        if (isEditMode) {
            pageTitleText!!.text = "Edit your Note."
            deleteNoteBtn!!.visibility = TextView.VISIBLE
        }
        
        saveNoteBtn!!.setOnClickListener { saveNote() }
        deleteNoteBtn!!.setOnClickListener { deleteNoteFromFirebase() }
    }
    
    private fun saveNote() {
        val noteTitle = titleEditText!!.text.toString()
        val noteContent = contentEditText!!.text.toString()
        if (noteTitle.isEmpty()) {
            titleEditText!!.error = "Title cannot be empty"
            return
        }
        val note = Note()
        note.title = noteTitle
        note.content = noteContent
        note.timestamp = Timestamp.now()
        saveNoteToFirebase(note)
    }
    
    private fun saveNoteToFirebase(note: Note) {
        if (isEditMode) {
            val docID = intent.getStringExtra("docID")
            Util.notesCollectionReference.document(docID!!).set(note).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    showToast(this@NoteDetailsActivity, "Note updated successfully!")
                    finish()
                } else {
                    showToast(this@NoteDetailsActivity, "Failed to update note!")
                }
            }
            return
        }
        
        val documentReference: DocumentReference = Util.notesCollectionReference.document()
        documentReference.set(note).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                showToast(this@NoteDetailsActivity, "Note added successfully!")
                finish()
            } else {
                showToast(this@NoteDetailsActivity, "Failed to add note!")
            }
        }
    }
    
    private fun deleteNoteFromFirebase() {
        val docID = intent.getStringExtra("docID")
        Util.notesCollectionReference.document(docID!!).delete().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                showToast(this@NoteDetailsActivity, "Note deleted successfully!")
                finish()
            } else {
                showToast(this@NoteDetailsActivity, "Failed to delete note!")
            }
        }
    }
}