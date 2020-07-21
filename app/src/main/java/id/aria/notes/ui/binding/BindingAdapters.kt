package id.aria.notes.ui.binding

import android.view.View
import android.widget.Spinner
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import id.aria.notes.R
import id.aria.notes.data.models.Note
import id.aria.notes.data.models.Priority
import id.aria.notes.ui.fragments.list.ListFragmentDirections

class BindingAdapters {

    companion object {

        @BindingAdapter("android:navigateToAddFragment")
        @JvmStatic
        fun navigateToAddFragment(view: FloatingActionButton, navigate: Boolean) {
            view.setOnClickListener {
                if (navigate) {
                    view.findNavController().navigate(R.id.action_listFragment_to_addFragment)
                }
            }
        }

        @BindingAdapter("android:emptyDatabase")
        @JvmStatic
        fun emptyDatabase(view: View, emptyDatabase: MutableLiveData<Boolean>) {
            when (emptyDatabase.value) {
                false -> view.visibility = View.INVISIBLE
                true -> view.visibility = View.VISIBLE
            }
        }

        @BindingAdapter("android:parsePriority")
        @JvmStatic
        fun parsePriority(view: Spinner, priority: Priority) {
            when (priority) {
                Priority.LOW -> {
                    view.setSelection(0)
                }
                Priority.MEDIUM -> {
                    view.setSelection(1)
                }
                Priority.HIGH -> {
                    view.setSelection(2)
                }
            }
        }

        @BindingAdapter("android:setColorPriority")
        @JvmStatic
        fun setColorPriority(view: CardView, priority: Priority) {
            when(priority) {
                Priority.LOW -> {view.setCardBackgroundColor(ContextCompat.getColor(view.context, R.color.green))}
                Priority.MEDIUM -> {view.setCardBackgroundColor(ContextCompat.getColor(view.context, R.color.yellow))}
                Priority.HIGH -> {view.setCardBackgroundColor(ContextCompat.getColor(view.context, R.color.red))}
            }
        }

        @BindingAdapter("sendDataListToUpdateFragment")
        @JvmStatic
        fun sendDataFromListToUpdateFragment(view: CardView, note: Note) {
            view.setOnClickListener {
                val action = ListFragmentDirections.actionListFragmentToUpdateFragment(note)
                view.findNavController().navigate(action)
            }
        }

    }
}