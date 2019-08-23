package com.day.ourday.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.day.ourday.data.entity.Item;
import com.day.ourday.repository.ItemRepository;

import java.util.List;

/**
 * Created by long.qiu on 19-8-22
 */
public class ItemViewModel extends AndroidViewModel {
    private LiveData<List<Item>> items;
    private ItemRepository itemRepository;

    public ItemViewModel(@NonNull Application application) {
        super(application);
        itemRepository = new ItemRepository(application);
        items = itemRepository.getItems();
    }


    public LiveData<List<Item>> getItems() {
        return items;
    }

    public void setItems(LiveData<List<Item>> items) {
        this.items = items;
    }

    public void insert(Item item) {
        itemRepository.insert(item);
    }
    public void delete(Item item) {
        itemRepository.delete(item);
    }
}
