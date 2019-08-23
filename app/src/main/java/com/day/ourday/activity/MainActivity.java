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
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.commit451.nativestackblur.NativeStackBlur;
import com.day.ourday.R;
import com.day.ourday.ViewModel.ItemViewModel;
import com.day.ourday.adapter.ItemListAdapter;
import com.day.ourday.data.entity.Item;
import com.day.ourday.util.DateUtils;
import com.day.ourday.view.MoreWindow;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.io.IOException;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final int STORAGE_PERMISSION = 0x20;
    private static final int REQUEST_CODE_GALLERY = 0x10;

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private ImageView imageView;
    private ImageView imageBlurView;
    private TextView addItemTextView;
    private TextView menuTextView;
    private RecyclerView recyclerView;
    private MoreWindow mMoreWindow;
    private ItemListAdapter recyclerViewAdapter;
    private Bitmap blurBitmap;
    private SeekBar seekBar;
    private ItemViewModel itemViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestStoragePermission(this);
        blurImage();
        initView();
        initData();
        setListener();
    }

    private void initData() {
        recyclerViewAdapter = new ItemListAdapter();
        recyclerView.setAdapter(recyclerViewAdapter);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        itemViewModel = ViewModelProviders.of(this).get(ItemViewModel.class);
        itemViewModel.getItems().observe(this, items -> {
            recyclerViewAdapter.updateItems(items);
            updateHeader(items.get(0));
        });

    }
    private ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            Item item = (Item) viewHolder.itemView.getTag();
            itemViewModel.delete(item);
            Toast.makeText(MainActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
        }
    });

    private void setListener() {

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            boolean blur = false;

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                imageBlurView.setVisibility(View.VISIBLE);
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


        addItemTextView.setOnClickListener(view -> {
            View id = findViewById(R.id.item_list_layout);
            if (mMoreWindow == null) {
                mMoreWindow = new MoreWindow(this);
                mMoreWindow.init(id);
            }
            mMoreWindow.showMoreWindow(id);
        });
    }

    private void blurImage() {
        // 默认模糊背景图片
        // TODO: 2019-06-28 处理
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.tang);
        blurBitmap = NativeStackBlur.process(bitmap, 50);
    }

    private void initView() {
        imageView = findViewById(R.id.imageView);
        imageBlurView = findViewById(R.id.imageBlurView);
        recyclerView = findViewById(R.id.item_list);
        recyclerView.addItemDecoration(
                new HorizontalDividerItemDecoration.Builder(this)
                        .drawable(R.color.colorDivider)
                        .sizeResId(R.dimen.divider)
                        .marginResId(R.dimen.left_margin, R.dimen.right_margin)
                        .build());
        addItemTextView = findViewById(R.id.addItem);
        menuTextView = findViewById(R.id.menu);
        seekBar = findViewById(R.id.seekBar);
        seekBar.setMax(160);
        displayFullBackground(this);
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

    public void requestStoragePermission(Activity activity) {
        int hasCameraPermission = ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);
        int writePermission = ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        Log.d(TAG, "开始" + hasCameraPermission);
        if (hasCameraPermission + writePermission == PackageManager.PERMISSION_GRANTED) {
            // 拥有权限，可以执行涉及到存储权限的操作
            Log.d(TAG, "你已经授权了该组权限");
        } else {
            // 没有权限，向用户申请该权限
            Log.d(TAG, "向用户申请该组权限");
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
    private void updateHeader(Item item) {
        TextView headAfterOrBefore = findViewById(R.id.header_after_or_before);
        TextView headerText = findViewById(R.id.header_text);
        TextView headerDayName = findViewById(R.id.header_day_name);
        TextView date = findViewById(R.id.header_date);
        headerDayName.setText(item.getName());
        date.setText(item.getDate());

        int days = DateUtils.getDays(item.getDate());
        if (days>0) {
            headAfterOrBefore.setText("天后");
        } else if (days< 0){
            headAfterOrBefore.setText("天前");
        } else {
            headAfterOrBefore.setText("今天");
        }
        headerText.setText(String.valueOf(Math.abs(days)));
    }

    @Override
    protected void onDestroy() {
        mMoreWindow = null;
        super.onDestroy();
    }



}
