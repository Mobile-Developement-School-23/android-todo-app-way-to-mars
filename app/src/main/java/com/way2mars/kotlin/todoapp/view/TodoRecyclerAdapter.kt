package com.way2mars.kotlin.todoapp.view

import android.text.SpannableString
import android.text.style.StrikethroughSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.way2mars.kotlin.todoapp.R
import com.way2mars.kotlin.todoapp.TodoApplication
import com.way2mars.kotlin.todoapp.data.Importance
import com.way2mars.kotlin.todoapp.data.TodoItem
import com.way2mars.kotlin.todoapp.data.TodoItemsRepository
import com.way2mars.kotlin.todoapp.utils.toFormatString

class TodoRecyclerAdapter : RecyclerView.Adapter<TodoRecyclerAdapter.TodoViewHolder>() {

    private val container = TodoItemsRepository()

    inner class TodoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
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
                    typeImage.visibility = View.GONE
                    checkBox.buttonTintList = TodoApplication.colorStateListGreenLight
                }

                Importance.COMMON -> {
                    typeImage.visibility = View.VISIBLE
                    typeImage.setImageDrawable(TodoApplication.imageTypeCommon)
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
        val viewItem =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.todo_item_view, parent, false)
        return TodoViewHolder(viewItem)
    }

    override fun getItemCount(): Int = container.getSize()

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        holder.bind(container.getItem(position))
    }
}