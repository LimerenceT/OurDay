package com.day.ourday.mvp.model;

import com.day.ourday.data.entity.Item;
import com.day.ourday.mvp.ItemContact;
import com.day.ourday.task.AddItemTask;
import com.day.ourday.task.IOListener;
import com.day.ourday.task.QueryItemsTask;

import java.util.Collections;
import java.util.List;


/**
 * Created by LimerenceT on 19-8-6
 */
public class ItemModel implements ItemContact.IModel {

    @Override
    public void addItem(Item item, ItemContact.UpdateListener listener) {

        new AddItemTask(new IOListener<Item>() {

            @Override
            public void onTaskResult(List<Item> data) {

            }

            @Override
            public void onTaskSuccess() {
                listener.notifyUpdate(Collections.singletonList(item));
            }
        }).execute(item);
    }

    @Override
    public void getAllItems(ItemContact.UpdateListener listener
    ) {
        new QueryItemsTask(new IOListener<Item>() {
            @Override
            public void onTaskResult(List<Item> data) {
                listener.notifyUpdate(data);
            }

            @Override
            public void onTaskSuccess() {

            }
        }).execute();

    }

}
