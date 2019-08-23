package com.day.ourday.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.day.ourday.data.AppDatabase;
import com.day.ourday.data.dao.ItemDao;
import com.day.ourday.data.entity.Item;
import com.day.ourday.task.AddItemTask;
import com.day.ourday.task.DeleteAsyncTask;

import java.util.List;

/**
 * Created by long.qiu on 19-8-22
 */
public class ItemRepository {
    private LiveData<List<Item>> items;
    private ItemDao itemDao;


    public ItemRepository(Application application) {
        itemDao = AppDatabase.getInstance(application).itemDao();
        items = itemDao.getAllItems();
    }

    public void insert(Item item) {
        new AddItemTask(itemDao).execute(item);
    }

    public void update(Item item) {

    }

    public void delete(Item item) {
        new DeleteAsyncTask(itemDao).execute(item);
    }

    public void setItems(LiveData<List<Item>> items) {
        this.items = items;
    }

    public LiveData<List<Item>> getItems() {
        return items;
    }

}
