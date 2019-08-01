package com.day.ourday.task;

import android.os.AsyncTask;
import android.util.Log;

import com.day.ourday.OurDayApplication;
import com.day.ourday.mvp.data.AppDatabase;
import com.day.ourday.mvp.data.entity.Item;

import java.util.List;

/**
 * Created by LimerenceT on 19-8-1
 */
public class QueryItemsTask extends AsyncTask<Void, Void, List<Item>> {
    private IOListener<Item> ioListener;

    public QueryItemsTask(IOListener<Item> listener) {
        ioListener = listener;
    }

    @Override
    protected List<Item> doInBackground(Void... voids) {
        Log.d("QueryTask", Thread.currentThread().getName());
        AppDatabase db = AppDatabase.getInstance(OurDayApplication.getInstance());
        return db.itemDao().getAllItems();
    }

    @Override
    protected void onPostExecute(List<Item> data) {
        if (ioListener != null) {
            ioListener.onTaskResult(data);
        }
    }

    @Override
    protected void onCancelled() {
        ioListener = null;
    }
}
