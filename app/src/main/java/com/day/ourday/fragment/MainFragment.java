package com.day.ourday.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.day.ourday.R;
import com.day.ourday.adapter.ItemListAdapter;
import com.day.ourday.data.entity.Item;
import com.day.ourday.databinding.FragmentMainBinding;
import com.day.ourday.viewmodel.ItemViewModel;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;


public class MainFragment extends Fragment {
    private static final String TAG = "MainFragment";
    private ItemListAdapter recyclerViewAdapter;
    private ItemViewModel itemViewModel;
    private FragmentMainBinding dataBinding;

    public MainFragment() {
        // Required empty public constructor
    }

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        dataBinding = FragmentMainBinding.inflate(inflater, container, false);
//        blurImage();
        dataBinding.setLifecycleOwner(this);

        initView();
        initData();
        setListener();
        return dataBinding.getRoot();
    }

    private void initData() {
        itemViewModel = ViewModelProviders.of(this).get(ItemViewModel.class);
        recyclerViewAdapter = new ItemListAdapter();
        recyclerViewAdapter.setViewModel(itemViewModel);
        dataBinding.itemList.itemList.setAdapter(recyclerViewAdapter);
        itemTouchHelper.attachToRecyclerView(dataBinding.itemList.itemList);
        dataBinding.setViewModel(itemViewModel);
        dataBinding.setLifecycleOwner(this);
    }


    private void setListener() {

//        dataBinding.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            boolean blur = false;
//
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
//                dataBinding.imageBlurView.setVisibility(View.VISIBLE);
//                dataBinding.imageBlurView.getBackground().setAlpha(i);
//                if (i > 150 && !blur) {
//                    imageView.setImageBitmap(blurBitmap);
//                    blur = true;
//                }
//                if (i < 150 && blur) {
//                    imageView.setImageResource(R.drawable.tang);
//                    blur = false;
//                }
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//
//            }
//        });

        dataBinding.addItem.setOnClickListener(view -> {
                    AddItemFragment addItemFragment = (AddItemFragment) getFragmentManager().findFragmentByTag("ItemFragment");
                    if (addItemFragment == null) {
                        addItemFragment = AddItemFragment.newInstance();
                    }
                    getFragmentManager().beginTransaction()
                            .addToBackStack(null)
                            .hide(this)
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .add(R.id.fragment_container, addItemFragment, "ItemFragment")
                            .commit();
                }
        );

        dataBinding.setting.setOnClickListener(view -> {
                    SettingFragment settingFragment = (SettingFragment) getFragmentManager().findFragmentByTag("SettingFragment");
                    if (settingFragment == null) {
                        settingFragment = SettingFragment.newInstance();
                    }
                    getFragmentManager().beginTransaction()
                            .addToBackStack(null)
                            .hide(this)
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .add(R.id.fragment_container, settingFragment, "SettingFragment")
                            .commit();
                }
        );
    }

//    private void blurImage() {
//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.tang);
//        blurBitmap = NativeStackBlur.process(bitmap, 50);
//    }

    /**
     * 根据百分比改变颜色透明度
     */
    public int changeAlpha(int color, float fraction) {
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        int alpha = (int) (Color.alpha(color) * fraction);
        return Color.argb(alpha, red, green, blue);
    }

    private void initView() {
        dataBinding.appBar.getBackground().setAlpha(0);
        dataBinding.itemList.itemList.addItemDecoration(
                new HorizontalDividerItemDecoration.Builder(getActivity())
                        .drawable(R.color.colorDivider)
                        .sizeResId(R.dimen.divider)
                        .marginResId(R.dimen.left_margin, R.dimen.right_margin)
                        .build());

        dataBinding.itemList.springNestedScrollview.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (scrollY > 200) {
                dataBinding.appBar.getBackground().setAlpha(Math.min(255, scrollY));
            } else {
                dataBinding.appBar.getBackground().setAlpha(Math.max(scrollY - 50, 0));
            }
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
            Toast.makeText(getActivity(), "删除成功", Toast.LENGTH_SHORT).show();
        }
    });

}
