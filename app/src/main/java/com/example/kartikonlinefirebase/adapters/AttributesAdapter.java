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

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.kartikonlinefirebase.R;
import com.example.kartikonlinefirebase.models.Category;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class AttributesAdapter extends FirestoreAdapter<AttributesAdapter.CategoryViewHolder> {

    private StorageReference storageReference;
    private OnItemClickListener listener;
    private Context context;

    public AttributesAdapter(Query query, OnItemClickListener listener, Context context) {
        super(query);
        this.listener = listener;
        this.context = context;
    }

    public interface OnItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new CategoryViewHolder(inflater.inflate(R.layout.attributes_list_item, parent, false));
    }

    public void deleteCategory(int position){
        String categoryImageUrl;
        categoryImageUrl = getSnapshot(position).getString("categoryImageUrl");
        if(categoryImageUrl != null && !TextUtils.isEmpty(categoryImageUrl)){

            storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(categoryImageUrl);
            storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {

                    Toast.makeText(context, "Category Deleted", Toast.LENGTH_SHORT).show();

                }

            });
        }
        getSnapshot(position).getReference().delete();
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        holder.bind(getSnapshot(position), listener);
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView attributeText;
        public CategoryViewHolder(View itemView) {
            super(itemView);
            attributeText = itemView.findViewById(R.id.tv_attribute_text);
        }

        public void bind(@NonNull final DocumentSnapshot snapshot, final OnItemClickListener listener) {
            Category category = snapshot.toObject(Category.class);
            //productImageView.setImageBitmap(product.getImages().get());
            attributeText.setText(category.getCategoryName());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION && listener != null){
                        listener.onItemClick(getSnapshot(position), position);
                    }
                }
            });

        }

    }
}


