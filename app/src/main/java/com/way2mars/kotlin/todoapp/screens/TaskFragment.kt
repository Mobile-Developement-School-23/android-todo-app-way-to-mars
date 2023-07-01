package com.way2mars.kotlin.todoapp.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
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
    ): View? {
        binding = FragmentTaskBinding.inflate(layoutInflater, container, false)

        viewModel.task.observe(viewLifecycleOwner, Observer {
            binding.editTextTask.setText(it.text)  // .text = myString.toEditable()
            binding.deadlineDate.text = it.deadline.toFormatString()

        })

        binding.deleteButton.setOnClickListener {
            // probably we should check the element
            viewModel.removeTask()
            // leave TaskFragment
            navigator().goBack()
        }

        return binding.root
    }


    // TaskFragment takes parameters ScrollingFragment
    companion object{

        private const val TAG_USER_ID = "TAG_USER_ID"

        fun newInstance(userId: String): TaskFragment{
            val fragment = TaskFragment()
            fragment.arguments = bundleOf(TAG_USER_ID to userId)
            return fragment
        }

    }
}