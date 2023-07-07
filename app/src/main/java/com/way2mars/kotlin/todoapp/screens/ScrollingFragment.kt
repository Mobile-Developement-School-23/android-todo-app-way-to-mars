package com.way2mars.kotlin.todoapp.screens

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.way2mars.kotlin.todoapp.R
import com.way2mars.kotlin.todoapp.TodoApplication
import com.way2mars.kotlin.todoapp.adapter.TodoItemActionListener
import com.way2mars.kotlin.todoapp.adapter.TodoRecyclerAdapter
import com.way2mars.kotlin.todoapp.databinding.FragmentScrollingBinding
import com.way2mars.kotlin.todoapp.model.TodoItem
import com.way2mars.kotlin.todoapp.model.TodoItemListener
import com.way2mars.kotlin.todoapp.model.TodoItemsRepository
import kotlin.random.Random


class ScrollingFragment : Fragment() {

    private lateinit var binding: FragmentScrollingBinding
    private lateinit var adapter: TodoRecyclerAdapter

    private val viewModel: ScrollingViewModel by viewModels { factory() }
    private val todoItemsRepository: TodoItemsRepository
        get() = (activity?.applicationContext as TodoApplication).repository

    private val listener: TodoItemListener = { tasks: List<TodoItem> ->
        adapter.tasks = tasks
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View {

        binding = FragmentScrollingBinding.inflate(inflater, container, false)
        adapter = TodoRecyclerAdapter(object : TodoItemActionListener {
            override fun onMarkDone(todoItem: TodoItem) {
                viewModel.markDone(todoItem)
            }

            override fun onGetInfo(todoItem: TodoItem) {
                Snackbar.make(binding.root, "Get Info", Snackbar.LENGTH_SHORT).show()
                contract().showDetailsScreen(todoItem)
            }

            override fun onRemove(todoItem: TodoItem) {
                Snackbar.make(binding.root, "Remove", Snackbar.LENGTH_SHORT).show()
                viewModel.removeItem(todoItem)
            }

            override fun onTaskMove(todoItem: TodoItem, moveBy: Int) {
                viewModel.moveItem(todoItem, moveBy)
            }
        })

        viewModel.tasks.observe(viewLifecycleOwner, Observer {
            adapter.tasks = it
            binding.textCountDone.text = getString(R.string.tasks_done, viewModel.countDone)
        })

        val layoutManager = LinearLayoutManager(requireContext())
        binding.todoRecyclerView.layoutManager = layoutManager
        binding.todoRecyclerView.adapter = adapter


        // FAB listener
        binding.fab.setOnClickListener {
            Snackbar.make(binding.root, "Add new task", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        binding.eyeButton.setOnClickListener {
            Snackbar.make(binding.root, "Eye button", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
            viewModel.setFilter(binding.eyeButton.isChecked)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}