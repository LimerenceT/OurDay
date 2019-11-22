package com.day.ourday.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.day.ourday.BR;
import com.day.ourday.OurDayApplication;
import com.day.ourday.R;
import com.day.ourday.data.entity.Item;
import com.day.ourday.util.DateUtils;
import com.day.ourday.viewmodel.ItemViewModel;

import java.util.ArrayList;
import java.util.List;

public class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.ItemViewHolder> {

    private List<Item> items = new ArrayList<>();
    private ItemViewModel viewModel;


    public void setItems(List<Item> items) {
        this.items = items;
        Item item;
        if (!items.isEmpty()) {
            item = items.get(0);
        } else {
            item = new Item(OurDayApplication.getInstance().getString(R.string.default_name),
                    OurDayApplication.getInstance().getString(R.string.default_date));
        }
        item.setDays(DateUtils.getDays(item.getDate()));
        viewModel.getHeader().postValue(item);
    }

    public void updateItems(List<Item> newItems) {
        ItemDiffCallback itemDiffCallback = new ItemDiffCallback(items, newItems);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(itemDiffCallback);
        diffResult.dispatchUpdatesTo(this);
        setItems(newItems);
    }

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
        holder.itemView.setTag(item);
    }

    @Override
    public int getItemCount() {
        if (items != null) {
            return items.size();
        }
        return 0;
    }

    public void setViewModel(ItemViewModel viewModel) {
        this.viewModel = viewModel;
    }

    class ItemViewHolder<T extends ViewDataBinding> extends RecyclerView.ViewHolder {
        private T mBinding;

        public ItemViewHolder(T binding) {
            super(binding.getRoot());
            this.mBinding = binding;
        }

    }
}