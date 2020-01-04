package com.day.ourday

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.day.ourday.adapter.ItemListAdapter
import com.day.ourday.adapter.PictureListAdapter
import com.day.ourday.data.entity.Event
import com.day.ourday.data.entity.Item
import com.day.ourday.util.SystemUtils
import com.day.ourday.util.getFullPath
import java.util.*

/**
 * [BindingAdapter]s for the [Task]s list.
 */
@BindingAdapter("items")
fun setItems(listView: RecyclerView, items: List<Item>?) {
    (listView.adapter as ItemListAdapter).updateItems(if(items.isNullOrEmpty()) Collections.emptyList() else items)
}

@BindingAdapter("pictures")
fun setPictures(listView: RecyclerView, pictureFileList: List<String>?) {
    (listView.adapter as PictureListAdapter).updateList(if(pictureFileList.isNullOrEmpty()) Collections.emptyList() else pictureFileList)
}


@BindingAdapter("loadImage")
fun setImage(view: ImageView, fileName: String?) {
    if (fileName == null) {
        return
    } else {
        // TODO: 19-12-27 cross fade bug
        Glide.with(view.context)
                .asDrawable()
                .load(getFullPath(fileName))
                .transition(withCrossFade())
//                .placeholder(view.drawable)
                .skipMemoryCache(false)
                .into(view)
    }
}

@BindingAdapter("visibility")
fun setVisibility(view: View, event: Event?) {
    view.visibility = if (event?.type==Event.CHANGE) View.VISIBLE else View.GONE
}


@BindingAdapter("statusPadding")
fun setPadding(view: View, height: Int) {
    view.setPadding(0, SystemUtils.getStatusBarHeight(view.context), 0, 0)
}


