package com.day.ourday.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.view.TimePickerView;
import com.commit451.nativestackblur.NativeStackBlur;
import com.day.ourday.BitmapUtil;
import com.day.ourday.MoreWindow;
import com.day.ourday.R;
import com.day.ourday.adapter.SimpleItemRecyclerViewAdapter;
import com.day.ourday.data.AppDatabase;
import com.day.ourday.data.entity.Item;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ItemDetailActivity} representing
 * Item details. On tablets, the activity presents the list of items and
 * Item details side-by-side using two vertical panes.
 */
public class ItemListActivity extends AppCompatActivity {

    private static final int STORAGE_PERMISSION = 0x20;
    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private static final int REQUEST_CODE_GALLERY = 0x10;
    private ImageView imageView;
    private ImageView imageBlurView;
    private TextView addItemTextView;
    private TextView menuTextView;
    private TextView textViewProgress;
    private RecyclerView recyclerView;
    private HandlerThread queryThread;
    private Handler backgroundHandler;
    private MoreWindow mMoreWindow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);
        requestStoragePermission(this);
        imageView = findViewById(R.id.imageView);
        imageBlurView = findViewById(R.id.imageBlurView);
        recyclerView = findViewById(R.id.item_list);
        textViewProgress = findViewById(R.id.textViewProgress);
        addItemTextView = findViewById(R.id.addItem);
        menuTextView = findViewById(R.id.menu);
        if (findViewById(R.id.item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }
        FloatingActionButton fab = findViewById(R.id.fab);
        SeekBar seekBar = findViewById(R.id.seekBar);

        fab.setOnClickListener(view -> pickPictureFromGallery());


        // 默认模糊背景图片
        // TODO: 2019-06-28 处理
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.tang);
        final Bitmap blurBitmap = NativeStackBlur.process(bitmap, 50);
        seekBar.setMax(160);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            boolean blur = false;

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                imageBlurView.setVisibility(View.VISIBLE);
                textViewProgress.setText(String.valueOf(i));
                imageBlurView.getBackground().setAlpha(i);
                if (i > 150 && !blur) {
                    imageView.setImageBitmap(blurBitmap);
                    blur = true;
                }
                if (i < 150 && blur) {
                    imageView.setImageResource(R.drawable.tang);
                    blur = false;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        startBackgroundThread();
        backgroundHandler.post(() -> {
            assert recyclerView != null;
            List<Item> items = AppDatabase.getInstance(this).itemDao().getAllItems();
            runOnUiThread(() -> setupRecyclerView(recyclerView, items));
        });
        displayFullBackground(this);



        addItemTextView.setOnClickListener(view -> {
            View id = findViewById(R.id.item_list_layout);
            if (mMoreWindow == null) {
                mMoreWindow = new MoreWindow(this);
                mMoreWindow.init(id);
            }
            mMoreWindow.showMoreWindow(id);
        });
    }

    /**
     * 设置背景图全屏显示
     */
    public static void displayFullBackground(Activity activity) {
        View decorView = activity.getWindow().getDecorView();
        int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        decorView.setSystemUiVisibility(option);
        activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
    }


    // TODO: 2019-06-22 存储选择的图片，以后提供一个界面再次选择
    // FIXME: 2019-06-22 图片方向问题
    private void pickPictureFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // 以startActivityForResult的方式启动一个activity用来获取返回的结果
        startActivityForResult(intent, REQUEST_CODE_GALLERY);

    }

    public static void requestStoragePermission(Activity activity) {
        int hasCameraPermission = ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);
        int writePermission = ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        Log.e("TAG", "开始" + hasCameraPermission);
        if (hasCameraPermission + writePermission == PackageManager.PERMISSION_GRANTED) {
            // 拥有权限，可以执行涉及到存储权限的操作
            Log.e("TAG", "你已经授权了该组权限");
        } else {
            // 没有权限，向用户申请该权限
            Log.e("TAG", "向用户申请该组权限");
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_GALLERY) {
            try {
                displayImage(data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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

    private void displayImage(Uri imageUri) throws IOException {
        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
        imageView.setImageBitmap(bitmap);
    }


    private void setupRecyclerView(@NonNull RecyclerView recyclerView, List<Item> items) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this, items, mTwoPane));
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (queryThread == null) {
            startBackgroundThread();
        }
    }

    @Override
    protected void onPause() {
        stopBackgroundThread();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (mMoreWindow != null) {
            mMoreWindow.dismiss();
            mMoreWindow = null;
        }
        super.onDestroy();
    }

    private void startBackgroundThread() {
        queryThread = new HandlerThread("query");
        queryThread.start();
        backgroundHandler = new Handler(queryThread.getLooper());

    }

    private void stopBackgroundThread() {
        queryThread.quitSafely();
        try {
            queryThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        queryThread = null;
        backgroundHandler = null;
    }
}
