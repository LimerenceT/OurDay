package com.day.ourday.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
    private TextView settingTextView;
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
            if (!items.isEmpty()) {
                updateHeader(items.get(0));
            } else {
                clearHeader();
            }
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
                    ItemFragment itemFragment = (ItemFragment) getFragmentManager().findFragmentByTag("ItemFragment");
                    if (itemFragment == null) {
                        itemFragment = ItemFragment.newInstance();
                    }
                    getFragmentManager().beginTransaction()
                            .addToBackStack(null)
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .add(R.id.fragment_container, itemFragment, "ItemFragment")
                            .commit();
                }
        );

        settingTextView.setOnClickListener(view -> {
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

    private void blurImage() {
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
        settingTextView = view.findViewById(R.id.setting);
        seekBar = view.findViewById(R.id.seekBar);
        seekBar.setMax(160);
    }

    private void updateHeader(Item item) {
        TextView headAfterOrBefore = view.findViewById(R.id.header_after_or_before);
        TextView headerText = view.findViewById(R.id.header_text);
        TextView headerDayName = view.findViewById(R.id.header_day_name);
        TextView date = view.findViewById(R.id.header_date);
        headerDayName.setText(item.getName());
        date.setText(item.getDate());

        int days = DateUtils.getDays(item.getDate());
        if (days > 0) {
            headAfterOrBefore.setText("天后");
        } else if (days < 0) {
            headAfterOrBefore.setText("天前");
        } else {
            headAfterOrBefore.setText("今天");
        }
        headerText.setText(String.valueOf(Math.abs(days)));
    }

    private void clearHeader() {
        TextView headAfterOrBefore = view.findViewById(R.id.header_after_or_before);
        TextView headerText = view.findViewById(R.id.header_text);
        TextView headerDayName = view.findViewById(R.id.header_day_name);
        TextView date = view.findViewById(R.id.header_date);
        headerDayName.setText("某天");
        date.setText("2019.08.07");
        headAfterOrBefore.setText("天前");
        headerText.setText(String.valueOf(0));
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
