package com.day.ourday.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.day.ourday.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingFragment extends Fragment {

    public SettingFragment() {
        // Required empty public constructor
    }

    public static SettingFragment newInstance() {
        return new SettingFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        TextView cancel = view.findViewById(R.id.cancel);
        TextView chooseBg = view.findViewById(R.id.choose_bg);

        cancel.setOnClickListener(v -> getFragmentManager().popBackStack());
        chooseBg.setOnClickListener(v ->
                getFragmentManager().beginTransaction()
                        .addToBackStack(null).hide(this)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .add(R.id.fragment_container, PictureFragment.newInstance())
                        .commit());
        return view;
    }

    @Override
    public void onDestroyView() {

        super.onDestroyView();
    }
}
