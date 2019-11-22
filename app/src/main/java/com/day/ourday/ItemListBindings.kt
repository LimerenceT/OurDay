package com.day.ourday

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.day.ourday.adapter.ItemListAdapter
import com.day.ourday.data.entity.Item

/**
 * [BindingAdapter]s for the [Task]s list.
 */
@BindingAdapter("app:items")
fun setItems(listView: RecyclerView, items: List<Item>) {
    (listView.adapter as ItemListAdapter).updateItems(items)
}