package com.way2mars.kotlin.todoapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.way2mars.kotlin.todoapp.R
import com.way2mars.kotlin.todoapp.databinding.FragmentScrollingBinding


class ScrollingFragment : Fragment() {

    private var _binding: FragmentScrollingBinding? = null
    private val binding get() = _binding ?: throw RuntimeException("binding not must be null")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentScrollingBinding.inflate(layoutInflater, container, false)
//        binding.collapsingToolbarLayout.title = "Мои дела"

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // FAB listener
        binding.fab.setOnClickListener {
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        // Eye button listener
//        binding.isVisibleDoneTask.setOnClickListener {
//            bind ing.isVisibleDoneTask.isActivated = !binding.isVisibleDoneTask.isActivated
//            viewModel.isVisibleDone = binding.isVisibleDoneTask.isActivated


        // Task RecyclerView
//        binding.taskList.run {
//            adapter = taskListAdapter
//            layoutManager = LinearLayoutManager(
//                requireContext(),
//                LinearLayoutManager.VERTICAL,
//                false
//            ).apply {
//                reverseLayout = true
//                stackFromEnd = true
//            }
//        }
//        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}