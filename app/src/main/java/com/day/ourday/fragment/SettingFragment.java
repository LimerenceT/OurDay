package com.day.ourday.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.day.ourday.R;
import com.day.ourday.databinding.FragmentSettingBinding;

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

        FragmentSettingBinding dataBinding = FragmentSettingBinding.inflate(inflater, container, false);
        dataBinding.cancel.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_settingFragment_to_mainFragment));

        dataBinding.chooseBg.setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_settingFragment_to_pictureFragment));
//                getFragmentManager().beginTransaction()
//                        .addToBackStack(null).hide(this)
//                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
//                        .add(R.id.fragment_container, PictureFragment.newInstance())
//                        .commit());
        return dataBinding.getRoot();
    }

    @Override
    public void onDestroyView() {

        super.onDestroyView();
    }
}
