package com.day.ourday.data.dao;



import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.day.ourday.data.entity.Item;

import java.util.List;


/**
 * Create by LimerenceT on 2019-06-24
 */
@Dao
public interface ItemDao {
    @Query("select * from item")
    List<Item> getAllItems();

    @Query("select * from item where id=:id")
    Item getItem(int id);

    @Insert
    void insert(Item item);

    @Update
    void update(Item item);

    @Delete
    void delete(Item item);

}
