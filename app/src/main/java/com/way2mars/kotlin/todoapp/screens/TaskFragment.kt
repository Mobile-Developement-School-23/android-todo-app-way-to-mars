package com.way2mars.kotlin.todoapp.screens

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SwitchCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import com.way2mars.kotlin.todoapp.R
import com.way2mars.kotlin.todoapp.databinding.CalendarBinding
import com.way2mars.kotlin.todoapp.databinding.FragmentTaskBinding
import com.way2mars.kotlin.todoapp.model.Importance
import com.way2mars.kotlin.todoapp.utils.toFormatString
import com.way2mars.kotlin.todoapp.utils.toUnixTime
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class TaskFragment : Fragment() {

    private lateinit var binding: FragmentTaskBinding
    private val viewModel: TaskViewModel by viewModels { factory() }

    private lateinit var spinnerAdapter: SpinnerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.loadTask(requireArguments().getString(TAG_USER_ID, ""))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTaskBinding.inflate(layoutInflater, container, false)

        with(binding) {

            spinnerAdapter = SpinnerAdapter(
                listOf(
                    Importance.LOW,
                    Importance.COMMON,
                    Importance.HIGH
                )
            ).also { importanceSpinner.adapter = it }

            viewModel.task.observe(viewLifecycleOwner) {
                editTextTask.setText(it.text)
                deadlineText.text = it.deadline.toFormatString()
                deadlineSwitch.isChecked = it.deadline != null
                importanceSpinner.setSelection(
                    when (it.importance) {
                        Importance.LOW -> 0
                        Importance.COMMON -> 1
                        Importance.HIGH -> 2
                    }
                )
                // Paints delete button gray if .id is empty or makes it red and clickable
                setDeleteButtonState(it.id.isNotEmpty())
            }

            deadlineSwitch.setOnCheckedChangeListener { _, isChecked ->
                deadlineText.visibility = if (isChecked) View.VISIBLE else View.INVISIBLE
            }
            deadlineSwitch.setOnClickListener {
                val switch = it as SwitchCompat
                Snackbar.make(binding.root, switch.isChecked.toString(), Snackbar.LENGTH_SHORT).show()
                if (deadlineText.text.isEmpty())
                    calendarDialog(fromSwitch = true)
            }
            deadlineText.setOnClickListener {
                calendarDialog(fromSwitch = false)
                Snackbar.make(binding.root, deadlineText.text.toString(), Snackbar.LENGTH_SHORT).show()
            }
            closeButton.setOnClickListener { contract().goBack() }
            saveTask.setOnClickListener { saveTask() }
        }
        return binding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        saveIntermediateState()
    }

    private fun saveIntermediateState() {
        with(binding) {
            viewModel.saveIntermediateState(
                text = editTextTask.text.toString(),
                importance = importanceSpinner.selectedItem as Importance,
                deadline = if (deadlineSwitch.isChecked) deadlineText.text.toString().toUnixTime() else null
            )
        }
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
            .setNegativeButton(getString(R.string.calendar_cancel)) { _, _ ->
                if (fromSwitch) binding.deadlineSwitch.isChecked = false
            }
            .setPositiveButton(getString(R.string.calendar_ok)) { _, _ ->
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
    private fun setDeleteButtonState(state: Boolean) {
        val color = if (state) requireContext().getColor(R.color.red)
        else requireContext().getColor(R.color.color_light_gray)
        with(binding.deleteButton) {
            isClickable = state
            setTextColor(color)
            compoundDrawables[0].setTint(color)
            if (state) setOnClickListener {
                // probably we should check the element
                viewModel.removeTask()
                contract().goBack()
            }
        }
    }

    private fun saveTask() {
        Snackbar.make(binding.root, "Saving...", Snackbar.LENGTH_SHORT).show()
        saveIntermediateState()
        viewModel.saveTask()
        contract().goBack()
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