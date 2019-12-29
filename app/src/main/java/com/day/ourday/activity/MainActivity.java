package com.day.ourday.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.commit451.nativestackblur.NativeStackBlur;
import com.day.ourday.BR;
import com.day.ourday.R;
import com.day.ourday.data.entity.Event;
import com.day.ourday.databinding.ActivityMainBinding;
import com.day.ourday.fragment.MainFragment;
import com.day.ourday.util.PicturePathUtilsKt;
import com.day.ourday.viewmodel.PictureViewModel;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;
import static com.day.ourday.util.PicturePathUtilsKt.getFullPath;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final int STORAGE_PERMISSION = 0x20;
    private SharedPreferences sharedPreferences;
    private String fileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
//        requestStoragePermission(this);
        displayFullBackground(this);
        PictureViewModel pictureViewModel = ViewModelProviders.of(this).get(PictureViewModel.class);
        dataBinding.setVariable(BR.viewModel, pictureViewModel);
        dataBinding.setLifecycleOwner(this);
        sharedPreferences = getSharedPreferences("bg", Context.MODE_PRIVATE);
        String bgp = sharedPreferences.getString("bgp", "");
        Glide.with(this).asDrawable().load(bgp.isEmpty() ? BitmapFactory.decodeResource(getResources(), R.drawable.tang) :
                BitmapFactory.decodeFile(PicturePathUtilsKt.getFullPath(bgp))).into(dataBinding.imageView);
        int blurProgress = sharedPreferences.getInt("blurProgress", 0);
        dataBinding.imageView.setImageAlpha(255 - blurProgress);
        dataBinding.imageView.getForeground().setAlpha(blurProgress);

        pictureViewModel.getBgChangeEvent().observe(this, event -> {
            fileName = event.getType() == Event.START ? bgp : pictureViewModel.getMainBgPictureName().getValue();

            Bitmap bitmap = fileName.isEmpty() ? BitmapFactory.decodeResource(getResources(), R.drawable.tang) :
                    BitmapFactory.decodeFile(PicturePathUtilsKt.getFullPath(fileName));
            Glide.with(this).asDrawable().load(NativeStackBlur.process(bitmap, 70)).into(dataBinding.imageBlurView);
        });

        MainFragment mainFragment = MainFragment.newInstance();
        getSupportFragmentManager().beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .add(R.id.fragment_container, mainFragment, "MainFragment")
                .commit();
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
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    }
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }
}
