package com.day.ourday.mvp;

import com.day.ourday.data.entity.Item;

import java.util.List;

/**
 * Created by LimerenceT on 19-8-6
 */
public interface ItemContact {
    interface IModel {
        void addItem(Item item, UpdateListener listener);

        void getAllItems(UpdateListener listener);

    }

    interface IView {

    }

    interface IPresenter {

        void addItem(Item item, UpdateListener listener);

        void getAllItems(UpdateListener listener);
    }


    interface UpdateListener {
        void notifyUpdate(List<Item> items);
    }
}
