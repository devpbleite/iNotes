package app.kotlin.devpbleite.inotes.view

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.kotlin.devpbleite.inotes.adapter.NoteAdapter
import app.kotlin.devpbleite.inotes.databinding.ActivityMainBinding
import app.kotlin.devpbleite.inotes.viewmodel.MainActivityViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    
    private lateinit var addNoteBtn: FloatingActionButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var menuBtn: ImageButton
    private lateinit var noteAdapter: NoteAdapter
    private lateinit var viewModel: MainActivityViewModel
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        addNoteBtn = binding.btnAdd
        recyclerView = binding.recyclerView
        menuBtn = binding.btnMenuNote
        
        viewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]
        
        addNoteBtn.setOnClickListener{
            startActivity(
                Intent(
                    this@MainActivity,
                    NoteDetailsActivity::class.java
                )
            )
        }
        menuBtn.setOnClickListener { showMenu() }
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
        val options = viewModel.getNoteListOptions()
        
        recyclerView.layoutManager = LinearLayoutManager(this)
        noteAdapter = NoteAdapter(options, this)
        recyclerView.adapter = noteAdapter
    }
    
    override fun onStart() {
        super.onStart()
        noteAdapter.startListening()
    }
    
    override fun onStop() {
        super.onStop()
        noteAdapter.stopListening()
    }
    
    override fun onResume() {
        super.onResume()
        noteAdapter.notifyDataSetChanged()
    }
}
