package com.way2mars.kotlin.todoapp.adapter

import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.text.SpannableString
import android.text.style.StrikethroughSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.way2mars.kotlin.todoapp.R
import com.way2mars.kotlin.todoapp.R.*
import com.way2mars.kotlin.todoapp.databinding.ViewTodoItemBinding
import com.way2mars.kotlin.todoapp.model.Importance
import com.way2mars.kotlin.todoapp.model.TodoItem
import com.way2mars.kotlin.todoapp.utils.toFormatString

interface TodoItemActionListener {
    fun onMarkDone(todoItem: TodoItem)
    fun onGetInfo(todoItem: TodoItem)
    fun onRemove(todoItem: TodoItem)
    fun onTaskMove(todoItem: TodoItem, moveBy: Int)
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


class TodoRecyclerAdapter(private val todoItemActionListener: TodoItemActionListener) :
    RecyclerView.Adapter<TodoRecyclerAdapter.TodoViewHolder>(), View.OnClickListener, View.OnLongClickListener {

    var tasks: List<TodoItem> = emptyList()
        set(newData) {
            val diffCallback = TodoDiffCallback(field, newData)
            val diffResult = DiffUtil.calculateDiff(diffCallback, true)
            field = newData
            diffResult.dispatchUpdatesTo(this)
        }

    private lateinit var colorStateListRed: ColorStateList
    private var imageTypeLow: Drawable? = null
    private var imageTypeHigh: Drawable? = null
    private var colorStateListGreenLight: ColorStateList? = null

    inner class TodoViewHolder(private val binding: ViewTodoItemBinding) : RecyclerView.ViewHolder(binding.root) {
        private val checkBox: CheckBox = itemView.findViewById(id.item_checkbox)
        private val typeImage: ImageView = itemView.findViewById(id.item_type_img)
        private val message: TextView = itemView.findViewById(id.item_message)
        private val date: TextView = itemView.findViewById(id.item_date)

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
                    typeImage.setImageDrawable(imageTypeLow)
                    checkBox.buttonTintList = colorStateListGreenLight
                }

                Importance.COMMON -> {
                    typeImage.visibility = View.GONE
                    checkBox.buttonTintList = colorStateListGreenLight
                }

                Importance.HIGH -> {
                    typeImage.visibility = View.VISIBLE
                    typeImage.setImageDrawable(imageTypeHigh)
                    checkBox.buttonTintList = colorStateListRed
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

            // place item in all clickable views
            with(this.binding) {
                itemInfo.tag = item
                checkBox.tag = item
                itemView.tag = item
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ViewTodoItemBinding.inflate(inflater, parent, false)

        binding.root.setOnLongClickListener(this)
        binding.itemInfo.setOnClickListener(this)
        binding.itemCheckbox.setOnClickListener(this)

        colorStateListRed = ColorStateList.valueOf(parent.resources.getColor(R.color.red, null))
        imageTypeLow = ResourcesCompat.getDrawable(parent.resources, R.drawable.arrow, null)
        imageTypeHigh = ResourcesCompat.getDrawable(parent.resources, R.drawable.high_priority, null)
        colorStateListGreenLight = ColorStateList(
            arrayOf(
                intArrayOf(android.R.attr.state_checked),
                intArrayOf(-android.R.attr.state_checked)
            ), intArrayOf(
                ContextCompat.getColor(parent.context, R.color.green_light_theme),
                ContextCompat.getColor(parent.context, R.color.gray_light_light_theme)
            )
        )

        return TodoViewHolder(binding)
    }

    override fun getItemCount(): Int = tasks.size

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        holder.bind(tasks[position])
    }

    override fun onClick(v: View) {
        val task = v.tag as TodoItem

        when (v.id) {
            id.item_info -> {
                todoItemActionListener.onGetInfo(task)
            }

            id.item_checkbox -> {
                todoItemActionListener.onMarkDone(task)
            }
        }
    }

    override fun onLongClick(v: View): Boolean {
        Log.d("111", "Long click")
        when (v.id) {
            id.item_info -> return false
            id.item_checkbox -> return false
            else -> showPopupMenu(v)
        }
        return true
    }


    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(view.context, view)
        val context = view.context
        val task = view.tag as TodoItem
        val position = tasks.indexOfFirst { it.id == task.id }

        popupMenu.menu.add(0, ID_MOVE_UP, Menu.NONE, context.getString(string.popup_menu_up))
            .apply {
                isEnabled = position > 0
            }
        popupMenu.menu.add(0, ID_MOVE_DOWN, Menu.NONE, context.getString(string.popup_menu_down))
            .apply {
                isEnabled = position < tasks.size - 1
            }
        popupMenu.menu.add(0, ID_REMOVE, Menu.NONE, context.getString(string.popup_menu_remove))

        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                ID_MOVE_UP -> {
                    todoItemActionListener.onTaskMove(task, -1)
                }

                ID_MOVE_DOWN -> {
                    todoItemActionListener.onTaskMove(task, 1)
                }

                ID_REMOVE -> {
                    todoItemActionListener.onRemove(task)
                }
            }
            return@setOnMenuItemClickListener true
        }

        popupMenu.show()
    }

    companion object {
        private const val ID_MOVE_UP = 1
        private const val ID_MOVE_DOWN = 2
        private const val ID_REMOVE = 3
    }

}