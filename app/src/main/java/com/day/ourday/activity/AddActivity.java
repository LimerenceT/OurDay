package com.day.ourday.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.day.ourday.R;

public class AddActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.window_item_message);
        ItemListActivity.displayFullBackground(this);
    }
}
