package com.example.kartikonlinefirebase.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.kartikonlinefirebase.R;
import com.example.kartikonlinefirebase.adapters.BannerAdapter;
import com.example.kartikonlinefirebase.models.Banner;
import com.example.kartikonlinefirebase.utils.BitmapTransformer;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class AddSetQtyFragment extends DialogFragment implements BannerAdapter.OnItemClickListener {

    private String[] BANNER_POS = new String[] {"Top", "Bottom"};
    private List<String> catalogueList;
    private List<String> catalogueIdList;
    private static final int MAX_WIDTH = 1024;
    private static final int MAX_HEIGHT = 768;

    private View mRootView;
    private TextView dialogTitle;
    private ImageView deleteBannerButton;
    private ImageView bannerImage;
    private AutoCompleteTextView bannerPosDropDown, catalogueDropDown;
    private ArrayAdapter<String> bannerPosAdapter, catalogueAdapter;
    private Button saveButton;
    private Button cancelButton;
    private Switch clickablePropertySwitch;
    private RecyclerView bannerRecycler;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private StorageReference storageReference;
    private FirebaseFirestore mFirestore;
    private CollectionReference catalogueCollection;
    private CollectionReference bannerCollection;
    private Query mQuery;

    private BannerAdapter mAdapter;

    private Banner banner;

    private Uri uri;

    @Override
    public void onStart() {
        super.onStart();
        if (mAdapter != null) {
            mAdapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAdapter != null) {
            mAdapter.stopListening();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mRootView = inflater.inflate(R.layout.dialog_add_banner, container, false);

        dialogTitle = mRootView.findViewById(R.id.tv_add_banner_dialog_title);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mFirestore = FirebaseFirestore.getInstance();
        catalogueCollection = mFirestore.collection("catalogues");
        bannerCollection = mFirestore.collection("banners");

        banner = new Banner();

        deleteBannerButton = mRootView.findViewById(R.id.iv_delete_banner);
        bannerImage = mRootView.findViewById(R.id.iv_banner);
        cancelButton = mRootView.findViewById(R.id.btn_cancel);
        saveButton = mRootView.findViewById(R.id.btn_save);
        clickablePropertySwitch = mRootView.findViewById(R.id.switch_clickable);
        bannerPosDropDown = mRootView.findViewById(R.id.pos_selec_dropdown);
        catalogueDropDown = mRootView.findViewById(R.id.catalog_selec_dropdown);
        bannerRecycler = mRootView.findViewById(R.id.rv_banner_list);

        catalogueList = new ArrayList<>();
        catalogueIdList = new ArrayList<>();
        catalogueCollection.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(DocumentSnapshot document: queryDocumentSnapshots){
                    Log.d("NIKUNJ", document.getId() + " => " + document.getData());
                    catalogueList.add(document.getString("catalogueTitle"));
                    catalogueIdList.add(document.getId());
                    /*count++;
                    if (count == 1){
                        docFoundId = document.getId();
                    }*/
                }
            }
        });

        bannerImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent,"Select Picture"), 1);
            }
        });

        deleteBannerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bannerImage.setImageResource(R.drawable.ic_add_circle_black_24dp);
            }
        });


        bannerPosAdapter = new ArrayAdapter<>(getContext(), R.layout.support_simple_spinner_dropdown_item, BANNER_POS);
        catalogueAdapter = new ArrayAdapter<>(getContext(), R.layout.support_simple_spinner_dropdown_item, catalogueList);

        bannerPosDropDown.setAdapter(bannerPosAdapter);
        catalogueDropDown.setAdapter(catalogueAdapter);

        bannerPosDropDown.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("NIKUNJ", "position" + " => " + position);
                banner.setPositionOnUser(BANNER_POS[position]);
            }
        });

        catalogueDropDown.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("NIKUNJ", "position" + " => " + position);
                banner.setCatalogueId(catalogueIdList.get(position));
                banner.setCatalogueTitle(catalogueList.get(position));
            }
        });



        clickablePropertySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d("NIKUNJ", "position" + " => " + isChecked + "");
                banner.setClickable(isChecked);
            }
        });


        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                storageReference = FirebaseStorage.getInstance().getReference(
                        mFirebaseUser.getUid())
                        .child("banner-images")
                        .child(banner.getCatalogueId())
                        .child(uri.getLastPathSegment());

                putImageInStorage(storageReference, uri);

            }
        });

        mQuery = mFirestore.collection("banners").orderBy("catalogueTitle").limit(50);

        mAdapter = new BannerAdapter(mQuery, this, getContext()){

            @Override
            protected void onDataChanged() {
                // Show/hide content if the query returns empty.
                if (getItemCount() == 0) {
                    bannerRecycler.setVisibility(View.GONE);

                } else {
                    bannerRecycler.setVisibility(View.VISIBLE);

                }
            }

            @Override
            protected void onError(FirebaseFirestoreException e) {
                Log.e("error", e.getMessage());
            }
        };

        bannerRecycler.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        bannerRecycler.setAdapter(mAdapter);


        return mRootView;

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
                    Glide.with(this).load(bitmap).transform(new BitmapTransformer(MAX_WIDTH, MAX_HEIGHT)).into(bannerImage);

                }
            }

        }
    }

    private void putImageInStorage(StorageReference storageReference, Uri uri) {
        storageReference.putFile(uri).addOnCompleteListener(getActivity(),
                new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            task.getResult().getMetadata().getReference().getDownloadUrl()
                                    .addOnCompleteListener(getActivity(), new OnCompleteListener<Uri>() {
                                        @Override
                                        public void onComplete(Task<Uri> task) {
                                            if (task.isSuccessful()) {
                                                //callback.onImageUploaded(task.getResult().toString());
                                                //categoryViewModel.setCategoryImageUrl(task.getResult().toString());
                                                banner.setBannerImageUrl(task.getResult().toString());
                                                bannerCollection.add(banner);

                                                Toast.makeText(getActivity(), "Banner Added", Toast.LENGTH_SHORT).show();
                                                dismiss();

                                            }
                                        }
                                    });
                        } else {
                            Log.w("CatalogueMain", "Image upload task was not successful.",
                                    task.getException());
                        }
                    }
                });
    }

    @Override
    public void onItemClick(DocumentSnapshot documentSnapshot, int position) {

        Banner banner = documentSnapshot.toObject(Banner.class);

        catalogueDropDown.setText(banner.getCatalogueTitle().trim());
        bannerPosDropDown.setText(banner.getPositionOnUser());
        clickablePropertySwitch.setChecked(banner.isClickable());

        Glide.with(getContext())
                .load(banner.getBannerImageUrl())
                .into(bannerImage);

        dialogTitle.setText("Edit Banner");

    }

}
