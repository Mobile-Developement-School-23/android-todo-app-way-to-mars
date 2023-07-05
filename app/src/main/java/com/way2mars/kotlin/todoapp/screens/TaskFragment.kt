package com.way2mars.kotlin.todoapp.screens

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.way2mars.kotlin.todoapp.databinding.CalendarBinding
import com.way2mars.kotlin.todoapp.databinding.FragmentTaskBinding
import com.way2mars.kotlin.todoapp.utils.toFormatString

class TaskFragment : Fragment() {

    private lateinit var binding: FragmentTaskBinding
    private val viewModel: TaskViewModel by viewModels { factory() }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.loadTask(requireArguments().getString(TAG_USER_ID, ""))  // default = ""
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTaskBinding.inflate(layoutInflater, container, false)

        viewModel.task.observe(viewLifecycleOwner, Observer {
            binding.editTextTask.setText(it.text)  // .text = myString.toEditable()
            binding.deadlineText.text = it.deadline.toFormatString()

        })

        binding.deleteButton.setOnClickListener {
            // probably we should check the element
            viewModel.removeTask()
            // leave TaskFragment
            navigator().goBack()
        }

        binding.deadlineSwitch.setOnClickListener { onDateSwitchPressed() }

        return binding.root
    }

    private fun onDateSwitchPressed() {
        val dialogBinding = CalendarBinding.inflate(layoutInflater)
        var dateString: String? = null
        val dialog = AlertDialog.Builder(this.context)
            .setTitle("Выберите дату")
            .setView(dialogBinding.root)
            .setPositiveButton("Ок") { _, _ ->
                dateString?.let {
                    binding.deadlineText.text = it
                }
            }
            .create()
        dialogBinding.calendar.setOnDateChangeListener { _, y, m, d ->
            dateString = dateStringFromIntegers(
                year = y,
                month = m,
                dayOfMonth = d
            )
        }
        dialog.show()
    }


    // TaskFragment takes parameters ScrollingFragment
    companion object {

        private const val TAG_USER_ID = "TAG_USER_ID"

        private val months = listOf(
            "января",
            "февраля",
            "марта",
            "апреля",
            "мая",
            "июня",
            "июля",
            "августа",
            "сентября",
            "октября",
            "ноября",
            "декабря"
        )

        fun newInstance(userId: String): TaskFragment {
            val fragment = TaskFragment()
            fragment.arguments = bundleOf(TAG_USER_ID to userId)
            return fragment
        }

        fun dateStringFromIntegers(year: Int, month: Int, dayOfMonth: Int): String {
            return "$dayOfMonth ${months[month]} $year"
        }

    }
}