package com.day.ourday.task;

import android.os.AsyncTask;
import android.util.Log;

import com.day.ourday.OurDayApplication;
import com.day.ourday.data.AppDatabase;
import com.day.ourday.data.entity.Item;

/**
 * Created by LimerenceT on 19-8-1
 */
public class AddItemTask extends AsyncTask<Item, Void, Void> {
    private static final String TAG = "AddItemTask";
    private IOListener<Item> ioListener;

    public AddItemTask(IOListener<Item> ioListener) {
        this.ioListener = ioListener;
    }

    @Override
    protected Void doInBackground(Item... params) {
        AppDatabase db = AppDatabase.getInstance(OurDayApplication.getInstance());
        Item item = params[0];
        Log.d(TAG, "doInBackground: " + item.getDate());
        db.itemDao().insert(item);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if (ioListener != null) {
            ioListener.onTaskSuccess();
        }
    }
}
