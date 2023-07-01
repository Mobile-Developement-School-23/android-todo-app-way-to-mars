package com.way2mars.kotlin.todoapp.adapter

import android.text.SpannableString
import android.text.style.StrikethroughSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.way2mars.kotlin.todoapp.R
import com.way2mars.kotlin.todoapp.TodoApplication
import com.way2mars.kotlin.todoapp.databinding.TodoItemViewBinding
import com.way2mars.kotlin.todoapp.model.Importance
import com.way2mars.kotlin.todoapp.model.TodoItem
import com.way2mars.kotlin.todoapp.model.TodoItemsRepository
import com.way2mars.kotlin.todoapp.utils.toFormatString
import java.util.PrimitiveIterator

interface TodoItemActionListener{
    fun onMarkDone(todoItem: TodoItem)
    fun onGetInfo(todoItem: TodoItem)
    fun onRemove(todoItem: TodoItem)
    fun onUserMove(todoItem: TodoItem, moveBy: Int)
}


class TodoDiffCallback(
    private val oldList: List<TodoItem>,
    private val newList: List<TodoItem>,
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldTask = oldList[oldItemPosition]
        val newTask = newList[newItemPosition]
        return oldTask.id == newTask.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldTask = oldList[oldItemPosition]
        val newTask = newList[newItemPosition]
        return oldTask == newTask
    }
}


class TodoRecyclerAdapter(private val todoItemActionListener: TodoItemActionListener ) : RecyclerView.Adapter<TodoRecyclerAdapter.TodoViewHolder>(), View.OnClickListener {

    var container: List<TodoItem> = emptyList()
        set(newData){
            val diffCallback = TodoDiffCallback(field, newData)
            val diffResult = DiffUtil.calculateDiff(diffCallback, true)
            field = newData
            diffResult.dispatchUpdatesTo(this)
        }

    inner class TodoViewHolder(val binding: TodoItemViewBinding) : RecyclerView.ViewHolder(binding.root) {
        private val checkBox: CheckBox = itemView.findViewById(R.id.item_checkbox)
        private val typeImage: ImageView = itemView.findViewById(R.id.item_type_img)
        private val message: TextView = itemView.findViewById(R.id.item_message)
        private val date: TextView = itemView.findViewById(R.id.item_date)

        fun bind(item: TodoItem) {

            if (item.deadline == null)
                date.visibility = View.GONE
            else {
                date.visibility = View.VISIBLE
                date.text = item.deadline.toFormatString()
            }

            when (item.importance) {
                Importance.LOW -> {
                    typeImage.visibility = View.VISIBLE
                    typeImage.setImageDrawable(TodoApplication.imageTypeLow)
                    checkBox.buttonTintList = TodoApplication.colorStateListGreenLight
                }

                Importance.COMMON -> {
                    typeImage.visibility = View.GONE
                    checkBox.buttonTintList = TodoApplication.colorStateListGreenLight
                }

                Importance.HIGH -> {
                    typeImage.visibility = View.VISIBLE
                    typeImage.setImageDrawable(TodoApplication.imageTypeHigh)
                    checkBox.buttonTintList = TodoApplication.colorStateListRed
                }
            }

            checkBox.isChecked = item.isCompleted
            if (item.isCompleted) {
                val spannableString = SpannableString(item.text)
                spannableString.setSpan(
                    StrikethroughSpan(),
                    0,
                    spannableString.length,
                    0
                )
                message.text = spannableString
                message.alpha = 0.4f  // make text gray
            } else {
                message.text = item.text
                message.alpha = 1.0f
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding  = TodoItemViewBinding.inflate(inflater, parent, false)

        return TodoViewHolder(binding)
    }

    override fun getItemCount(): Int = container.size

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        holder.bind(container[position])
    }

    override fun onClick(p0: View?) {
        Unit
    }
}