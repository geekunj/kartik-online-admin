package com.example.kartikonlinefirebase.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.kartikonlinefirebase.R;
import com.example.kartikonlinefirebase.activities.CatalogueMain;
import com.example.kartikonlinefirebase.adapters.AttributesAdapter;
import com.example.kartikonlinefirebase.interfaces.ImageUploadListener;
import com.example.kartikonlinefirebase.models.Category;
import com.example.kartikonlinefirebase.models.Product;
import com.example.kartikonlinefirebase.utils.BitmapTransformer;
import com.example.kartikonlinefirebase.viewmodels.CategoryViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import static android.app.Activity.RESULT_OK;

public class AddCategoryDialogFragment extends DialogFragment implements AttributesAdapter.OnItemClickListener {

    private static final int MAX_WIDTH = 1024;
    private static final int MAX_HEIGHT = 768;
    private Uri uri;
    private View mRootView;
    private TextView dialogTitle;
    private TextInputEditText categoryNameEditText;
    private ImageView deleteCategoryImageButton;
    private ImageView categoryImage;
    private Button saveButton;
    private Button cancelButton;
    private RecyclerView categoryRecycler;
    private AttributesAdapter mAdapter;
    private Category category;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private FirebaseFirestore mFirestore;
    private CollectionReference categoryCollection;
    private StorageReference storageReference;
    private Query mQuery;
    private CategoryViewModel categoryViewModel;


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

        mRootView = inflater.inflate(R.layout.dialog_add_category, container, false);
        dialogTitle = mRootView.findViewById(R.id.tv_add_category_dialog_title);
        categoryNameEditText = mRootView.findViewById(R.id.tiet_category_name);
        deleteCategoryImageButton = mRootView.findViewById(R.id.iv_delete_category_btn);
        categoryImage = mRootView.findViewById(R.id.iv_category);
        cancelButton = mRootView.findViewById(R.id.btn_cancel);
        saveButton = mRootView.findViewById(R.id.btn_save);
        categoryRecycler = mRootView.findViewById(R.id.rv_category_list);

        category = new Category();

        /*categoryViewModel = ViewModelProviders.of(requireActivity()).get(CategoryViewModel.class);
        categoryViewModel.getCategoryImageUrl().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                categoryViewModel.isUploaded.setValue(true);
                category.setCategoryImageUrl(s);
            }
        });*/



        categoryNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if(s.length() > 2){
                    category.setCategoryName(s.toString());
                }

            }
        });


        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        mFirestore = FirebaseFirestore.getInstance();

        categoryCollection = mFirestore.collection("categories");

        mQuery = mFirestore.collection("categories").orderBy("categoryName").limit(50);

        mAdapter = new AttributesAdapter(mQuery, this, getContext()){

            @Override
            protected void onDataChanged() {
                // Show/hide content if the query returns empty.
                if (getItemCount() == 0) {
                    categoryRecycler.setVisibility(View.GONE);

                } else {
                    categoryRecycler.setVisibility(View.VISIBLE);

                }
            }

            @Override
            protected void onError(FirebaseFirestoreException e) {
                Log.e("error", e.getMessage());
            }
        };

        categoryRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        categoryRecycler.setAdapter(mAdapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                categoryImage.setImageResource(R.drawable.ic_add_circle_black_24dp);
                mAdapter.deleteCategory(viewHolder.getAdapterPosition());
            }
        }).attachToRecyclerView(categoryRecycler);



        categoryImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent,"Select Picture"), 1);
            }
        });

        deleteCategoryImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoryImage.setImageResource(R.drawable.ic_add_circle_black_24dp);
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
                        .child("category-images")
                        .child(uri.getLastPathSegment());

                putImageInStorage(storageReference, uri);

            }
        });


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
                    Glide.with(this)
                            .load(bitmap)
                            .transform(new BitmapTransformer(MAX_WIDTH, MAX_HEIGHT))
                            .into(categoryImage);

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
                                                category.setCategoryImageUrl(task.getResult().toString());
                                                categoryCollection.add(category);

                                                Toast.makeText(getActivity(), "Category Added", Toast.LENGTH_SHORT).show();
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

        Category category = documentSnapshot.toObject(Category.class);

        categoryNameEditText.setText(category.getCategoryName().trim());

        Glide.with(getContext())
                .load(category.getCategoryImageUrl())
                .into(categoryImage);

        dialogTitle.setText("Edit Category");

    }
}
