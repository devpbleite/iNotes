package app.kotlin.devpbleite.inotes.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.DocumentReference
import app.kotlin.devpbleite.inotes.model.Note
import app.kotlin.devpbleite.inotes.util.Util

class NoteDetailsViewModel : ViewModel() {
    
    sealed class NoteOperationResult {
        object Success : NoteOperationResult()
        data class Error(val message: String) : NoteOperationResult()
    }
    
    private val _resultMessage = MutableLiveData<NoteOperationResult>()
    val resultMessage: LiveData<NoteOperationResult>
        get() = _resultMessage
    
    fun updateNote(docID: String, note: Note) {
        Util.notesCollectionReference.document(docID).set(note)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _resultMessage.value = NoteOperationResult.Success
                } else {
                    _resultMessage.value =
                        NoteOperationResult.Error("Failed to update note")
                }
            }
    }
    
    fun addNewNote(note: Note) {
        val documentReference: DocumentReference = Util.notesCollectionReference.document()
        documentReference.set(note)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _resultMessage.value = NoteOperationResult.Success
                } else {
                    _resultMessage.value =
                        NoteOperationResult.Error("Failed to add note")
                }
            }
    }
    
    fun deleteNote(docID: String) {
        Util.notesCollectionReference.document(docID).delete()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _resultMessage.value = NoteOperationResult.Success
                } else {
                    _resultMessage.value =
                        NoteOperationResult.Error("Failed to delete note")
                }
            }
    }
}
