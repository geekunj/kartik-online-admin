package com.example.kartikonlinefirebase.fragments;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.kartikonlinefirebase.R;
import com.example.kartikonlinefirebase.activities.EditProductInfoActivity;
import com.example.kartikonlinefirebase.interfaces.OnMenuSaveButonClickListener;
import com.example.kartikonlinefirebase.interfaces.TextWatcherInterface;
import com.example.kartikonlinefirebase.models.Product;
import com.example.kartikonlinefirebase.utils.Config;

import com.example.kartikonlinefirebase.utils.TabChangedEvent;
import com.example.kartikonlinefirebase.utils.TextChangedEvent;
import com.example.kartikonlinefirebase.viewmodels.CatalogueProductViewModel;
import com.example.kartikonlinefirebase.viewmodels.ProductViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.example.kartikonlinefirebase.utils.Config.mStaticProduct;

public class CatalogueItemInfoFragment extends Fragment implements OnMenuSaveButonClickListener{

    private static final String TAG = "CatalogueItemInfo";
    private static final String[] GENDERS = new String[] {"Men", "Women", "Kids"};
    private List<String> qtyList;
    private List<String> setList;
    private List<String> categoryList;
    private List<String> sizeList;
    private List<String> colorList;

    private List<Product> productList;
    private List<String> list;
    EditProductInfoActivity editProductInfoActivity;
//    EventBus bus = EventBus.getDefault();
//    EventBus mBus = EventBus.getDefault();
    private FirebaseFirestore mFirestore;
    private AutoCompleteTextView cnQtyDropdown, setQtyDropdown, sizeDropdown;
    private AutoCompleteTextView colorSelectDropdown, catSelectDropdown, genderDropdown;
    private EditText productNameText, productPriceText, productDiscountPriceText;
    private EditText productSizeSelectionText, productColorSelectionText, productSortTagsText;
    private EditText productSoleNameText, productDescriptionText;

    private ArrayAdapter<String> genderAdapter,qtyAdapter,mSetAdapter,categoryAdapter,sizeAdapter,colorAdapter;

    private CatalogueProductViewModel catalogueProductViewModel;

    private ProductViewModel productViewModel;

    private Query mQuery;
    private TextWatcherInterface textWatcherInterface;
    private Button testButton;


    public CatalogueItemInfoFragment() {

    }

    TextWatcher textWatcher;


    @Override
    public void onStart() {
        super.onStart();
        //bus.register(this);
        //mBus.register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        //bus.register(this);
        //mBus.unregister(this);
    }

    @Subscribe
    public void onEvent(TabChangedEvent event){
        performValidationAndSetViewModel();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_catalogue_item_info, container, false);

        catalogueProductViewModel = ViewModelProviders.of(requireActivity()).get(CatalogueProductViewModel.class);
        productViewModel = ViewModelProviders.of(requireActivity()).get(ProductViewModel.class);

        productViewModel.getProductName().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {

            }
        });

        editProductInfoActivity = (EditProductInfoActivity) getActivity();

        initViews(view);

        mFirestore = FirebaseFirestore.getInstance();

        initAttributesLists();

        populateAttributesLists();

        initAttributesDropdownAdapters();

        genderDropdown.setAdapter(genderAdapter);
        cnQtyDropdown.setAdapter(qtyAdapter);
        setQtyDropdown.setAdapter(mSetAdapter);
        catSelectDropdown.setAdapter(categoryAdapter);
        sizeDropdown.setAdapter(sizeAdapter);
        colorSelectDropdown.setAdapter(colorAdapter);

        textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {




            }
        };




        productNameText.addTextChangedListener(new GenericTextWatcher(productNameText));
        productPriceText.addTextChangedListener(new GenericTextWatcher(productPriceText));
        productDiscountPriceText.addTextChangedListener(new GenericTextWatcher(productDiscountPriceText));
        productSizeSelectionText.addTextChangedListener(new GenericTextWatcher(productSizeSelectionText));
        productColorSelectionText.addTextChangedListener(new GenericTextWatcher(productColorSelectionText));
        productSortTagsText.addTextChangedListener(new GenericTextWatcher(productSortTagsText));
        productSoleNameText.addTextChangedListener(new GenericTextWatcher(productSoleNameText));
        productDescriptionText.addTextChangedListener(new GenericTextWatcher(productDescriptionText));

        cnQtyDropdown.addTextChangedListener(new GenericTextWatcher(cnQtyDropdown));
        setQtyDropdown.addTextChangedListener(new GenericTextWatcher(setQtyDropdown));
        sizeDropdown.addTextChangedListener(new GenericTextWatcher(sizeDropdown));
        colorSelectDropdown.addTextChangedListener(new GenericTextWatcher(colorSelectDropdown));
        catSelectDropdown.addTextChangedListener(new GenericTextWatcher(catSelectDropdown));
        genderDropdown.addTextChangedListener(new GenericTextWatcher(genderDropdown));





        productSizeSelectionText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(productSizeSelectionText.getText().toString())){
                    productSizeSelectionText.setText("");
                }
                String[] sizeStringArray = sizeList.toArray(new String[0]);
                final List<Integer> sizesSelected = new ArrayList<>();

                new MaterialAlertDialogBuilder(getContext())
                        .setTitle("Select Sizes")
                        .setMultiChoiceItems(sizeStringArray, null, new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                if (isChecked) {
                                    sizesSelected.add(which);
                                } else if (sizesSelected.contains(which)) {
                                    sizesSelected.remove(Integer.valueOf(which));
                                }
                            }
                        })
                        .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                for (int i : sizesSelected){
                                    productSizeSelectionText.append(sizeList.get(i) + ", ");
                                }

                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create()
                        .show();

            }
        });

        productColorSelectionText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(productColorSelectionText.getText().toString())){
                    productColorSelectionText.setText("");
                }
                String[] colorStringArray = colorList.toArray(new String[0]);
                final List<Integer> colorSelected = new ArrayList<>();

                new MaterialAlertDialogBuilder(getContext())
                        .setTitle("Select Sizes")
                        .setMultiChoiceItems(colorStringArray, null, new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                if (isChecked) {
                                    colorSelected.add(which);
                                } else if (colorSelected.contains(which)) {
                                    colorSelected.remove(Integer.valueOf(which));
                                }
                            }
                        })
                        .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                for (int i : colorSelected){
                                    productColorSelectionText.append(colorList.get(i) + ", ");
                                }

                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create()
                        .show();

            }
        });

        //setHasOptionsMenu(true);


        //TODO: get data from firebase for already existing items in a catalogue
        //TODO: set this data in respective Views if the user has come from an already created catalogue

        return view;
    }

    private void initViews(View view) {

        genderDropdown = view.findViewById(R.id.gender_dropdown);
        cnQtyDropdown = view.findViewById(R.id.cn_qty_dropdown);
        setQtyDropdown = view.findViewById(R.id.set_qty_dropdown);
        catSelectDropdown = view.findViewById(R.id.catagory_dropdown);
        sizeDropdown = view.findViewById(R.id.size_dropdown);
        colorSelectDropdown = view.findViewById(R.id.color_dropdown);
        testButton = view.findViewById(R.id.btn_test);
        productNameText = view.findViewById(R.id.et_item_name);
        productPriceText = view.findViewById(R.id.et_item_price);
        productDiscountPriceText = view.findViewById(R.id.et_item_disc_price);
        productSizeSelectionText = view.findViewById(R.id.et_size_selec);
        productColorSelectionText = view.findViewById(R.id.et_color_selec);
        //productSizeText = view.findViewById(R.id.et_item_size);
        productSortTagsText = view.findViewById(R.id.et_item_sort_tags);
        productSoleNameText = view.findViewById(R.id.et_item_sole_name);
        productDescriptionText = view.findViewById(R.id.et_item_desc);
    }
    private void initAttributesLists() {
        qtyList = new ArrayList<>();
        setList = new ArrayList<>();
        categoryList = new ArrayList<>();
        sizeList = new ArrayList<>();
        colorList = new ArrayList<>();
    }
    private void populateAttributesLists(){

        mFirestore.collection("quantities").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        qtyList.add(document.getData().get("quantity").toString());
                    }
                    Log.d(TAG, qtyList.toString());
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });

        mFirestore.collection("sets").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        setList.add(document.getData().get("setQty").toString());
                    }
                    Log.d(TAG, setList.toString());
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });

        mFirestore.collection("categories").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        categoryList.add(document.getData().get("categoryName").toString());
                    }
                    Log.d(TAG, categoryList.toString());
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });

        mFirestore.collection("sizes").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        sizeList.add(document.getData().get("size").toString());
                    }
                    Log.d(TAG, sizeList.toString());
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });

        mFirestore.collection("colors").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        colorList.add(document.getData().get("colorName").toString());
                    }
                    Log.d(TAG, colorList.toString());
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });

    }
    private void initAttributesDropdownAdapters() {

        genderAdapter = new ArrayAdapter<>(getContext(), R.layout.support_simple_spinner_dropdown_item, GENDERS);
        qtyAdapter = new ArrayAdapter<>(getContext(), R.layout.support_simple_spinner_dropdown_item, qtyList);
        mSetAdapter = new ArrayAdapter<>(getContext(), R.layout.support_simple_spinner_dropdown_item, setList);
        categoryAdapter = new ArrayAdapter<>(getContext(), R.layout.support_simple_spinner_dropdown_item, categoryList);
        sizeAdapter = new ArrayAdapter<>(getContext(), R.layout.support_simple_spinner_dropdown_item, sizeList);
        colorAdapter = new ArrayAdapter<>(getContext(), R.layout.support_simple_spinner_dropdown_item, colorList);

    }

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        super.onCreateOptionsMenu(menu, inflater);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//        switch (item.getItemId()){
//            case R.id.item_check:
//                setItemFromItemForm();
//                Toast.makeText(getActivity(), "item info saved", Toast.LENGTH_SHORT).show();
//                Log.e("CatalogueInfo", "item info saved " + mStaticProduct.getProductName());
//                return true;
//
//
//            default: break;
//        }
//
//        return false;
//    }

    public void performValidationAndSetViewModel() {

        if (TextUtils.isEmpty(productNameText.getText())) {
            productNameText.setError("item Name can't be empty");
        } else if (TextUtils.isEmpty(productPriceText.getText())) {
            productPriceText.setError("price can't be empty");
        } else if (TextUtils.isEmpty(productDiscountPriceText.getText())) {
            productDiscountPriceText.setError("price can't be empty");
        } else if (TextUtils.isEmpty(cnQtyDropdown.getText())) {
            cnQtyDropdown.setError("quantity can't be empty");
        } else if (TextUtils.isEmpty(setQtyDropdown.getText())) {
            setQtyDropdown.setError("quantity can't be empty");
        } else if (TextUtils.isEmpty(sizeDropdown.getText())) {
            sizeDropdown.setError("size can't be empty");
        } else if (TextUtils.isEmpty(productSizeSelectionText.getText().toString())) {
            productSizeSelectionText.setError("size selection can't be empty");
        } else if(TextUtils.isEmpty(colorSelectDropdown.getText())){
            colorSelectDropdown.setError("color can't be empty");
        } else if(TextUtils.isEmpty(productColorSelectionText.getText())){
            productColorSelectionText.setError("color selection cant be empty");
        } else if(TextUtils.isEmpty(catSelectDropdown.getText())) {
            catSelectDropdown.setError("category can't be empty");
        } else if(TextUtils.isEmpty(productSortTagsText.getText())){
            productSortTagsText.setError("sort tags can't be empty");
        } else if(TextUtils.isEmpty(genderDropdown.getText())){
            genderDropdown.setError("gender can't be empty");
        } else if(TextUtils.isEmpty(productSoleNameText.getText())){
            productSoleNameText.setError("sole name cant be empty");
        } else if(TextUtils.isEmpty(productDescriptionText.getText())){
            productDescriptionText.setError("description can't be empty");
        } else {
            try {
                productViewModel.setProductName(productNameText.getText().toString());
                productViewModel.setPrice(productPriceText.getText().toString());
                productViewModel.setDiscountPrice(productDiscountPriceText.getText().toString());
                productViewModel.setCartonQuanity(cnQtyDropdown.getText().toString());
                productViewModel.setSetQuantity(setQtyDropdown.getText().toString());
                productViewModel.setSize(sizeDropdown.getText().toString());
                productViewModel.setSizeSelection(productSizeSelectionText.getText().toString());
                productViewModel.setColor(colorSelectDropdown.getText().toString());
                productViewModel.setColorSelection(productColorSelectionText.getText().toString());
                productViewModel.setSortTags(productSortTagsText.getText().toString());
                productViewModel.setType(genderDropdown.getText().toString());
                productViewModel.setSoleName(productSoleNameText.getText().toString());
                productViewModel.setDescription(productDescriptionText.getText().toString());
                productViewModel.setCategoryName(catSelectDropdown.getText().toString());
            } catch (Exception e) {
                Toast.makeText(getActivity(), "Fill all the details correctly", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
            Logger.e("CatalogueItemInfo " + productViewModel.getProductName().getValue());
            Logger.e("CatalogueItemInfo " + productViewModel.getNotes().getValue());


        }
    }




    @Override
    public void onMenuButonClick() {
        performValidationAndSetViewModel();
    }


    public class GenericTextWatcher implements TextWatcher {

        private View view;


        public GenericTextWatcher(View view) {

            this.view = view;
        }




        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

            if(s.length() > 1){
                productViewModel.onDataChanged();
            }

            String text = s.toString();

            switch(view.getId()){

                case R.id.et_item_name:
                    productViewModel.setProductName(text);
                    break;

                case R.id.et_item_price:
                    productViewModel.setPrice(text);
                    break;

                case R.id.et_item_disc_price:
                    productViewModel.setDiscountPrice(text);
                    break;

                case R.id.et_size_selec:
                    productViewModel.setSize(text);
                    break;

                case R.id.et_color_selec:
                    productViewModel.setColor(text);
                    break;

                case R.id.et_item_sort_tags:
                    productViewModel.setSortTags(text);
                    break;

                case R.id.et_item_sole_name:
                    productViewModel.setSoleName(text);
                    break;

                case R.id.et_item_desc:
                    productViewModel.setDescription(text);
                    break;

                case R.id.gender_dropdown:
                    productViewModel.setType(text);
                    break;

                case R.id.cn_qty_dropdown:
                    productViewModel.setCartonQuanity(text);
                    break;

                case R.id.set_qty_dropdown:
                    productViewModel.setSetQuantity(text);
                    break;

                case R.id.catagory_dropdown:
                    productViewModel.setCategoryName(text);
                    break;

                case R.id.size_dropdown:
                    productViewModel.setSizeSelection(text);
                    break;

                case R.id.color_dropdown:
                    productViewModel.setColorSelection(text);
                    break;

            }

        }
    }

}
