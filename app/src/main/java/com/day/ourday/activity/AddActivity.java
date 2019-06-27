package com.day.ourday.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.view.TimePickerView;
import com.commit451.nativestackblur.NativeStackBlur;
import com.day.ourday.BitmapUtil;
import com.day.ourday.R;
import com.day.ourday.data.AppDatabase;
import com.day.ourday.data.entity.Item;

import java.text.SimpleDateFormat;

public class AddActivity extends AppCompatActivity {

    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        db = AppDatabase.getInstance(this);
        setContentView(R.layout.window_item_message);
        View add_item_layout = findViewById(R.id.window_item);
        EditText editText = findViewById(R.id.edit_view);
        TextView check = findViewById(R.id.check);
        TextView cancel = findViewById(R.id.cancel);
        TextView dateView = findViewById(R.id.date);
        ItemListActivity.displayFullBackground(this);

        View activityView = this.getWindow().getDecorView();
        activityView.setDrawingCacheEnabled(true);
        activityView.destroyDrawingCache();
        activityView.buildDrawingCache();
        Bitmap bmp = activityView.getDrawingCache();
        Bitmap bitmap = NativeStackBlur.process(bmp, 50);
//        String imageName = getIntent().getStringExtra("imageName");
//        Bitmap bitmap = BitmapUtil.getBitmapFromFile(imageName, this);
        Drawable drawable = new BitmapDrawable(getResources(), bitmap);
        add_item_layout.setBackground(drawable);
        //时间选择器
        TimePickerView pvTime = new TimePickerBuilder(this, (date, v) -> {
            dateView.setText(new SimpleDateFormat("yyyy.MM.dd").format(date));
//            Toast.makeText(this, date.toString(), Toast.LENGTH_SHORT).show();
        })
                .setCancelText("取消")
                .setSubmitText("确定")
                .build();
        dateView.setOnClickListener(view -> {
            pvTime.show();
        });

        check.setOnClickListener(view -> {
            Item item = new Item();
            item.setName(editText.getText().toString());
            item.setDate(dateView.getText().toString());
            item.setDays(3);
            addItem(item);

        });

        cancel.setOnClickListener(view -> finish());

    }
    private void addItem(Item item) {
        new AsyncTask<Item, Void, Void>() {
            @Override
            protected Void doInBackground(Item... params) {
                Item item = params[0];
                db.itemDao().insert(item);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                setResult(RESULT_OK);
                finish();
            }
        }.execute(item);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    v.setFocusable(false);
                    v.setFocusableInTouchMode(true);
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }

}
