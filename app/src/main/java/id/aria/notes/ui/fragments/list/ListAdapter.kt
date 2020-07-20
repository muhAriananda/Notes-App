package id.aria.notes.ui.fragments.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import id.aria.notes.R
import id.aria.notes.data.models.Note
import id.aria.notes.data.models.Priority
import kotlinx.android.synthetic.main.item_list_note.view.*

class ListAdapter : RecyclerView.Adapter<ListAdapter.MyViewHolder>() {

    private var dataList = emptyList<Note>()

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_list_note, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val note = dataList[position]

        holder.itemView.apply {
            txt_title.text = note.title
            txt_description.text = note.description

            when (note.priority) {
                Priority.HIGH -> priorities.setCardBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.red
                    )
                )
                Priority.MEDIUM -> priorities.setCardBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.yellow
                    )
                )
                Priority.LOW -> priorities.setCardBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.green
                    )
                )
            }

            setOnClickListener {
                val action = ListFragmentDirections.actionListFragmentToUpdateFragment(note)
                findNavController().navigate(action)
            }
        }
    }

    fun setData(notes: List<Note>) {
        this.dataList = notes
        notifyDataSetChanged()
    }

}