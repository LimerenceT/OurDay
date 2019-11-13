package com.day.ourday.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.day.ourday.BR;
import com.day.ourday.R;
import com.day.ourday.data.entity.Item;
import com.day.ourday.util.DateUtils;

import java.util.ArrayList;
import java.util.List;

public class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.ItemViewHolder> {

    private List<Item> items = new ArrayList<>();

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public void updateItems(List<Item> newItems) {
        ItemDiffCallback itemDiffCallback = new ItemDiffCallback(items, newItems);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(itemDiffCallback);
        diffResult.dispatchUpdatesTo(this);
        setItems(newItems);
    }
    private final View.OnClickListener mOnClickListener = view -> {
        Item item = (Item) view.getTag();
        Context context = view.getContext();


    };


    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewDataBinding dataBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item, parent, false);
        return new ItemViewHolder(dataBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Item item = items.get(position);
        item.setDays(DateUtils.getDays(item.getDate()));
        holder.mBinding.setVariable(BR.item, item);
        holder.mBinding.executePendingBindings();
        holder.itemView.setOnClickListener(mOnClickListener);
        holder.itemView.setTag(item);
    }

    @Override
    public int getItemCount() {
        if (items != null) {
            return items.size();
        }
        return 0;
    }

    class ItemViewHolder<T extends ViewDataBinding> extends RecyclerView.ViewHolder {
        private T mBinding;

        public ItemViewHolder(T binding) {
            super(binding.getRoot());
            this.mBinding = binding;
        }

    }
}