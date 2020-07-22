package id.aria.notes.ui.fragments.add

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import id.aria.notes.R
import id.aria.notes.data.models.Note
import id.aria.notes.data.viewmodel.NoteViewModel
import id.aria.notes.data.viewmodel.SharedViewModel
import kotlinx.android.synthetic.main.fragment_add.*

class AddFragment : Fragment() {

    private val noteViewModel: NoteViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        spinner_priorities.onItemSelectedListener = sharedViewModel.listener
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_fragment_add, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_add) {
            insertNote()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun insertNote() {
        val title = edt_title.text.toString()
        val priority = spinner_priorities.selectedItem.toString()
        val description = edt_description.text.toString()

        val validation = sharedViewModel.validationDataFrom(title, description)
        if (validation) {
            val note = Note(
                0,
                title,
                sharedViewModel.parsePriority(priority),
                description
            )
            noteViewModel.insertNote(note)
            Toast.makeText(requireContext(), "Successfully add!", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_addFragment_to_listFragment)
        } else {
            Toast.makeText(requireContext(), "Please add the field", Toast.LENGTH_SHORT).show()
        }
    }
}