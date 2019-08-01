package com.day.ourday.mvp.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.day.ourday.mvp.data.dao.ItemDao;
import com.day.ourday.mvp.data.entity.Item;

/**
 * Create by LimerenceT on 2019-06-24
 */
@Database(entities = {Item.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;
    private static final Object sLock = new Object();
    public abstract ItemDao itemDao();

    public static AppDatabase getInstance(Context context) {
        synchronized (sLock) {
            if (INSTANCE == null) {
                INSTANCE =
                        Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "app.db")
                                .fallbackToDestructiveMigration()
                                .build();
            }
            return INSTANCE;
        }
    }

}
