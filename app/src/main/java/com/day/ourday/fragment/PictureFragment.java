package com.day.ourday.fragment;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.day.ourday.R;
import com.day.ourday.activity.MainActivity;

import java.io.IOException;

import static android.app.Activity.RESULT_OK;

public class PictureFragment extends Fragment {

    private static final int REQUEST_CODE_GALLERY = 0x10;
    private Bitmap bitmap;
    private View view;
    private View confirm;
    public PictureFragment() {
        // Required empty public constructor
    }

    public static PictureFragment newInstance() {
        return new PictureFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_picture, container, false);

        ImageView imageView = view.findViewById(R.id.pick);
        imageView.setOnClickListener(v -> pickPictureFromGallery());
        confirm = view.findViewById(R.id.confirm);
        confirm.setOnClickListener(v -> {
            MainActivity activity = (MainActivity) this.getActivity();
            activity.setBackground(bitmap);
        });
        View back = view.findViewById(R.id.cancel);
        back.setOnClickListener(view -> getFragmentManager().popBackStack());
        return view;
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
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // 以startActivityForResult的方式启动一个activity用来获取返回的结果
        startActivityForResult(intent, REQUEST_CODE_GALLERY);

    }

    private void displayImage(Uri imageUri) throws IOException {
        bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
        view.setBackground(new BitmapDrawable(getContext().getResources(), bitmap));
        confirm.setVisibility(View.VISIBLE);

        //todo yasuo

    }

    public interface BackgroundListener {
        void setBackground(Bitmap bitmap);
    }
}
