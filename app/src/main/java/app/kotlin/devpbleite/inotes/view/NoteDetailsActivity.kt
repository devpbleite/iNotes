package app.kotlin.devpbleite.inotes.view

import NoteDetailsViewModel
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import app.kotlin.devpbleite.inotes.databinding.ActivityNoteDetailBinding
import app.kotlin.devpbleite.inotes.model.Note
import app.kotlin.devpbleite.inotes.util.Util
import com.google.firebase.Timestamp

class NoteDetailsActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityNoteDetailBinding
    private lateinit var viewModel: NoteDetailsViewModel
    
    private var isEditMode = false
    private var docID: String? = null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNoteDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
    
        viewModel = ViewModelProvider(this).get(NoteDetailsViewModel::class.java)
        
        val titleEditText = binding.noteTitleText
        val contentEditText = binding.noteContentText
        val saveNoteBtn = binding.btnSaveNote
        val deleteNoteBtn = binding.deleteNoteText
        
        val intentExtras = intent.extras
        if (intentExtras != null) {
            isEditMode = intentExtras.getBoolean("isEditMode")
            docID = intentExtras.getString("docID")
            val title = intentExtras.getString("title")
            val content = intentExtras.getString("content")
            titleEditText.setText(title)
            contentEditText.setText(content)
        }
        
        saveNoteBtn.setOnClickListener {
            val noteTitle = titleEditText.text.toString()
            val noteContent = contentEditText.text.toString()
            
            if (noteTitle.isEmpty()) {
                titleEditText.error = "Title cannot be empty"
                return@setOnClickListener
            }
            
            val note = Note()
            note.title = noteTitle
            note.content = noteContent
            note.timestamp = Timestamp.now()
            
            if (isEditMode && docID != null) {
                viewModel.updateNote(docID!!, note)
            } else {
                viewModel.addNewNote(note)
            }
        }
        
        deleteNoteBtn.setOnClickListener {
            if (isEditMode && docID != null) {
                viewModel.deleteNote(docID!!)
            }
        }
        
        viewModel.resultMessage.observe(this, Observer { message ->
            Util.showToast(this@NoteDetailsActivity, message.toString())
        })
    }
}
