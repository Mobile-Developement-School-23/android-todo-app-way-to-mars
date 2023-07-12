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
import com.way2mars.kotlin.todoapp.adapter.TodoItemActionListener
import com.way2mars.kotlin.todoapp.adapter.TodoRecyclerAdapter
import com.way2mars.kotlin.todoapp.databinding.FragmentScrollingBinding
import com.way2mars.kotlin.todoapp.model.TodoItem
import kotlin.properties.Delegates


class ScrollingFragment : Fragment() {

    private lateinit var binding: FragmentScrollingBinding
    private lateinit var adapter: TodoRecyclerAdapter

    private val viewModel: ScrollingViewModel by viewModels { factory() }
//    private val todoItemsRepository: TodoItemsRepository
//        get() = (activity?.applicationContext as TodoApplication).repository
//
//    private val listener: TodoItemListener = { tasks: List<TodoItem> ->
//        adapter.tasks = tasks
//    }
    private var eyeState by Delegates.notNull<Boolean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        eyeState = savedInstanceState?.getBoolean(KEY_EYE_BUTTON_STATE) ?: true
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
                Log.d(TAG, "OnGetInfo")
                contract().showDetailsScreen(todoItem)
            }

            override fun onRemove(todoItem: TodoItem) {
                Snackbar.make(binding.root, "Заметка удалена", Snackbar.LENGTH_SHORT).show()
                Log.d(TAG, "adapter :: onRemove")
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
            Log.d(TAG, "FAB onClick")
            contract().createNewTask()
        }

        binding.eyeButton.setOnCheckedChangeListener{ view, isChecked ->
            Log.d(TAG, "Eye button - onCheckedChangeListener")
            viewModel.setFilter(isChecked)
            eyeState = isChecked
        }
        binding.eyeButton.isChecked = eyeState

        return binding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(KEY_EYE_BUTTON_STATE, eyeState)
        Log.d(TAG, "onSaveInstanceState")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(TAG, "onDestroyView")
    }

    companion object{
        private const val KEY_EYE_BUTTON_STATE = "KEY_EYE_BUTTON_STATE"

        @JvmStatic private val TAG = ScrollingFragment::class.java.simpleName
    }
}