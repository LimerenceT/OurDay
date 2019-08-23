package com.day.ourday.data;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.day.ourday.data.dao.ItemDao;
import com.day.ourday.data.entity.Item;
import com.day.ourday.util.DateUtils;

import java.util.Date;

/**
 * Create by LimerenceT on 2019-06-24
 */
@Database(entities = {Item.class}, version = 3, exportSchema = false)
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
                                .addCallback(callback)
                                .build();
            }
            return INSTANCE;
        }
    }

    private static RoomDatabase.Callback callback = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(INSTANCE).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void,Void,Void> {
        private ItemDao itemDao;

        public PopulateDbAsyncTask(AppDatabase database) {
            this.itemDao = database.itemDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Date date = new Date();
            String today = DateUtils.format(date);
            itemDao.insert(new Item("某天",today));
            itemDao.insert(new Item("某天",today));
            return null;
        }
    }

}
