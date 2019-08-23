package com.day.ourday.task;

import android.os.AsyncTask;

import com.day.ourday.data.dao.ItemDao;
import com.day.ourday.data.entity.Item;

/**
 * Created by long.qiu on 19-8-23
 */
public class DeleteAsyncTask extends AsyncTask<Item, Void, Void> {
    private ItemDao itemDao;

    public DeleteAsyncTask(ItemDao itemDao) {
        this.itemDao = itemDao;
    }

    @Override
    protected Void doInBackground(Item... items) {
        itemDao.delete(items[0]);
        return null;
    }
}
