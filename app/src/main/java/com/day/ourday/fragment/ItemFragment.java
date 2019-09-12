package com.day.ourday.fragment;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.view.TimePickerView;
import com.commit451.nativestackblur.NativeStackBlur;
import com.day.ourday.R;
import com.day.ourday.ViewModel.ItemViewModel;
import com.day.ourday.data.entity.Item;
import com.day.ourday.util.DateUtils;

import java.util.Date;

public class ItemFragment extends Fragment implements View.OnClickListener {

    private ItemViewModel itemViewModel;
    private EditText editText;
    private TextView dateView;
    private TextView cancel;
    private TextView confirm;
    private View bgView;
    private TimePickerView timePicker;
    private View view;

    public static ItemFragment newInstance() {
        return new ItemFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_item, container, false);
        // FIXME restart activity error
        view.setBackground(screenShot());
        init();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        itemViewModel = ViewModelProviders.of(this).get(ItemViewModel.class);
        // TODO: Use the ViewModel
    }

    public void init() {
        initView();
        setListener();
        initData();
    }

    /**
     * 每次都截图模糊
     */
    private Drawable screenShot() {
        View decorView = getActivity().getWindow().getDecorView();
        decorView.setDrawingCacheEnabled(true);
        decorView.destroyDrawingCache();
        decorView.buildDrawingCache();
        Bitmap bmp = decorView.getDrawingCache();
        Bitmap bitmap = NativeStackBlur.process(bmp, 35);
        return new BitmapDrawable(getContext().getResources(), bitmap);
    }

    private void initView() {
        cancel = view.findViewById(R.id.cancel);
        dateView = view.findViewById(R.id.window_date);
        confirm = view.findViewById(R.id.confirm);
        editText = view.findViewById(R.id.edit_view);
        bgView = view.findViewById(R.id.add_item_window);
        //时间选择器
        timePicker = new TimePickerBuilder(getContext(), (date, v) ->
                dateView.setText(DateUtils.format(date)))
                .setCancelText(getContext().getString(R.string.add_item_window_cancel_text))
                .setOutSideCancelable(true)
                .setSubmitText(getContext().getString(R.string.add_item_window_confirm_text))
                .isDialog(true)
                .build();
        timePickerSetting();
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

    private void initData() {
        editText.setText(R.string.add_item_window_name_default_text);
        dateView.setText(DateUtils.format(new Date()));
    }


    private void setListener() {
        confirm.setOnClickListener(this);
        cancel.setOnClickListener(this);
        dateView.setOnClickListener(this);
        bgView.setOnTouchListener((v, event) -> {
            v.setFocusable(true);
            v.setFocusableInTouchMode(true);
            v.requestFocus();
            v.performClick();
            return false;
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.confirm:
                Item item = new Item();
                String date = dateView.getText().toString();
                item.setName("".equals(editText.getText().toString()) ? "某天" : editText.getText().toString());
                item.setDate(date);
                itemViewModel.insert(item);
                getFragmentManager().popBackStack();
                break;
            case R.id.cancel:
                getFragmentManager().popBackStack();
                break;
            case R.id.window_date:
                view.setFocusable(true);
                view.setFocusableInTouchMode(true);
                view.requestFocus();
                timePicker.show();
                break;
        }
    }
}
