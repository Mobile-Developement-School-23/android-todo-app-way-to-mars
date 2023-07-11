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
import com.google.android.material.snackbar.Snackbar
import com.way2mars.kotlin.todoapp.R
import com.way2mars.kotlin.todoapp.databinding.CalendarBinding
import com.way2mars.kotlin.todoapp.databinding.FragmentTaskBinding
import com.way2mars.kotlin.todoapp.model.Importance
import com.way2mars.kotlin.todoapp.utils.toFormatString
import com.way2mars.kotlin.todoapp.utils.toUnixTime
import java.lang.RuntimeException
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class TaskFragment : Fragment() {

    private lateinit var binding: FragmentTaskBinding
    private val viewModel: TaskViewModel by viewModels { factory() }

    private lateinit var spinnerAdapter: SpinnerAdapter

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

        with(binding) {
            viewModel.task.observe(viewLifecycleOwner, Observer {
                editTextTask.setText(it.text)
                deadlineText.text = it.deadline.toFormatString()
            })

            deleteButton.setOnClickListener {
                // probably we should check the element
                viewModel.removeTask()
                contract().goBack()
            }

            deadlineSwitch.setOnCheckedChangeListener { compoundButton, isChecked ->
                if (isChecked) {
                    calendarDialog(fromSwitch = true)
                    deadlineText.visibility = View.VISIBLE
                } else {
                    deadlineText.visibility = View.INVISIBLE
                }
            }

            deadlineText.setOnClickListener { calendarDialog(fromSwitch = false) }

            spinnerAdapter = SpinnerAdapter(
                listOf(Importance.LOW, Importance.COMMON, Importance.HIGH)
            )  // .context

            importanceSpinner.adapter = spinnerAdapter

            // Paints delete button gray if .id is empty or makes it red and clickable
            setDeleteState((viewModel.task.value?.id?.length ?: 0) != 0)
        }
        return binding.root
    }

    private fun calendarDialog(fromSwitch: Boolean) {
        val dialogBinding = CalendarBinding.inflate(layoutInflater)
        var dateString: String? = null

        val titleFiller = { date: LocalDate ->
            val internalFormatter = DateTimeFormatter.ofPattern("E, MMMM d")
            val externalFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy")
            try {
                dateString = date.format(externalFormatter)
            } catch (e: RuntimeException) {
                e.printStackTrace()
            }
            dialogBinding.calendarTitleYear.text = date.year.toString()
            try {
                dialogBinding.calendarTitleDay.text = date.format(internalFormatter)
            } catch (e: RuntimeException) {
                dialogBinding.calendarTitleDay.text = "n/a"
                e.printStackTrace()
            }
        }

        val dialog = AlertDialog.Builder(this.context)
            .setView(dialogBinding.root)
            .setNegativeButton("ОТМЕНА") { dialog, which ->
                if (fromSwitch) binding.deadlineSwitch.isChecked = false
            }
            .setPositiveButton("ГОТОВО") { _, _ ->
                dateString?.let {
                    binding.deadlineText.text = it
                }
            }
            .setOnCancelListener {
                if (fromSwitch) binding.deadlineSwitch.isChecked = false
            }
            .create()
        dialogBinding.calendar.setOnDateChangeListener { _, y, m, d ->
            titleFiller(LocalDate.of(y, m + 1, d))
        }

        val initialDate = binding.deadlineText.text.toString().toUnixTime()
            .let { if (it == 0L) System.currentTimeMillis() else it }

        dialogBinding.calendar.setDate(initialDate, true, true)
        titleFiller(LocalDate.ofEpochDay(dialogBinding.calendar.date / (24 * 60 * 60 * 1000) + 1))

        dialog.show()
    }

    // Makes the "Button" active or inactive
    private fun setDeleteState(state: Boolean) {
        val color = if (state) requireContext().getColor(R.color.red)
        else requireContext().getColor(R.color.color_light_gray)
        with(binding.deleteButton) {
            isClickable = state
            tag = state
            setTextColor(color)
            compoundDrawables[0].setTint(color)
            if (state) setOnClickListener {
                Snackbar.make(binding.root, "Delete", Snackbar.LENGTH_SHORT).show()
            }
        }
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