package com.example.listadetareas.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.listadetareas.data.Task
import com.example.listadetareas.databinding.ItemTaskBinding
import com.example.listadetareas.utils.TaskDiffUtil

class TaskAdapter(
    var items: List<Task>,
    val onItemClick: (position: Int) -> Unit,
    val onItemCheck: (position: Int) -> Unit,
    val onItemMenu: (position: Int, v: View) -> Unit
): Adapter<TaskViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = items[position]
        holder.render(task)
        holder.itemView.setOnClickListener {
            onItemClick(holder.adapterPosition)
        }
        holder.binding.doneCheckBox.setOnCheckedChangeListener { compoundButton, b ->
            if (holder.binding.doneCheckBox.isPressed) {
                onItemCheck(holder.adapterPosition)
            }
        }
        holder.binding.menuButton.setOnClickListener { view ->
            onItemMenu(holder.adapterPosition, view)
        }
    }

    fun updateItems(items: List<Task>) {
        val diffCallback = TaskDiffUtil(items, this.items)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.items = items
        diffResult.dispatchUpdatesTo(this)
    }
}

class TaskViewHolder(val binding: ItemTaskBinding) : ViewHolder(binding.root) {

    fun render(task: Task) {
        binding.titleTextView.text = task.title
        binding.doneCheckBox.isChecked = task.done
    }
}