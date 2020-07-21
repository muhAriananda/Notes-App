package id.aria.notes.ui.fragments.list

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.snackbar.Snackbar
import id.aria.notes.R
import id.aria.notes.data.models.Note
import id.aria.notes.data.viewmodels.NoteViewModel
import id.aria.notes.data.viewmodels.SharedViewModel
import id.aria.notes.databinding.FragmentListBinding
import id.aria.notes.utils.SwipeToDelete
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator

class ListFragment : Fragment() {

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
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_delete_all) {
            deleteAll()
        }
        return super.onOptionsItemSelected(item)
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
                restoreNoteDelete(viewHolder.itemView, noteToDelete, viewHolder.adapterPosition)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun restoreNoteDelete(view: View, noteToDelete: Note, position: Int) {
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