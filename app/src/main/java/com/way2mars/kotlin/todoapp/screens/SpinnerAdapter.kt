package com.way2mars.kotlin.todoapp.screens

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.core.content.res.ResourcesCompat
import com.way2mars.kotlin.todoapp.R
import com.way2mars.kotlin.todoapp.TodoApplication
import com.way2mars.kotlin.todoapp.databinding.SpinnerItemBinding
import com.way2mars.kotlin.todoapp.model.Importance

class SpinnerAdapter(
    private val values: List<Importance>,
) : BaseAdapter() {

    private var iconImportanceLow: Drawable? = null
    private var iconImportanceCommon: Drawable? = null
    private var iconImportanceHigh: Drawable? = null

    override fun getCount(): Int = values.size

    override fun getItem(position: Int): Importance = values[position]

    override fun getItemId(position: Int): Long = values[position].hashCode().toLong()

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val binding: SpinnerItemBinding =
            view?.tag as SpinnerItemBinding? ?: createBinding(parent.context)

        when (getItem(position)) {
            Importance.LOW -> {
                binding.spinnerText.text = "Низкая"
                binding.spinnerIcon.setImageDrawable(iconImportanceLow)
            }

            Importance.COMMON -> {
                binding.spinnerText.text = "Обычная"
                binding.spinnerIcon.setImageDrawable(iconImportanceCommon)
            }

            Importance.HIGH -> {
                binding.spinnerText.text = "Высокая"
                binding.spinnerIcon.setImageDrawable(iconImportanceHigh)
            }
        }

        return binding.root
    }

    private fun createBinding(context: Context): SpinnerItemBinding {
        val binding = SpinnerItemBinding.inflate(LayoutInflater.from(context))
        binding.root.tag = binding

        iconImportanceLow = ResourcesCompat.getDrawable(context.resources, R.drawable.priority_low, null)
        iconImportanceCommon = ResourcesCompat.getDrawable(context.resources, R.drawable.priority_common, null)
        iconImportanceHigh = ResourcesCompat.getDrawable(context.resources, R.drawable.priority_high, null)

        return binding
    }

}