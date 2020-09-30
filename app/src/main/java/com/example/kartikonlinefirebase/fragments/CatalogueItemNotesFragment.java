package com.example.kartikonlinefirebase.fragments;

import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import com.example.kartikonlinefirebase.R;
import com.example.kartikonlinefirebase.activities.EditProductInfoActivity;
import com.example.kartikonlinefirebase.interfaces.OnMenuSaveButonClickListener;
import com.example.kartikonlinefirebase.models.Product;
import com.example.kartikonlinefirebase.utils.Config;
import com.example.kartikonlinefirebase.viewmodels.ProductViewModel;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.orhanobut.logger.Logger;

import static com.example.kartikonlinefirebase.utils.Config.mStaticProduct;

public class CatalogueItemNotesFragment extends Fragment implements OnMenuSaveButonClickListener {

    private EditText productNotesText;

    private ProductViewModel productViewModel;

    EditProductInfoActivity editProductInfoActivity;
    private DatabaseReference mFirebaseDatabaseReference;
    private FirebaseFirestore mFirestore;
    private CollectionReference productCollection;

    public CatalogueItemNotesFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        productViewModel = ViewModelProviders.of(requireActivity()).get(ProductViewModel.class);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_catalogue_item_notes, container, false);

        mFirestore = FirebaseFirestore.getInstance();

        productNotesText = (EditText) view.findViewById(R.id.et_prod_notes);
//        setHasOptionsMenu(true);
        editProductInfoActivity = (EditProductInfoActivity) getActivity();
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();

        productCollection = mFirestore.collection("products");

        productNotesText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.length() > 1){
                    productViewModel.onDataChanged();
                    productViewModel.setNotes(s.toString());
                }

            }
        });


        return view;
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
//                setItemMoreDetails();
//                Config.setmStaticProduct(editProductInfoActivity.product);
//                Toast.makeText(getActivity(), "item note saved", Toast.LENGTH_SHORT).show();
//                Logger.e("CatalogueNotes "+ productViewModel.getProductName().getValue());
//                return true;
//
//            default: break;
//        }
//
//        return false;
//    }

    private void setItemMoreDetails() {

        //mStaticProduct.setNotes(productNotesText.getText().toString());
        Logger.e("CatalogueNotes "+ productViewModel.getProductName().getValue());

        productViewModel.setNotes(productNotesText.getText().toString());

        mStaticProduct.setNotes(productViewModel.getNotes().getValue());
        mStaticProduct.setProductName(productViewModel.getProductName().getValue());
        mStaticProduct.setPrice(productViewModel.getPrice().getValue());
        mStaticProduct.setDiscountPrice(productViewModel.getDiscountPrice().getValue());
        mStaticProduct.setCartonQuanity(productViewModel.getCartonQuanity().getValue());
        mStaticProduct.setSetQuantity(productViewModel.getSetQuantity().getValue());
        mStaticProduct.setSize(productViewModel.getSize().getValue());
        mStaticProduct.setSizeSelection(productViewModel.getSizeSelection().getValue());
        mStaticProduct.setColor(productViewModel.getColor().getValue());
        mStaticProduct.setColorSelection(productViewModel.getColorSelection().getValue());
        mStaticProduct.setCategoryName(productViewModel.getCategoryName().getValue());
        mStaticProduct.setSortTags(productViewModel.getSortTags().getValue());
        mStaticProduct.setType(productViewModel.getType().getValue());
        mStaticProduct.setSoleName(productViewModel.getSoleName().getValue());
        mStaticProduct.setDescription(productViewModel.getDescription().getValue());
        mStaticProduct.setIsOutOfStock(productViewModel.getIsOutOfStock().getValue());
        mStaticProduct.setIsForceAllowOrder(productViewModel.getIsForceAllowOrder().getValue());
        mStaticProduct.setIsShowOutOfStock(productViewModel.getIsShowOutOfStock().getValue());
        mStaticProduct.setAvailableQuantity(productViewModel.getAvailableQuantity().getValue());
        mStaticProduct.setVideoUrl(productViewModel.getVideoUrl().getValue());
        mStaticProduct.setNotes(productViewModel.getNotes().getValue());

        productCollection.add(mStaticProduct);



        /*mFirebaseDatabaseReference.child("products").push().setValue(mStaticProduct, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if(databaseError == null){
                    String key = databaseReference.getKey();

                }else{
                    Log.w("CatalogueMain", "unable to write message to database", databaseError.toException());
                }
            }
        });*/


    }

    @Override
    public void onMenuButonClick() {
        setItemMoreDetails();
    }
}
