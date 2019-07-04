package com.day.ourday;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Handler;
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
import com.day.ourday.data.AppDatabase;
import com.day.ourday.data.entity.Item;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


/**
 * Create by LimerenceT on 2019-06-27
 */
public class MoreWindow extends PopupWindow implements View.OnClickListener {
    private Activity mContext;
    private LinearLayout layout;
    private EditText editText;
    private TextView dateView;
    private TextView close;
    private View bgView;
    private int mWidth;
    private int mHeight;
    private int statusBarHeight;
    private Handler mHandler = new Handler();
    private AppDatabase db;

    public MoreWindow(Activity context) {
        mContext = context;
    }

    /**
     * 初始化
     *
     * @param view 要显示的模糊背景View,一般选择跟布局layout
     */
    public void init(View view) {
        db = AppDatabase.getInstance(mContext);
        Rect frame = new Rect();
        mContext.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        statusBarHeight = frame.top;
        DisplayMetrics metrics = new DisplayMetrics();
        mContext.getWindowManager().getDefaultDisplay()
                .getMetrics(metrics);
        mWidth = metrics.widthPixels;
        mHeight = metrics.heightPixels;
        setWidth(mWidth);
        setHeight(mHeight);

        layout = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.window_item_message, null);

        setContentView(layout);

        close = layout.findViewById(R.id.cancel);
        dateView = layout.findViewById(R.id.date);
        TextView check = layout.findViewById(R.id.check);
        editText = layout.findViewById(R.id.edit_view);
        close.setOnClickListener(v -> {
            if (isShowing()) {
                closeAnimation();
            }
        });

        // 截图
        view.setDrawingCacheEnabled(true);
        view.destroyDrawingCache();
        view.buildDrawingCache();
        Bitmap bmp = view.getDrawingCache();
        Bitmap result = NativeStackBlur.process(bmp, 50);
        Drawable drawable = new BitmapDrawable(mContext.getResources(), result);
        layout.setBackground(drawable);
        bgView = layout.findViewById(R.id.window_item);
        bgView.setOnTouchListener((v, event) -> {
            v.setFocusable(true);
            v.setFocusableInTouchMode(true);
            v.requestFocus();
            InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

            v.performClick();
            return false;
        });

        setOutsideTouchable(true);
        setFocusable(true);
        setClippingEnabled(false);//使popupwindow全屏显示

        //时间选择器
        TimePickerView pvTime = new TimePickerBuilder(mContext, (date, v) -> dateView.setText(new SimpleDateFormat("yyyy.MM.dd", Locale.CHINA).format(date)))
                .setCancelText("取消").setOutSideCancelable(true)
                .setSubmitText("确定").isDialog(true)
                .build();
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                Gravity.BOTTOM);

        params.leftMargin = 0;
        params.rightMargin = 0;
        pvTime.getDialogContainerLayout().setLayoutParams(params);
        Window dialogWindow = pvTime.getDialog().getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        dialogWindow.setWindowAnimations(com.bigkoo.pickerview.R.style.picker_view_slide_anim);
        dateView.setOnClickListener(v -> {
            pvTime.show();
            v.setFocusable(true);
            v.setFocusableInTouchMode(true);
            v.requestFocus();
        });

        check.setOnClickListener(view1 -> {
            Item item = new Item();
            item.setName(editText.getText().toString());
            item.setDate(dateView.getText().toString());
            item.setDays(3);
            addItem(item);

        });

    }

    // TODO: 2019-06-28 优化
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
                if (isShowing()) {
                    closeAnimation();
                }
            }
        }.execute(item);
    }

//    public int getNavigationBarHeight(Activity activity) {
//        Resources resources = activity.getResources();
//        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
//        //获取NavigationBar的高度
//        int height = resources.getDimensionPixelSize(resourceId);
//        return height;
//    }


    /**
     * 显示window动画
     *
     * @param anchor
     */
    public void showMoreWindow(View anchor) {
        initData();
        setAnimationStyle(R.style.popwin_anim_style);
        showAtLocation(anchor, Gravity.TOP | Gravity.START, 0, 0);
//        showAnimation(layout);
    }

    private void initData() {
        editText.setText("某天");
        dateView.setText(new SimpleDateFormat("yyyy.MM.dd", Locale.CHINA).format(new Date()));

    }

    // 容器中的控件动画
    private void showAnimation(ViewGroup layout) {
        try {
            LinearLayout linearLayout = layout.findViewById(R.id.line);
            mHandler.post(() -> {
                //＋ 旋转动画
                close.animate().rotation(90).setDuration(400);
            });
            //菜单项弹出动画
            for (int i = 0; i < linearLayout.getChildCount(); i++) {
                final View child = linearLayout.getChildAt(i);

                child.setOnClickListener(this);
                child.setVisibility(View.INVISIBLE);
                mHandler.postDelayed(() -> {
                    child.setVisibility(View.VISIBLE);
                    ValueAnimator fadeAnim = ObjectAnimator.ofFloat(child, "translationY", 600, 0);
                    fadeAnim.setDuration(300);
                    KickBackAnimator kickAnimator = new KickBackAnimator();
                    kickAnimator.setDuration(150);
                    fadeAnim.setEvaluator(kickAnimator);
                    fadeAnim.start();
                }, i * 500 + 100);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 关闭window动画
     */
    // FIXME: 2019-06-29 能够连续点两次
    private void closeAnimation() {
        dismiss();
    }

    @Override
    public void onClick(View view) {

    }
}
