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
import com.way2mars.kotlin.todoapp.model.Importance
import com.way2mars.kotlin.todoapp.utils.toFormatString
import java.lang.RuntimeException
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class TaskFragment : Fragment() {

    private lateinit var binding: FragmentTaskBinding
    private val viewModel: TaskViewModel by viewModels { factory() }

    private  lateinit var spinnerAdapter: SpinnerAdapter


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

        spinnerAdapter = SpinnerAdapter(
            listOf(Importance.LOW, Importance.COMMON, Importance.HIGH)
        )  // .context

        binding.importanceSpinner.adapter = spinnerAdapter

        return binding.root
    }

    private fun onDateSwitchPressed() {
        val dialogBinding = CalendarBinding.inflate(layoutInflater)
        var dateString: String? = null
        val dialog = AlertDialog.Builder(this.context)
            .setView(dialogBinding.root)
            .setNegativeButton("ОТМЕНА"){ a, b -> Unit}
            .setPositiveButton("ГОТОВО") { _, _ ->
                dateString?.let {
                    binding.deadlineText.text = it
                }
            }
            .create()
        dialogBinding.calendar.setOnDateChangeListener { _, y, m, d ->
            val date = LocalDate.of(y, m + 1, d)

            val internal_formatter = DateTimeFormatter.ofPattern("E, MMMM yy")
            val external_formatter = DateTimeFormatter.ofPattern("d MMMM yyyy")

            try {
                dateString = date.format(external_formatter)
            }
            catch (e: RuntimeException){
                e.printStackTrace()
            }
            dialogBinding.calendarTitleYear.text = y.toString()
            try {
                dialogBinding.calendarTitleDay.text = date.format(internal_formatter)
            }catch (e: RuntimeException){
                e.printStackTrace()
            }
        }
        dialog.show()
    }


    // TaskFragment takes parameters ScrollingFragment
    companion object {

        private const val TAG_USER_ID = "TAG_USER_ID"

        fun newInstance(userId: String): TaskFragment {
            val fragment = TaskFragment()
            fragment.arguments = bundleOf(TAG_USER_ID to userId)
            return fragment
        }

    }
}