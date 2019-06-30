package com.day.ourday.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.day.ourday.R;
import com.day.ourday.activity.ItemDetailActivity;
import com.day.ourday.activity.ItemListActivity;
import com.day.ourday.data.entity.Item;
import com.day.ourday.fragment.ItemDetailFragment;

import java.util.List;

public class SimpleItemRecyclerViewAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final ItemListActivity mParentActivity;
    private final List<Item> mValues;
    private final boolean mTwoPane;
    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Item item = (Item) view.getTag();
            if (mTwoPane) {
                Bundle arguments = new Bundle();
                arguments.putSerializable(ItemDetailFragment.ARG_ITEM_ID, item);
                ItemDetailFragment fragment = new ItemDetailFragment();
                fragment.setArguments(arguments);
                mParentActivity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.item_detail_container, fragment)
                        .commit();
            } else {
                Context context = view.getContext();
                Intent intent = new Intent(context, ItemDetailActivity.class);
                intent.putExtra(ItemDetailFragment.ARG_ITEM_ID, item);

                context.startActivity(intent);
            }
        }
    };

    public SimpleItemRecyclerViewAdapter(ItemListActivity parent,
                                         List<Item> items,
                                         boolean twoPane) {
        mValues = items;
        mParentActivity = parent;
        mTwoPane = twoPane;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            return new HeaderViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.header, parent, false));
        } else {
            return new ItemViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_list_content, parent, false));
        }

    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            ((ItemViewHolder) holder).mNameView.setText(mValues.get(position).getName());
            ((ItemViewHolder) holder).mDateView.setText(mValues.get(position).getDate());
            ((ItemViewHolder) holder).mDayView.setText(String.valueOf(mValues.get(position).getDays()));

            holder.itemView.setTag(mValues.get(position));
            holder.itemView.setOnClickListener(mOnClickListener);
        } else if (holder instanceof HeaderViewHolder){
            ((HeaderViewHolder) holder).textView.setText("哈哈哈哈哈哈哈哈哈哈");
//            ((HeaderViewHolder) holder).textView.setBackgroundColor(Color.TRANSPARENT);
        }

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        public final TextView mNameView;
        final TextView mDateView;
        final TextView mDayView;

        ItemViewHolder(View view) {
            super(view);
            mNameView = view.findViewById(R.id.item_name);
            mDateView = view.findViewById(R.id.item_date);
            mDayView = view.findViewById(R.id.item_day);
        }
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder {
        final TextView textView;


        HeaderViewHolder(View view) {
            super(view);
            textView = view.findViewById(R.id.header);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;
        } else return 1;
    }
}