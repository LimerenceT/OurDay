package com.day.ourday.mvp.presenter;

import com.day.ourday.data.entity.Item;
import com.day.ourday.mvp.ItemContact;
import com.day.ourday.mvp.model.ItemModel;

/**
 * Created by LimerenceT on 19-8-6
 */
public class ItemPresenter implements ItemContact.IPresenter{

    private ItemContact.IModel iModel;
    private ItemContact.IView iView;

    public ItemPresenter() {
        this.iModel = new ItemModel();
    }

    @Override
    public void addItem(Item item, ItemContact.UpdateListener listener) {
        iModel.addItem(item, listener);
    }

    @Override
    public void getAllItems(ItemContact.UpdateListener listener) {
        iModel.getAllItems(listener);
    }

}
