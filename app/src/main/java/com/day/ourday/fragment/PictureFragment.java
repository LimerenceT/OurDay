package com.day.ourday.fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;

import com.day.ourday.R;
import com.day.ourday.adapter.PictureListAdapter;
import com.day.ourday.databinding.FragmentPictureBinding;
import com.day.ourday.viewmodel.PictureViewModel;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;

public class PictureFragment extends Fragment {

    private static final int REQUEST_CODE_GALLERY = 0x10;
    private FragmentPictureBinding dataBinding;
    private PictureViewModel pictureViewModel;
    private String fileName;


    public PictureFragment() {
        // Required empty public constructor
    }

    static PictureFragment newInstance() {
        return new PictureFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        dataBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_picture, container, false);
        initView();
        return dataBinding.getRoot();
    }

    private void initView() {
        pictureViewModel = ViewModelProviders.of(requireActivity()).get(PictureViewModel.class);
        dataBinding.pick.setOnClickListener(v -> pickPictureFromGallery());
        dataBinding.confirm.setOnClickListener(v -> {
            pictureViewModel.getMainBgUri().setValue(fileName);
            SharedPreferences sharedPreferences = getContext().getSharedPreferences("bg", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("bgp", fileName);
            getFragmentManager().popBackStack();
        });
        dataBinding.cancel.setOnClickListener(view -> getFragmentManager().popBackStack());
        PictureListAdapter pictureListAdapter = new PictureListAdapter();
        ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(getContext().getFilesDir().list()));
        pictureListAdapter.setPictureList(arrayList);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 4);
        dataBinding.pictureList.setLayoutManager(gridLayoutManager);
        dataBinding.pictureList.setAdapter(pictureListAdapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_GALLERY) {
            try {
                displayImage(data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void pickPictureFromGallery() {
        Intent intent=new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
        startActivityForResult(intent, REQUEST_CODE_GALLERY);
    }

    private void displayImage(Uri imageUri) throws IOException {
        dataBinding.confirm.setVisibility(View.VISIBLE);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig= Bitmap.Config.RGB_565;
        ParcelFileDescriptor parcelFileDescriptor =
                getContext().getContentResolver().openFileDescriptor(imageUri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options);
        parcelFileDescriptor.close();

        fileName = UUID.randomUUID().toString() + ".jpg";

        File file = new File(getContext().getFilesDir(), fileName);
        if (!file.exists()) {
            try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, fileOutputStream);
            }
        }
        dataBinding.previewBg.setImageBitmap(bitmap);
    }

}
