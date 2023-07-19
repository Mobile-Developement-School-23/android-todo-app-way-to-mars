package com.way2mars.kotlin.todoapp.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.way2mars.kotlin.todoapp.R
import com.way2mars.kotlin.todoapp.adapter.TodoItemActionListener
import com.way2mars.kotlin.todoapp.adapter.TodoRecyclerAdapter
import com.way2mars.kotlin.todoapp.databinding.FragmentScrollingBinding
import com.way2mars.kotlin.todoapp.model.TodoItem


class ScrollingFragment : Fragment() {

    private lateinit var binding: FragmentScrollingBinding
    private lateinit var adapter: TodoRecyclerAdapter
    private val viewModel: ScrollingViewModel by viewModels { factory() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentScrollingBinding.inflate(inflater, container, false)
        setupAdapter()

        viewModel.countDone.observe(viewLifecycleOwner) {
            binding.textCountDone.text = getString(R.string.tasks_done, it)
        }

        binding.fab.setOnClickListener {
            contract().createNewTask()
        }

        binding.eyeButton.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setFilter(isChecked)
        }
        binding.eyeButton.isChecked = viewModel.filterState

        return binding.root
    }

    private fun setupAdapter() {
        val layoutManager = LinearLayoutManager(requireContext())

        adapter = TodoRecyclerAdapter(object : TodoItemActionListener {
            override fun onMarkDone(todoItem: TodoItem) {
                viewModel.markDone(todoItem)
            }

            override fun onGetInfo(todoItem: TodoItem) {
                contract().showDetailsScreen(todoItem)
            }

            override fun onRemove(todoItem: TodoItem) {
                Snackbar.make(binding.root, "Заметка удалена", Snackbar.LENGTH_SHORT).show()
                viewModel.removeItem(todoItem)
            }

            override fun onTaskMove(todoItem: TodoItem, moveBy: Int) {
                viewModel.moveItem(todoItem, moveBy)
            }
        })

        binding.todoRecyclerView.layoutManager = layoutManager
        binding.todoRecyclerView.adapter = adapter

        viewModel.tasks.observe(viewLifecycleOwner) {
            adapter.tasks = it
        }
    }

}