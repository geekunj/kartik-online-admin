package com.example.kartikonlinefirebase.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;
import com.example.kartikonlinefirebase.R;
import com.example.kartikonlinefirebase.activities.EditProductInfoActivity;
import com.example.kartikonlinefirebase.interfaces.OnMenuSaveButonClickListener;
import com.example.kartikonlinefirebase.viewmodels.CatalogueProductViewModel;
import com.example.kartikonlinefirebase.viewmodels.ProductViewModel;
import com.google.firebase.firestore.FirebaseFirestore;
import com.orhanobut.logger.Logger;
import static com.example.kartikonlinefirebase.utils.Config.mStaticProduct;


public class CatalogueItemInventoryFragment extends Fragment implements OnMenuSaveButonClickListener {

    EditProductInfoActivity editProductInfoActivity;
    private Switch outOfStockSwitch, showOutOfStockSwitch, forceAllowOrderSwitch;
    private EditText availableQtyText;
    private ProductViewModel productViewModel;



    public CatalogueItemInventoryFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        productViewModel = ViewModelProviders.of(requireActivity()).get(ProductViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_catalogue_item_inventory, container, false);

        editProductInfoActivity = (EditProductInfoActivity) getActivity();
//        setHasOptionsMenu(true);

        outOfStockSwitch = (Switch) view.findViewById(R.id.switch_out_of_stock);
        showOutOfStockSwitch = (Switch) view.findViewById(R.id.switch_show_out_of_stock);
        forceAllowOrderSwitch = (Switch) view.findViewById(R.id.switch_force_allow_order);
        availableQtyText = (EditText) view.findViewById(R.id.et_available_qty);

        availableQtyText.addTextChangedListener(new TextWatcher() {
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
                    productViewModel.setAvailableQuantity(s.toString());
                }

            }
        });

        outOfStockSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                productViewModel.onDataChanged();
                productViewModel.setIsOutOfStock(isChecked);
            }
        });

        forceAllowOrderSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                productViewModel.onDataChanged();
                productViewModel.setIsForceAllowOrder(isChecked);
            }
        });

        showOutOfStockSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                productViewModel.onDataChanged();
                productViewModel.setIsShowOutOfStock(isChecked);
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
//                setItemInventoryInfo();
//                Toast.makeText(getActivity(), "item inventory saved", Toast.LENGTH_SHORT).show();
//                Log.e("CatalogueInventory", "item inventory saved " + mStaticProduct.toString());
//                return true;
//
//
//            default: break;
//        }
//
//        return false;
//    }

    private void setItemInventoryInfo() {



        productViewModel.setAvailableQuantity(availableQtyText.getText().toString());
        productViewModel.setIsOutOfStock(outOfStockSwitch.isChecked());
        productViewModel.setIsShowOutOfStock(showOutOfStockSwitch.isChecked());
        productViewModel.setIsForceAllowOrder(forceAllowOrderSwitch.isChecked());

        mStaticProduct.setAvailableQuantity(productViewModel.getAvailableQuantity().getValue());
        mStaticProduct.setIsOutOfStock(productViewModel.getIsOutOfStock().getValue());
        mStaticProduct.setIsShowOutOfStock(productViewModel.getIsShowOutOfStock().getValue());
        mStaticProduct.setIsForceAllowOrder(productViewModel.getIsForceAllowOrder().getValue());

        Logger.e("CatalogueItemInventory "+ productViewModel.getPrice().getValue());

    }

    @Override
    public void onMenuButonClick() {

        setItemInventoryInfo();

    }
}
