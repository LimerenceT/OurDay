package com.day.ourday.adapter;

import androidx.recyclerview.widget.DiffUtil;

import com.day.ourday.data.entity.Item;

import java.util.List;

/**
 * Created by long.qiu on 19-8-23
 */
public class ItemDiffCallback extends DiffUtil.Callback {
    private List<Item> oldList;
    private List<Item> newList;

    public ItemDiffCallback(List<Item> oldList, List<Item> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).getId() == newList.get(newItemPosition).getId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return false;
    }
}
