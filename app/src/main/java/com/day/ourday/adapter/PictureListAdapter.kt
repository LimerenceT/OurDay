package com.day.ourday.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.day.ourday.BR
import com.day.ourday.databinding.PictureBinding
import com.day.ourday.viewmodel.PictureViewModel

/**
 * Create by LimerenceT on 2019/12/26
 */
class PictureListAdapter(private val pictureViewModel: PictureViewModel) : RecyclerView.Adapter<PictureListAdapter.PictureViewHolder>(){
    private var pictureList:List<String> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PictureViewHolder {
        val dataBinding = PictureBinding.inflate(LayoutInflater.from(parent.context), null, false)
        return PictureViewHolder(dataBinding)
    }

    override fun getItemCount(): Int {
        return pictureList.size
    }

    fun updateList(pictureFileList: List<String>) {
        pictureList= pictureFileList
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: PictureViewHolder, position: Int) {
        val picturePath = pictureList[position]
        with(holder.mBinding) {
            setVariable(BR.picturePath, picturePath)
            setClickListener {
                pictureViewModel.mainBgPicturePath.value = holder.mBinding.picturePath
            }
            executePendingBindings()
        }
    }


    class PictureViewHolder(val mBinding: PictureBinding) : RecyclerView.ViewHolder(mBinding.root)
}

