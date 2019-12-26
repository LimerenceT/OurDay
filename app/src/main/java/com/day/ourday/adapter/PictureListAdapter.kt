package com.day.ourday.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.day.ourday.BR
import com.day.ourday.R

/**
 * Create by LimerenceT on 2019/12/26
 */
class PictureListAdapter() : RecyclerView.Adapter<PictureListAdapter.PictureViewHolder>(){
    var pictureList = ArrayList<String>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PictureViewHolder {
        val dataBinding = DataBindingUtil.inflate<ViewDataBinding>(LayoutInflater.from(parent.context), R.layout.picture, null, false)
        return PictureViewHolder(dataBinding)
    }

    override fun getItemCount(): Int {
        return pictureList.size
    }

    override fun onBindViewHolder(holder: PictureViewHolder, position: Int) {
        val picturePath = pictureList[position]
        holder.mBinding.setVariable(BR.picturePath, picturePath)
        holder.mBinding.executePendingBindings()
        holder.itemView.tag = picturePath

    }


    class PictureViewHolder(val mBinding: ViewDataBinding) : RecyclerView.ViewHolder(mBinding.root)
}

