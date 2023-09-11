package app.kotlin.devpbleite.inotes.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.kotlin.devpbleite.inotes.databinding.RecyclerNoteItemBinding
import app.kotlin.devpbleite.inotes.model.Note
import app.kotlin.devpbleite.inotes.util.Util.timestampToString
import app.kotlin.devpbleite.inotes.view.NoteDetailsActivity
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class NoteAdapter(
    options: FirestoreRecyclerOptions<Note>,
    private val context: Context
) : FirestoreRecyclerAdapter<Note, NoteAdapter.NoteViewHolder>(options) {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val binding = RecyclerNoteItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoteViewHolder(binding)
    }
    
    override fun onBindViewHolder(holder: NoteViewHolder, position: Int, note: Note) {
        holder.bind(note, position)
    }
    
    inner class NoteViewHolder(private val binding: RecyclerNoteItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        
        fun bind(note: Note, position: Int) {
            binding.noteTitleText.text = note.title
            binding.noteContentText.text = note.content
            binding.noteTimestampText.text = timestampToString(note.timestamp!!)
            
            binding.root.setOnClickListener {
                val intent = Intent(context, NoteDetailsActivity::class.java)
                intent.putExtra("title", note.title)
                intent.putExtra("content", note.content)
                val docID = snapshots.getSnapshot(position).id
                intent.putExtra("docID", docID)
                context.startActivity(intent)
            }
        }
    }
}
