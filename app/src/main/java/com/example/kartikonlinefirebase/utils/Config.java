package com.example.kartikonlinefirebase.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.ActivityCompat;

import com.example.kartikonlinefirebase.models.Catalogue;
import com.example.kartikonlinefirebase.models.Product;

import java.util.List;

public class Config {

    public static Product mStaticProduct;
    public static Catalogue mStaticCatalogue;
    public static List<Product> mStaticProductList;
    public static List<Catalogue> mCatalogueList;
    public static Uri selectedImageUri;



    public static List<Catalogue> getmCatalogueList() {
        return mCatalogueList;
    }

    public static Product getmStaticProduct() {
        return mStaticProduct;
    }

    public static void setmStaticProduct(Product mStaticProduct) {
        Config.mStaticProduct = mStaticProduct;
    }

    public static void setmCatalogueList(List<Catalogue> mCatalogueList) {
        Config.mCatalogueList = mCatalogueList;
    }

    public static List<Product> getmStaticProductList() {
        return mStaticProductList;
    }

    public static void setmStaticProductList(List<Product> mStaticProductList) {
        Config.mStaticProductList = mStaticProductList;
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                for (String permission : permissions) {
                    if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                        return false;
                    }
                }
            }
        }
        return true;
    }




}
