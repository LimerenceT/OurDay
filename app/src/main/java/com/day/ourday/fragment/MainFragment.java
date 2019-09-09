package com.day.ourday.fragment;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.commit451.nativestackblur.NativeStackBlur;
import com.day.ourday.R;
import com.day.ourday.ViewModel.ItemViewModel;
import com.day.ourday.adapter.ItemListAdapter;
import com.day.ourday.data.entity.Item;
import com.day.ourday.util.DateUtils;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;


public class MainFragment extends Fragment {
    private static final String TAG = "MainFragment";

    private ImageView imageView;
    private ImageView imageBlurView;
    private TextView addItemTextView;
    private TextView menuTextView;
    private RecyclerView recyclerView;
    private ItemListAdapter recyclerViewAdapter;
    private Bitmap blurBitmap;
    private SeekBar seekBar;
    private ItemViewModel itemViewModel;
    private View view;

    public MainFragment() {
        // Required empty public constructor
    }

    public static MainFragment newInstance() {
        return new MainFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_main, container, false);
        blurImage();
        initView();
        initData();
        setListener();
        return view;
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
            ItemFragment itemFragment = ItemFragment.newInstance();
            Bundle bundle = new Bundle();
            bundle.putParcelable("bitmap", screenShot());
            itemFragment.setArguments(bundle);
            getFragmentManager().beginTransaction()
                    .addToBackStack(null)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
//                    .setCustomAnimations(R.anim.pop_enter_anim, R.anim.pop_exit_anim, R.anim.pop_enter_anim, R.anim.pop_exit_anim)
                    .add(R.id.item_list_layout, itemFragment)
                    .commit();
        });
    }

    /**
     * 每次都截图模糊
     */
    private Bitmap screenShot() {
        view.setDrawingCacheEnabled(true);
        view.destroyDrawingCache();
        view.buildDrawingCache();
        Bitmap bmp = view.getDrawingCache();
        Bitmap bitmap = NativeStackBlur.process(bmp, 35);
        return Bitmap.createScaledBitmap(bitmap, 120, 180, true);
    }

    private void blurImage() {
        // 默认模糊背景图片
        // TODO: 2019-06-28 处理
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.tang);
        blurBitmap = NativeStackBlur.process(bitmap, 50);
    }

    private void initView() {
        imageView = view.findViewById(R.id.imageView);
        imageBlurView = view.findViewById(R.id.imageBlurView);
        recyclerView = view.findViewById(R.id.item_list);
        recyclerView.addItemDecoration(
                new HorizontalDividerItemDecoration.Builder(getActivity())
                        .drawable(R.color.colorDivider)
                        .sizeResId(R.dimen.divider)
                        .marginResId(R.dimen.left_margin, R.dimen.right_margin)
                        .build());
        addItemTextView = view.findViewById(R.id.addItem);
        menuTextView = view.findViewById(R.id.menu);
        seekBar = view.findViewById(R.id.seekBar);
        seekBar.setMax(160);
        displayFullBackground(getActivity());
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


    private void updateHeader(Item item) {
        TextView headAfterOrBefore = view.findViewById(R.id.header_after_or_before);
        TextView headerText = view.findViewById(R.id.header_text);
        TextView headerDayName = view.findViewById(R.id.header_day_name);
        TextView date = view.findViewById(R.id.header_date);
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
