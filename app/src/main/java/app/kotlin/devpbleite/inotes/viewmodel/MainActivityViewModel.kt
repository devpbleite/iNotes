package app.kotlin.devpbleite.inotes.viewmodel

import androidx.lifecycle.ViewModel
import app.kotlin.devpbleite.inotes.model.Note
import app.kotlin.devpbleite.inotes.util.Util
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.Query

class MainActivityViewModel : ViewModel() {
    
    private val notesCollectionReference = Util.notesCollectionReference
    
    fun getNoteListOptions(): FirestoreRecyclerOptions<Note> {
        val query: Query = notesCollectionReference
            .orderBy("timestamp", Query.Direction.ASCENDING)
        
        return FirestoreRecyclerOptions.Builder<Note>()
            .setQuery(query, Note::class.java)
            .build()
    }
}
