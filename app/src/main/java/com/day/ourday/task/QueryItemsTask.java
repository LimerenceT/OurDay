package com.day.ourday.task;

import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.day.ourday.data.dao.ItemDao;
import com.day.ourday.data.entity.Item;

import java.util.List;

/**
 * Created by LimerenceT on 19-8-1
 */
public class QueryItemsTask extends AsyncTask<Void, Void, LiveData<List<Item>>> {
    private static final String TAG = "QueryItemsTask";
    private ItemDao itemDao;

    public QueryItemsTask(ItemDao itemDao) {
        this.itemDao = itemDao;
    }

    @Override
    protected LiveData<List<Item>> doInBackground(Void... voids) {
        return itemDao.getAllItems();
    }

}
