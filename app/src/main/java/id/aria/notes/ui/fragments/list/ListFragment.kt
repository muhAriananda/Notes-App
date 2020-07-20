package id.aria.notes.ui.fragments.list

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import id.aria.notes.R
import id.aria.notes.data.viewmodels.NoteViewModel
import id.aria.notes.data.viewmodels.SharedViewModel
import kotlinx.android.synthetic.main.fragment_list.*

class ListFragment : Fragment() {

    private val noteViewModel: NoteViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by viewModels()
    private val adapter: ListAdapter by lazy { ListAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rv_notes.adapter = adapter
        rv_notes.layoutManager = LinearLayoutManager(requireActivity())

        noteViewModel.getAllNotes.observe(viewLifecycleOwner, Observer { notes->
            sharedViewModel.checkIfDatabaseEmpty(notes)
            adapter.setData(notes)
        })

        sharedViewModel.emptyDatabase.observe(viewLifecycleOwner, Observer {
            showDatabaseEmptyView(it)
        })

        floatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.action_listFragment_to_addFragment)
        }
    }

    private fun showDatabaseEmptyView(emptyDatabase: Boolean) {
        if (emptyDatabase) {
            img_no_data.visibility = View.VISIBLE
            tv_no_data.visibility =View.VISIBLE
        } else {
            img_no_data.visibility = View.INVISIBLE
            tv_no_data.visibility =View.INVISIBLE
        }
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

    private fun deleteAll() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setNegativeButton("No") { _, _->}
        builder.setPositiveButton("Yes") {_, _->
            noteViewModel.deleteAll()
            Toast.makeText(requireContext(), "Successfully remove everything", Toast.LENGTH_SHORT).show()
        }
        builder.setTitle("Delete Everything")
        builder.setMessage("Are you sure want to delete everything?")
        builder.create().show()
    }
}