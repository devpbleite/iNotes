package app.kotlin.devpbleite.inotes

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.kotlin.devpbleite.inotes.NoteAdapter.NoteViewHolder
import app.kotlin.devpbleite.inotes.Util.timestampToString
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class NoteAdapter(
    options: FirestoreRecyclerOptions<Note>,
    private var context: Context
) : FirestoreRecyclerAdapter<Note, NoteViewHolder>(options) {
    override fun onBindViewHolder(
        holder: NoteViewHolder, position: Int,
        note: Note
    ) {
        holder.titleTextView.text = note.title
        holder.contentTextView.text = note.content
        holder.timestampTextView.text =
            timestampToString(
                note.timestamp!!
            )
        
        holder.itemView.setOnClickListener {
            val intent = Intent(context, NoteDetailsActivity::class.java)
            intent.putExtra("title", note.title)
            intent.putExtra("content", note.content)
            val docID = this.snapshots.getSnapshot(position).id
            intent.putExtra("docID", docID)
            context.startActivity(intent)
        }
    }
    
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NoteViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.recycler_note_item, parent, false)
        return NoteViewHolder(view)
    }
    
    inner class NoteViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var titleTextView: TextView
        var contentTextView: TextView
        var timestampTextView: TextView
        
        init {
            titleTextView = itemView.findViewById(R.id.note_title_text)
            contentTextView = itemView.findViewById(R.id.note_content_text)
            timestampTextView = itemView.findViewById(R.id.note_timestamp_text)
        }
    }
}