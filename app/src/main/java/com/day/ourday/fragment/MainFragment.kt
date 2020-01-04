package com.day.ourday.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import androidx.core.widget.NestedScrollView.OnScrollChangeListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.SimpleCallback
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.day.ourday.BR
import com.day.ourday.R
import com.day.ourday.adapter.ItemListAdapter
import com.day.ourday.data.entity.Event
import com.day.ourday.data.entity.Item
import com.day.ourday.databinding.FragmentMainBinding
import com.day.ourday.util.SharedPreferencesUtils.defaultPrefs
import com.day.ourday.util.SharedPreferencesUtils.set
import com.day.ourday.util.SystemUtils.getStatusBarHeight
import com.day.ourday.viewmodel.ItemViewModel
import com.day.ourday.viewmodel.PictureViewModel
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration.Builder

class MainFragment : Fragment() {
    private lateinit var dataBinding: FragmentMainBinding
    private lateinit var itemViewModel:ItemViewModel
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? { // Inflate the layout for this fragment
        dataBinding = FragmentMainBinding.inflate(inflater, container, false)
        dataBinding.lifecycleOwner = this
        dataBinding.toolbar.setPadding(0, getStatusBarHeight(requireContext()), 0, 0)
        initView()
        initData()
        setListener()
        return dataBinding.root
    }

    private fun initData() {
        itemViewModel = ViewModelProviders.of(this).get(ItemViewModel::class.java)
        val pictureViewModel = ViewModelProviders.of(requireActivity()).get(PictureViewModel::class.java)
        val recyclerViewAdapter = ItemListAdapter()
        recyclerViewAdapter.setViewModel(itemViewModel)
        dataBinding.itemList.itemList.adapter = recyclerViewAdapter
        itemTouchHelper.attachToRecyclerView(dataBinding.itemList.itemList)
        dataBinding.setVariable(BR.itemViewModel, itemViewModel)
        dataBinding.setVariable(BR.pictureViewModel, pictureViewModel)
        dataBinding.lifecycleOwner = this
        val sharedPreferences = defaultPrefs(requireContext())
        dataBinding.blurConfirm.setOnClickListener {
            val progress = dataBinding.blurSeekBar.progress
            sharedPreferences[getString(R.string.blur_progress)] = progress
            dataBinding.blurView.visibility = View.GONE
        }
        pictureViewModel.bgChangeEvent.observe(this, Observer { event: Event ->
            if (event.type == Event.CHANGE) {
                dataBinding.blurSeekBar.progress = 0
            }
        })
    }

    private fun setListener() {
        dataBinding.blurSeekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                val imageView = activity!!.findViewById<ImageView>(R.id.imageView)
                imageView.imageAlpha = 255 - i
                imageView.foreground.alpha = i
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                dataBinding.blurSeekBar.thumb = resources.getDrawable(R.drawable.thumb_press)
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                dataBinding.blurSeekBar.thumb = resources.getDrawable(R.drawable.thumb_normal)
            }
        })
        dataBinding.addItem.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_addItemFragment)
        }
        dataBinding.setting.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_settingFragment)
        }
    }
    private fun initView() {
        dataBinding.appBar.background.alpha = 0
        dataBinding.itemList.itemList.addItemDecoration(
                Builder(activity)
                        .drawable(R.color.colorDivider)
                        .sizeResId(R.dimen.divider)
                        .marginResId(R.dimen.left_margin, R.dimen.right_margin)
                        .build())
        dataBinding.itemList.springNestedScrollview.setOnScrollChangeListener(OnScrollChangeListener { v: NestedScrollView?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int ->
            if (scrollY > 200) {
                dataBinding.appBar.background.alpha = Math.min(255, scrollY)
            } else {
                dataBinding.appBar.background.alpha = Math.max(scrollY - 50, 0)
            }
        })
    }

    private val itemTouchHelper = ItemTouchHelper(object : SimpleCallback(0, ItemTouchHelper.LEFT) {
        override fun onMove(recyclerView: RecyclerView, viewHolder: ViewHolder, target: ViewHolder): Boolean {
            return false
        }

        override fun onSwiped(viewHolder: ViewHolder, direction: Int) {
            val item = viewHolder.itemView.tag as Item
            itemViewModel.delete(item)
            Toast.makeText(activity, "删除成功", Toast.LENGTH_SHORT).show()
        }
    })

    companion object {
        private const val TAG = "MainFragment"
    }


}