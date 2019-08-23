package com.day.ourday.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

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
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, int position) {
        Item item = items.get(position);
        holder.mNameView.setText(item.getName());
        holder.mDateView.setText(item.getDate());
        item.setDays(DateUtils.getDays(item.getDate()));
        int days = item.getDays();
        if (days>0) {
            holder.dayAfterOrBefore.setText("天后");
            holder.dayAfterOrBefore.setBackgroundColor(Color.BLUE);
        } else if (days<0) {
            holder.dayAfterOrBefore.setText("天前");
            holder.dayAfterOrBefore.setBackgroundColor(Color.RED);
        } else {
            holder.dayAfterOrBefore.setText("今天");
            holder.dayAfterOrBefore.setBackgroundColor(Color.GRAY);
        }
        holder.mDayView.setText(String.valueOf(Math.abs(days)));
        holder.itemView.setTag(item);
        holder.itemView.setOnClickListener(mOnClickListener);
    }

    @Override
    public int getItemCount() {
        if (items != null) {
            return items.size();
        }
        return 0;
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        final TextView mNameView;
        final TextView mDateView;
        final TextView mDayView;
        final TextView dayAfterOrBefore;

        ItemViewHolder(View view) {
            super(view);
            mNameView = view.findViewById(R.id.item_name);
            mDateView = view.findViewById(R.id.item_date);
            mDayView = view.findViewById(R.id.item_day);
            dayAfterOrBefore = view.findViewById(R.id.day_after_before);
        }
    }
}