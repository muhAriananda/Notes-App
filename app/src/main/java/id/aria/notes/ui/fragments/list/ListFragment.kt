package id.aria.notes.ui.fragments.list

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.snackbar.Snackbar
import id.aria.notes.R
import id.aria.notes.data.models.Note
import id.aria.notes.data.viewmodel.NoteViewModel
import id.aria.notes.data.viewmodel.SharedViewModel
import id.aria.notes.databinding.FragmentListBinding
import id.aria.notes.utils.SwipeToDelete
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator

class ListFragment : Fragment(), SearchView.OnQueryTextListener {

    private val noteViewModel: NoteViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by viewModels()

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    private val adapter: ListAdapter by lazy { ListAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //Set Menu
        setHasOptionsMenu(true)

        //Data binding
        _binding = FragmentListBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.mSharedViewModel = sharedViewModel

        //Setup RecyclerView
        setupRecyclerView()

        //Observer LiveData
        noteViewModel.getAllNotes.observe(viewLifecycleOwner, Observer { notes ->
            sharedViewModel.checkIfDatabaseEmpty(notes)
            adapter.setData(notes)
        })

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_fragment_list, menu)

        val search = menu.findItem(R.id.menu_search)
        val searchView = search.actionView as? SearchView
        searchView?.isSubmitButtonEnabled = true
        searchView?.setOnQueryTextListener(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_delete_all -> deleteAll()
            R.id.menu_high_priority -> noteViewModel.getNotesByHigh.observe(this, Observer { adapter.setData(it)})
            R.id.menu_low_priority -> noteViewModel.getNotesByLow.observe(this, Observer { adapter.setData(it) })
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query != null) {
            searchThroughDatabase(query)
        }
        return true
    }

    override fun onQueryTextChange(query: String?): Boolean {
        if (query != null) {
            searchThroughDatabase(query)
        }
        return true
    }

    //Search Database
    private fun searchThroughDatabase(query: String) {
        val searchQuery = "%$query%"

        noteViewModel.searchNote(searchQuery).observe(viewLifecycleOwner, Observer {notes ->
            notes?.let {
                adapter.setData(it)
            }
        })
    }

    //Alert Dialog and delete all Notes
    private fun deleteAll() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setNegativeButton("No") { _, _ -> }
        builder.setPositiveButton("Yes") { _, _ ->
            noteViewModel.deleteAll()
            Toast.makeText(requireContext(), "Successfully remove everything", Toast.LENGTH_SHORT)
                .show()
        }
        builder.setTitle("Delete Everything")
        builder.setMessage("Are you sure want to delete everything?")
        builder.create().show()
    }

    private fun setupRecyclerView() {
        val recyclerView = binding.rvNotes
        recyclerView.adapter = adapter
        recyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        recyclerView.itemAnimator = SlideInUpAnimator().apply {
            addDuration = 300
        }

        //Set swipe to delete
        swipeToDelete(recyclerView)
    }

    private fun swipeToDelete(recyclerView: RecyclerView) {
        val swipeToDeleteCallback = object : SwipeToDelete() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val noteToDelete = adapter.dataList[viewHolder.adapterPosition]
                //Delete Note
                noteViewModel.deleteNote(noteToDelete)
                adapter.notifyItemRemoved(viewHolder.adapterPosition)

                //Restore Delete
                restoreNoteDelete(viewHolder.itemView, noteToDelete)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun restoreNoteDelete(view: View, noteToDelete: Note) {
        val snackBar = Snackbar.make(
            view,
            "Deleted ${noteToDelete.title}",
            Snackbar.LENGTH_LONG
        )
        snackBar.setAction("Undo") {
            noteViewModel.insertNote(noteToDelete)
        }
        snackBar.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}