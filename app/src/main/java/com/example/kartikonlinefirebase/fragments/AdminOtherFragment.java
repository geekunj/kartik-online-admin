package com.example.kartikonlinefirebase.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.kartikonlinefirebase.R;
import com.example.kartikonlinefirebase.adapters.AttributesAdapter;
import com.example.kartikonlinefirebase.models.Color;
import com.example.kartikonlinefirebase.models.Quantity;
import com.example.kartikonlinefirebase.models.Set;
import com.example.kartikonlinefirebase.models.Category;
import com.example.kartikonlinefirebase.models.Size;
import com.example.kartikonlinefirebase.utils.BitmapTransformer;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.io.IOException;

import static android.app.Activity.RESULT_OK;

public class AdminOtherFragment extends Fragment implements View.OnClickListener {

    EditText addBanner, addCat, addSize, addColor, addQty, addSetQty, notify, myEditText;
    private FirebaseFirestore mFirestore;
    private Query mQuery;
    private CollectionReference quantityCollection, setCollection;
    private CollectionReference categoryCollection, sizeCollection, colorCollection;
    private Quantity mQuantity;
    private Set mSet;
    private Category mCategory;
    private Size mSize;
    private com.example.kartikonlinefirebase.models.Color mColor;
    private AttributesAdapter mAdapter;

    private String[] BANNER_POS = new String[] {"Top", "Bottom"};
    private static final int MAX_WIDTH = 1024;
    private static final int MAX_HEIGHT = 768;
    private View mRootView;
    private ImageView deleteBannerButton;
    private ImageView bannerImage;
    private AutoCompleteTextView bannerPosDropDown;
    private ArrayAdapter<String> bannerPosAdapter;
    private Button saveButton;
    private Button cancelButton;

    private AddBannerDialogFragment addBannerDialogFragment;
    private AddCategoryDialogFragment addCategoryDialogFragment;
    //private FirestoreRecyclerAdapter<Category, CategoryViewHolder> adapter;

    private FragmentTransaction ft;

    public AdminOtherFragment() {

    }

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_admin_other, container, false);

        addBanner = view.findViewById(R.id.et_add_banner);
        addCat = view.findViewById(R.id.et_add_cat);
        addSize = view.findViewById(R.id.et_add_size);
        addColor = view.findViewById(R.id.et_add_color);
        addQty = view.findViewById(R.id.et_add_qty);
        addSetQty = view.findViewById(R.id.et_add_set_qty);
        notify = view.findViewById(R.id.et_notify);

        mFirestore = FirebaseFirestore.getInstance();
        //mQuery = mFirestore.collection("Quantity");

        quantityCollection = mFirestore.collection("quantities");
        setCollection = mFirestore.collection("sets");
        categoryCollection = mFirestore.collection("categories");
        sizeCollection = mFirestore.collection("sizes");
        colorCollection = mFirestore.collection("colors");

        mQuantity = new Quantity();
        mSet = new Set();
        mCategory = new Category();
        mSize = new Size();
        mColor = new Color();

        addBanner.setOnClickListener(this);
        addCat.setOnClickListener(this);
        addSize.setOnClickListener(this);
        addColor.setOnClickListener(this);
        addQty.setOnClickListener(this);
        addSetQty.setOnClickListener(this);
        notify.setOnClickListener(this);





        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.et_add_banner:

                addBannerDialogFragment = new AddBannerDialogFragment();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                addBannerDialogFragment.show(ft, "dialog");





                break;
            case R.id.et_add_cat:

                addCategoryDialogFragment = new AddCategoryDialogFragment();
                ft = getActivity().getSupportFragmentManager().beginTransaction();
                addCategoryDialogFragment.show(ft, "dialog");

                /*AlertDialog addCategoryDialog = new MaterialAlertDialogBuilder(getContext())
                        .setTitle("Add Category")
                        .setView(R.layout.dialog_add_attributes)
                        .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                EditText addCategoryText = (EditText)((AlertDialog) dialog).findViewById(R.id.et_dialog);
                                if(!TextUtils.isEmpty(addCategoryText.getText().toString())){
                                    mCategory.setCategoryName(addCategoryText.getText().toString());
                                }
                                categoryCollection.add(mCategory);
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();
                addCategoryDialog.show();
                final RecyclerView categoryRecycler = addCategoryDialog.findViewById(R.id.rv_attribute_list);

                mQuery = mFirestore.collection("categories").orderBy("categoryName").limit(50);

                *//*FirestoreRecyclerOptions<Category> options = new FirestoreRecyclerOptions.Builder<Category>()
                        .setQuery(mQuery, Category.class)
                        .build();

                adapter = new FirestoreRecyclerAdapter<Category, CategoryViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull CategoryViewHolder holder, int position, @NonNull Category category) {
                        holder.setAttributeName(category.getCategoryName());
                    }

                    @NonNull
                    @Override
                    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.attributes_list_item, parent, false);
                        return new CategoryViewHolder(view);
                    }
                };*//*

                mAdapter = new AttributesAdapter(mQuery){

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
                categoryRecycler.setAdapter(mAdapter);*/


                break;
            case R.id.et_notify:
                break;
            case R.id.et_add_size:
                new MaterialAlertDialogBuilder(getContext())
                        .setTitle("Add Size")
                        .setView(R.layout.dialog_add_attributes)
                        .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                EditText sizeText = (EditText) ((AlertDialog) dialog).findViewById(R.id.et_dialog);
                                if(!TextUtils.isEmpty(sizeText.getText().toString())){
                                    mSize.setSize(sizeText.getText().toString());
                                }
                                sizeCollection.add(mSize);
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
                break;
            case R.id.et_add_color:
                new MaterialAlertDialogBuilder(getContext())
                        .setTitle("Add Color")
                        .setView(R.layout.dialog_add_attributes)
                        .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                EditText sizeText = (EditText) ((AlertDialog) dialog).findViewById(R.id.et_dialog);
                                if(!TextUtils.isEmpty(sizeText.getText().toString())){
                                    mColor.setColorName(sizeText.getText().toString());
                                }
                                colorCollection.add(mColor);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();

               /* final ColorPicker colorPicker = new ColorPicker(getActivity());
                colorPicker.setOnChooseColorListener(new ColorPicker.OnChooseColorListener() {
                    @Override
                    public void onChooseColor(int position,int color) {
                        // put code
                    }

                    @Override
                    public void onCancel(){
                        // put code
                    }
                })
                        .addListenerButton("newButton", new ColorPicker.OnButtonListener() {
                            @Override
                            public void onClick(View v, int position, int color) {
                                // put code
                            }
                        })
                        .disableDefaultButtons(false)
                        .setDefaultColorButton(Color.parseColor("#f84c44"))
                        .setColumns(5)
                        .setRoundColorButton(true)
                        .setTitlePadding(0,0,0,14)
                        .show();*/

                break;
            case R.id.et_add_qty:
                new MaterialAlertDialogBuilder(getContext())
                        .setTitle("Add Quantity")
                        .setView(R.layout.dialog_add_attributes)
                        .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                EditText addQuantityText = (EditText)((AlertDialog) dialog).findViewById(R.id.et_dialog);

                                if(!TextUtils.isEmpty(addQuantityText.getText().toString())){
                                    String qty = addQuantityText.getText().toString();
                                    mQuantity.setQuantity(qty);
                                }
                                quantityCollection.add(mQuantity);
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
                break;
            case R.id.et_add_set_qty:
                new MaterialAlertDialogBuilder(getContext())
                        .setTitle("Add Set Quantity")
                        .setView(R.layout.dialog_add_attributes)
                        .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                EditText addSetText = (EditText)((AlertDialog) dialog).findViewById(R.id.et_dialog);

                                if(!TextUtils.isEmpty(addSetText.getText().toString())){
                                    String set = addSetText.getText().toString();
                                    mSet.setSetQty(set);
                                }
                                setCollection.add(mSet);

                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
                break;


        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1){

            if(resultCode == RESULT_OK) {
                if (data != null) {

                    Uri uri = data.getData();
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

}
