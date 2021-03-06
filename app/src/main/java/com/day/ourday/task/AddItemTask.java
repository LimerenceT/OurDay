package com.day.ourday.task;

import android.os.AsyncTask;

import com.day.ourday.data.dao.ItemDao;
import com.day.ourday.data.entity.Item;

/**
 * Created by LimerenceT on 19-8-1
 */
public class AddItemTask extends AsyncTask<Item, Void, Void> {
    private ItemDao itemDao;

    public AddItemTask(ItemDao itemDao) {
        this.itemDao = itemDao;
    }

    @Override
    protected Void doInBackground(Item... params) {
        Item item = params[0];
        itemDao.insert(item);
        return null;
    }

}
