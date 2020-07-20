package id.aria.notes.ui.fragments.update

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import id.aria.notes.R
import id.aria.notes.data.models.Note
import id.aria.notes.data.models.Priority
import id.aria.notes.data.viewmodels.NoteViewModel
import id.aria.notes.data.viewmodels.SharedViewModel
import kotlinx.android.synthetic.main.fragment_update.*

class UpdateFragment : Fragment() {

    private val args by navArgs<UpdateFragmentArgs>()
    private val sharedViewModel: SharedViewModel by viewModels()
    private val noteViewModel: NoteViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_update, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val note = args.currentItem
        edt_current_title.setText(note.title)
        edt_current_description.setText(note.description)
        spinner_current_priorities.setSelection(sharedViewModel.parsePriorityToInt(note.priority))

        spinner_current_priorities.onItemSelectedListener = sharedViewModel.listener
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_fragment_update, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.menu_update -> updateNote()
            R.id.menu_delete -> deleteNote()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun updateNote() {
        val title = edt_current_title.text.toString()
        val description = edt_current_description.text.toString()
        val priority = spinner_current_priorities.selectedItem.toString()

        val validation = sharedViewModel.validationDataFrom(title, description)
        if (validation) {
            val noteUpdate = Note(
                args.currentItem.id,
                title,
                sharedViewModel.parsePriority(priority),
                description
            )
            noteViewModel.updateNote(noteUpdate)
            Toast.makeText(requireContext(), "Update Successfully", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)

        }else {
            Toast.makeText(requireContext(), "Please fill out fields", Toast.LENGTH_SHORT).show()
        }
    }

    private fun deleteNote() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setNegativeButton("No") { _, _->}
        builder.setPositiveButton("Yes") {_, _->
            noteViewModel.deleteNote(args.currentItem)
            Toast.makeText(requireContext(), "Successfully remove ${args.currentItem.title}", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        }
        builder.setTitle("Delete ${args.currentItem.title}")
        builder.setMessage("Are you sure want to delete ${args.currentItem.title}?")
        builder.create().show()
    }
}