package com.asifrezan.camera_application.ui

import com.asifrezan.camera_application.models.PhotoType

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.Spinner
import com.asifrezan.camera_application.R


class PhotoTypeSelector @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    private val spinner: Spinner
    private var listener: ((Int, PhotoType) -> Unit)? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.activity_photo_type_selector, this, true)
        spinner = findViewById(R.id.spinner_photo_type)
    }

    fun setItems(items: List<PhotoType>) {
        val adapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, items.map { it.name })
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }

    fun setOnItemSelectedListener(listener: (position: Int, item: PhotoType) -> Unit) {
        this.listener = listener
        spinner.onItemSelectedListener = object : android.widget.AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: android.widget.AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                listener.invoke(position, PhotoType.values()[position])
            }
            override fun onNothingSelected(parent: android.widget.AdapterView<*>?) {}
        }
    }
}