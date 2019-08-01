package com.day.ourday.task;

import android.os.AsyncTask;

import com.day.ourday.OurDayApplication;
import com.day.ourday.mvp.data.AppDatabase;
import com.day.ourday.mvp.data.entity.Item;

/**
 * Created by LimerenceT on 19-8-1
 */
public class AddItemTask extends AsyncTask<Item, Void, Void> {

    @Override
    protected Void doInBackground(Item... params) {
        AppDatabase db = AppDatabase.getInstance(OurDayApplication.getInstance());
        Item item = params[0];
        db.itemDao().insert(item);
        return null;
    }

}
