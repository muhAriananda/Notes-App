package id.aria.notes.data.viewmodels

import android.app.Application
import android.text.TextUtils
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import id.aria.notes.R
import id.aria.notes.data.models.Note
import id.aria.notes.data.models.Priority

class SharedViewModel(application: Application) : AndroidViewModel(application) {

    val emptyDatabase: MutableLiveData<Boolean> = MutableLiveData(true)

    fun checkIfDatabaseEmpty(notes: List<Note>) {
        emptyDatabase.value = notes.isEmpty()
    }

    val listener: AdapterView.OnItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) {}
        override fun onItemSelected(
            parent: AdapterView<*>?,
            view: View?,
            position: Int,
            id: Long
        ) {

            when (position) {
                0 -> {
                    (parent?.getChildAt(0) as TextView).setTextColor(
                        ContextCompat.getColor(
                            application,
                            R.color.green
                        )
                    )
                }
                1 -> {
                    (parent?.getChildAt(0) as TextView).setTextColor(
                        ContextCompat.getColor(
                            application,
                            R.color.yellow
                        )
                    )
                }
                2 -> {
                    (parent?.getChildAt(0) as TextView).setTextColor(
                        ContextCompat.getColor(
                            application,
                            R.color.red
                        )
                    )
                }
            }
        }
    }

    fun validationDataFrom(title: String, description: String): Boolean {
        return if (TextUtils.isEmpty(title) || TextUtils.isEmpty(description)) {
            false
        } else !(title.isEmpty() || description.isEmpty())
    }

    fun parsePriority(priority: String): Priority {
        return when (priority) {
            "High priority" -> {
                Priority.HIGH
            }
            "Medium priority" -> {
                Priority.MEDIUM
            }
            "Low priority" -> {
                Priority.LOW
            }
            else -> {
                Priority.LOW
            }
        }
    }

    fun parsePriorityToInt(priority: Priority): Int {
        return when(priority) {
            Priority.LOW -> 0
            Priority.MEDIUM -> 1
            Priority.HIGH -> 2
        }
    }
}