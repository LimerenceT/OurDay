package com.day.ourday

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.day.ourday.adapter.ItemListAdapter
import com.day.ourday.adapter.PictureListAdapter
import com.day.ourday.data.entity.Item
import java.io.File
import java.util.*

/**
 * [BindingAdapter]s for the [Task]s list.
 */
@BindingAdapter("app:items")
fun setItems(listView: RecyclerView, items: List<Item>?) {
    (listView.adapter as ItemListAdapter).updateItems(if(items.isNullOrEmpty()) Collections.emptyList() else items)
}

@BindingAdapter("app:pictures")
fun setPictures(listView: RecyclerView, pictureFileList: List<String>?) {
    (listView.adapter as PictureListAdapter).updateList(if(pictureFileList.isNullOrEmpty()) Collections.emptyList() else pictureFileList)
}


@BindingAdapter("app:loadImage")
fun setImage(view: ImageView, picturePath: String) {
    Glide.with(view.context)
            .load(File(view.context.filesDir, picturePath))
            .skipMemoryCache(false)
            .transition(withCrossFade())
            .into(view)
}