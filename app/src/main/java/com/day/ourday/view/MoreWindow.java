package com.day.ourday.view;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.view.TimePickerView;
import com.commit451.nativestackblur.NativeStackBlur;
import com.day.ourday.R;
import com.day.ourday.adapter.ItemListAdapter;
import com.day.ourday.mvp.ItemContact;
import com.day.ourday.data.entity.Item;
import com.day.ourday.mvp.presenter.ItemPresenter;
import com.day.ourday.util.DateUtils;

import java.util.Date;
import java.util.List;


/**
 * Create by LimerenceT on 2019-06-27
 */
public class MoreWindow extends PopupWindow implements View.OnClickListener, ItemContact.UpdateListener {
    private Activity mContext;
    private LinearLayout layout;
    private EditText editText;
    private TextView dateView;
    private TextView cancel;
    private TextView confirm;
    private View bgView;
    private View view;
    private TimePickerView timePicker;
    private ItemListAdapter recyclerViewAdapter;
    private ItemPresenter itemPresenter;


    public MoreWindow(Activity context, ItemListAdapter recyclerViewAdapter) {
        mContext = context;
        this.recyclerViewAdapter = recyclerViewAdapter;
        itemPresenter = new ItemPresenter();
    }

    /**
     * 初始化
     *
     * @param view 要显示的模糊背景View,一般选择跟布局layout
     */
    public void init(View view) {
        this.view = view;
        initWindow();
        initView();
        setListener();
    }

    private void initView() {
        cancel = layout.findViewById(R.id.cancel);
        dateView = layout.findViewById(R.id.date);
        confirm = layout.findViewById(R.id.confirm);
        editText = layout.findViewById(R.id.edit_view);
        bgView = layout.findViewById(R.id.add_item_window);
        //时间选择器
        timePicker = new TimePickerBuilder(mContext, (date, v) ->
                dateView.setText(DateUtils.format(date)))
                .setCancelText(mContext.getString(R.string.add_item_window_cancel_text))
                .setOutSideCancelable(true)
                .setSubmitText(mContext.getString(R.string.add_item_window_confirm_text))
                .isDialog(true)
                .build();
        timePickerSetting();
    }

    /**
     * 每次都截图模糊
     */
    private void screenShot() {
        view.setDrawingCacheEnabled(true);
        view.destroyDrawingCache();
        view.buildDrawingCache();
        Bitmap bmp = view.getDrawingCache();
        Bitmap result = NativeStackBlur.process(bmp, 50);
        Drawable drawable = new BitmapDrawable(mContext.getResources(), result);
        layout.setBackground(drawable);
    }

    private void setListener() {
        confirm.setOnClickListener(this);
        cancel.setOnClickListener(this);
        dateView.setOnClickListener(this);
        bgView.setOnTouchListener((v, event) -> {
            v.setFocusable(true);
            v.setFocusableInTouchMode(true);
            v.requestFocus();
            InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            v.performClick();
            return false;
        });
    }

    private void initWindow() {
        Rect frame = new Rect();
        mContext.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);

        DisplayMetrics metrics = new DisplayMetrics();
        mContext.getWindowManager().getDefaultDisplay()
                .getMetrics(metrics);
        int mWidth = metrics.widthPixels;
        int mHeight = metrics.heightPixels;
        setWidth(mWidth);
        setHeight(mHeight);

        setOutsideTouchable(true);
        setFocusable(true);
        //使popupwindow全屏显示
        setClippingEnabled(false);

        layout = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.window_item_message, null);
        setContentView(layout);
    }

    private void timePickerSetting() {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                Gravity.BOTTOM);
        params.leftMargin = 0;
        params.rightMargin = 0;
        timePicker.getDialogContainerLayout().setLayoutParams(params);
        Window dialogWindow = timePicker.getDialog().getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        dialogWindow.setWindowAnimations(com.bigkoo.pickerview.R.style.picker_view_slide_anim);
    }

    /**
     * 显示window动画
     *
     * @param anchor
     */
    public void showMoreWindow(View anchor) {
        initData();
        screenShot();
        setAnimationStyle(R.style.popwin_anim_style);
        showAtLocation(anchor, Gravity.TOP | Gravity.START, 0, 0);
    }

    private void initData() {
        editText.setText(R.string.add_item_window_name_default_text);
        dateView.setText(DateUtils.format(new Date()));
    }

    /**
     * 关闭window动画
     */
    private void closeAnimation() {
        dismiss();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.confirm:
                Item item = new Item();
                item.setName(editText.getText().toString());
                item.setDate(dateView.getText().toString());
                item.setDays(3);
                itemPresenter.addItem(item, this);
                if (isShowing()) {
                    closeAnimation();
                }
                break;
            case R.id.cancel:
                if (isShowing()) {
                    closeAnimation();
                }
                break;
            case R.id.date:
                dateView.setOnClickListener(v -> {
                    timePicker.show();
                    view.setFocusable(true);
                    v.setFocusableInTouchMode(true);
                    v.requestFocus();
                });
        }
    }

    @Override
    public void notifyUpdate(List<Item> items) {
        recyclerViewAdapter.addData(items.get(0));
    }
}
