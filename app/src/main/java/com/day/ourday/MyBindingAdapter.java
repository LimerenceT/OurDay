package com.day.ourday;

import android.graphics.Color;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

/**
 * Created by long.qiu on 19-10-26
 */
public class MyBindingAdapter {

    @BindingAdapter(value = {"app:days"})
    public static void setColorAndText(TextView textView, int days) {
        if (days>0) {
            textView.setText("天后");
            textView.setBackgroundColor(Color.BLUE);
        } else if (days<0) {
            textView.setText("天前");
            textView.setBackgroundColor(Color.RED);
        } else {
            textView.setText("今天");
            textView.setBackgroundColor(Color.GRAY);
        }
    }
}
