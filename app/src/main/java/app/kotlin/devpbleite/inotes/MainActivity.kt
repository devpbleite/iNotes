package app.kotlin.devpbleite.inotes

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Query

class MainActivity : AppCompatActivity() {
    
    private var addNoteBtn: FloatingActionButton? = null
    private var recyclerView: RecyclerView? = null
    private var menuBtn: ImageButton? = null
    private var noteAdapter: NoteAdapter? = null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        addNoteBtn = findViewById(R.id.btn_add)
        recyclerView = findViewById(R.id.recycler_view)
        menuBtn = findViewById(R.id.btn_menu_note)
        addNoteBtn!!.setOnClickListener{
            startActivity(
                Intent(
                    this@MainActivity,
                    NoteDetailsActivity::class.java
                )
            )
        }
        menuBtn!!.setOnClickListener { showMenu() }
        setupRecyclerView()
    }
    
    private fun showMenu() {
        val popupMenu = PopupMenu(this@MainActivity, menuBtn)
        popupMenu.menu.add("Logout").setOnMenuItemClickListener {
            if (it.title == "Logout") {
                FirebaseAuth.getInstance().signOut()
                startActivity(
                    Intent(
                        this@MainActivity,
                        LoginActivity::class.java
                    )
                )
                finish()
                return@setOnMenuItemClickListener true
            }
            false
        }
        popupMenu.show()
    }
    
    private fun setupRecyclerView() {
        val query: Query = Util.notesCollectionReference
            .orderBy("timestamp", Query.Direction.DESCENDING)
        val options: FirestoreRecyclerOptions<Note> =
            FirestoreRecyclerOptions.Builder<Note>()
                .setQuery(query, Note::class.java).build()
        recyclerView!!.layoutManager = LinearLayoutManager(this)
        noteAdapter = NoteAdapter(options, this)
        recyclerView!!.adapter = noteAdapter
    }
    
    override fun onStart() {
        super.onStart()
        noteAdapter!!.startListening()
    }
    
    override fun onStop() {
        super.onStop()
        noteAdapter!!.stopListening()
    }
    
    override fun onResume() {
        super.onResume()
        noteAdapter!!.notifyDataSetChanged()
    }
}