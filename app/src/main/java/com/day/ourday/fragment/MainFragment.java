package com.day.ourday.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.commit451.nativestackblur.NativeStackBlur;
import com.day.ourday.BR;
import com.day.ourday.R;
import com.day.ourday.adapter.ItemListAdapter;
import com.day.ourday.data.entity.Item;
import com.day.ourday.databinding.FragmentMainBinding;
import com.day.ourday.view.SpringNestedScrollView;
import com.day.ourday.viewmodel.ItemViewModel;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;


public class MainFragment extends Fragment {
    private static final String TAG = "MainFragment";
    private RecyclerView recyclerView;
    private ItemListAdapter recyclerViewAdapter;
    private Bitmap blurBitmap;
    private ImageView imageView;
    private ItemViewModel itemViewModel;
    private View view;
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
        dataBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_main, container, false);
        view = dataBinding.getRoot();
        blurImage();
        initView();
        initData();
        setListener();
        return view;
    }

    private void initData() {
        itemViewModel = ViewModelProviders.of(this).get(ItemViewModel.class);
        recyclerViewAdapter = new ItemListAdapter();
        recyclerViewAdapter.setViewModel(itemViewModel);
        recyclerView.setAdapter(recyclerViewAdapter);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        dataBinding.setVariable(BR.viewModel, itemViewModel);
        dataBinding.setLifecycleOwner(this);
        itemViewModel.getItems().observe(this, items -> {
            recyclerViewAdapter.updateItems(items);
        });
    }


    private void setListener() {

        dataBinding.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            boolean blur = false;

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                dataBinding.imageBlurView.setVisibility(View.VISIBLE);
                dataBinding.imageBlurView.getBackground().setAlpha(i);
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

        dataBinding.addItem.setOnClickListener(view -> {
                    ItemFragment itemFragment = (ItemFragment) getFragmentManager().findFragmentByTag("ItemFragment");
                    if (itemFragment == null) {
                        itemFragment = ItemFragment.newInstance();
                    }
                    getFragmentManager().beginTransaction()
                            .addToBackStack(null)
                            .hide(this)
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .add(R.id.fragment_container, itemFragment, "ItemFragment")
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

    private void blurImage() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.tang);
        blurBitmap = NativeStackBlur.process(bitmap, 50);
    }

    private void initView() {
        // todo: root and include child can't just use
        view.findViewById(R.id.app_bar).getBackground().setAlpha(0);
        imageView = view.findViewById(R.id.imageView);
        recyclerView = view.findViewById(R.id.item_list);
        recyclerView.addItemDecoration(
                new HorizontalDividerItemDecoration.Builder(getActivity())
                        .drawable(R.color.colorDivider)
                        .sizeResId(R.dimen.divider)
                        .marginResId(R.dimen.left_margin, R.dimen.right_margin)
                        .build());

        SpringNestedScrollView springNestedScrollView = view.findViewById(R.id.springNestedScrollview);
        springNestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY > 200) {
                    view.findViewById(R.id.app_bar).getBackground().setAlpha(Math.min(255, scrollY));
                } else {
                    view.findViewById(R.id.app_bar).getBackground().setAlpha(Math.max(scrollY - 50, 0));
                }
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
