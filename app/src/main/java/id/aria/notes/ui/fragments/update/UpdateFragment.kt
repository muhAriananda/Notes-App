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
import id.aria.notes.data.viewmodel.NoteViewModel
import id.aria.notes.data.viewmodel.SharedViewModel
import id.aria.notes.databinding.FragmentUpdateBinding
import kotlinx.android.synthetic.main.fragment_update.*

class UpdateFragment : Fragment() {

    private val args by navArgs<UpdateFragmentArgs>()

    private val sharedViewModel: SharedViewModel by viewModels()
    private val noteViewModel: NoteViewModel by viewModels()

    private var _binding: FragmentUpdateBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //Set Menu
        setHasOptionsMenu(true)

        //Data Binding
        _binding = FragmentUpdateBinding.inflate(inflater, container, false)
        binding.arg = args

        binding.spinnerCurrentPriorities.onItemSelectedListener = sharedViewModel.listener

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_fragment_update, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_update -> updateNote()
            R.id.menu_delete -> deleteNote()
        }
        return super.onOptionsItemSelected(item)
    }

    //Update notes
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

        } else {
            Toast.makeText(requireContext(), "Please fill out fields", Toast.LENGTH_SHORT).show()
        }
    }

    //Alert Dialog and for delete dialog
    private fun deleteNote() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setNegativeButton("No") { _, _ -> }
        builder.setPositiveButton("Yes") { _, _ ->
            noteViewModel.deleteNote(args.currentItem)
            Toast.makeText(
                requireContext(),
                "Successfully remove ${args.currentItem.title}",
                Toast.LENGTH_SHORT
            ).show()
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        }
        builder.setTitle("Delete ${args.currentItem.title}")
        builder.setMessage("Are you sure want to delete ${args.currentItem.title}?")
        builder.create().show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}