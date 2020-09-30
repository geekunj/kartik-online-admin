/**
 * Copyright 2017 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
 package com.example.kartikonlinefirebase.adapters;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kartikonlinefirebase.R;
import com.example.kartikonlinefirebase.models.Product;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;


/**
 * RecyclerView adapter for a bunch of Ratings.
 */
public class ProductAdapter extends FirestoreAdapter<ProductAdapter.ViewHolder> {

    public ProductAdapter(Query query) {
        super(query);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.products_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getSnapshot(position));
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView prodImage;
        TextView prodTitleText;
        TextView prodPriceText;
        TextView prodDiscPriceText;
        TextView prodDescText;
        TextView prodSizeText;
        TextView prodGenderText;
        TextView prodSoleNameText;
        TextView prodColorText;
        ProgressBar imageUploadProgress;

        public ViewHolder(View itemView) {
            super(itemView);
            prodImage = itemView.findViewById(R.id.iv_prod_image);
            imageUploadProgress = itemView.findViewById(R.id.pb_image_upload);
            prodTitleText = itemView.findViewById(R.id.tv_prod_title);
            prodPriceText = itemView.findViewById(R.id.tv_prod_price);
            prodDiscPriceText = itemView.findViewById(R.id.tv_prod_disc_price);
            prodSizeText = itemView.findViewById(R.id.tv_prod_size);
            prodGenderText = itemView.findViewById(R.id.tv_prod_gender);
            prodSoleNameText = itemView.findViewById(R.id.tv_prod_sole_name);
            prodColorText = itemView.findViewById(R.id.tv_prod_color);
            prodDescText = itemView.findViewById(R.id.tv_prod_desc);
        }

        public void bind(final DocumentSnapshot snapshot) {

            try {
                Product product = snapshot.toObject(Product.class);

                if(product != null){
                    imageUploadProgress.setVisibility(View.GONE);
                }
                //productImageView.setImageBitmap(product.getImages().get());
                prodTitleText.setText(product.getProductName());
                prodPriceText.setText(product.getPrice() + "");
                prodDiscPriceText.setPaintFlags(prodDiscPriceText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                prodDiscPriceText.setText(product.getDiscountPrice() + "");
                prodSizeText.setText(product.getSizeSelection());
                prodGenderText.setText(product.getType());
                prodSoleNameText.setText(product.getSoleName());
                prodColorText.setText(product.getColor());
                prodDescText.setText(product.getDescription());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
