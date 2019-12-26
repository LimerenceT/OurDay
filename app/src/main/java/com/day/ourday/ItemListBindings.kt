package com.day.ourday

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.day.ourday.adapter.ItemListAdapter
import com.day.ourday.data.entity.Item
import java.io.File

/**
 * [BindingAdapter]s for the [Task]s list.
 */
@BindingAdapter("app:items")
fun setItems(listView: RecyclerView, items: List<Item>) {
    (listView.adapter as ItemListAdapter).updateItems(items)
}


@BindingAdapter("app:loadImage")
fun setImage(view: ImageView, picturePath: String) {
    Glide.with(view.context)
            .load(File(view.context.filesDir, picturePath))
            .into(view)
}