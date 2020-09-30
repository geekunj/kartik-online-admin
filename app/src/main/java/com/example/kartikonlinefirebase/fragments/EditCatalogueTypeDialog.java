package com.example.kartikonlinefirebase.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.kartikonlinefirebase.R;
import com.example.kartikonlinefirebase.utils.BitmapTransformer;

import java.io.IOException;

import static android.app.Activity.RESULT_OK;


public class EditCatalogueTypeDialog extends DialogFragment {

    private static final String[] TYPES = new String[] {"Our Sellers", "Featured Brands"};
    private static final int MAX_WIDTH = 1024;
    private static final int MAX_HEIGHT = 768;
    private Uri uri;

    private ArrayAdapter<String> catalogueTypeAdapter;
    private AutoCompleteTextView catalogueTypeDropdown;
    private Button saveButton;
    private Button cancelButton;
    private ImageView deleteCatalogueImageButton;
    private ImageView catalogueImage;

    public EditCatalogueTypeDialog() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_edit_catalogue_type_dialog, container, false);

        catalogueTypeDropdown = view.findViewById(R.id.catalogue_type_dropdown);
        saveButton = view.findViewById(R.id.btn_save);
        cancelButton = view.findViewById(R.id.btn_cancel);
        catalogueImage = view.findViewById(R.id.iv_catalogue);
        deleteCatalogueImageButton  = view.findViewById(R.id.iv_delete_catalogue_btn);

        catalogueTypeAdapter = new ArrayAdapter<>(getContext(), R.layout.support_simple_spinner_dropdown_item, TYPES);
        catalogueTypeDropdown.setAdapter(catalogueTypeAdapter);

        catalogueImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent,"Select Picture"), 1);
            }
        });

        deleteCatalogueImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                catalogueImage.setImageResource(R.drawable.ic_add_circle_black_24dp);
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1){

            if(resultCode == RESULT_OK) {
                if (data != null) {

                    uri = data.getData();
                    Bitmap bitmap = null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Glide.with(this)
                            .load(bitmap)
                            .transform(new BitmapTransformer(MAX_WIDTH, MAX_HEIGHT))
                            .into(catalogueImage);

                }
            }

        }
    }

}
