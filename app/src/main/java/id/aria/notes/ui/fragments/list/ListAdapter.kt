package id.aria.notes.ui.fragments.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import id.aria.notes.data.models.Note
import id.aria.notes.databinding.ItemListNoteBinding
import id.aria.notes.utils.NoteDiffUtil

class ListAdapter : RecyclerView.Adapter<ListAdapter.MyViewHolder>() {

    var dataList = emptyList<Note>()

    class MyViewHolder(private val binding: ItemListNoteBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(note: Note) {
            binding.note = note
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemListNoteBinding.inflate(layoutInflater, parent, false)
                return MyViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val note = dataList[position]
        holder.bind(note)
    }

    fun setData(notes: List<Note>) {
        val noteDiffUtil = NoteDiffUtil(dataList, notes)
        val noteDiffUtilResult = DiffUtil.calculateDiff(noteDiffUtil)
        this.dataList = notes
        noteDiffUtilResult.dispatchUpdatesTo(this)
    }
}