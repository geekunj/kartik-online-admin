package com.example.kartikonlinefirebase.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kartikonlinefirebase.R;
import com.example.kartikonlinefirebase.models.Catalogue;
import com.example.kartikonlinefirebase.models.Category;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class CatalogueAdapter extends FirestoreAdapter<CatalogueAdapter.CatalogueViewHolder> {

    private StorageReference catalogueImageStorageReference;
    private StorageReference productImageStorageReference;
    private CatalogueAdapter.OnItemClickListener listener;
    private Context context;
    private Query mQuery;
    private FirebaseFirestore mFirestore;

    public CatalogueAdapter(Query query, CatalogueAdapter.OnItemClickListener listener, Context context) {
        super(query);
        this.listener = listener;
        this.context = context;
        mFirestore = FirebaseFirestore.getInstance();
    }

    public interface OnItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    @NonNull
    @Override
    public CatalogueAdapter.CatalogueViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new CatalogueAdapter.CatalogueViewHolder(inflater.inflate(R.layout.catalogue_list_item, parent, false));
    }

    public void deleteCatalogue(int position){
        String catalogueImageUrl, productImageUrl;
        String catalogueId;
        catalogueImageUrl = getSnapshot(position).getString("catalogueImageUrl");
        catalogueId = getSnapshot(position).getId();
        productImageStorageReference = FirebaseStorage.getInstance().getReference(catalogueId).child("catalogues")
                .child(catalogueId).child("product-images");
        mFirestore.collection("products").whereEqualTo("catalogueId", catalogueId)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot document : queryDocumentSnapshots) {
                            Log.d("NIKUNJ", document.getId() + " => " + document.getData());
                            productImageStorageReference = FirebaseStorage.getInstance()
                                    .getReferenceFromUrl(document.getString("productImageUrl"));
                            catalogueImageStorageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    Toast.makeText(context, "product image deleted", Toast.LENGTH_SHORT).show();

                                }
                            });
                            document.getReference().delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(context, "product deleted", Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    }
                });

        if(catalogueImageUrl != null && !TextUtils.isEmpty(catalogueImageUrl)){

            catalogueImageStorageReference = FirebaseStorage.getInstance().getReferenceFromUrl(catalogueImageUrl);
            catalogueImageStorageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {

                    Toast.makeText(context, "Catalogue Deleted", Toast.LENGTH_SHORT).show();

                }
            });
        }
        getSnapshot(position).getReference().delete();
    }

    @Override
    public void onBindViewHolder(@NonNull CatalogueAdapter.CatalogueViewHolder holder, int position) {
        holder.bind(getSnapshot(position), listener);
    }

    class CatalogueViewHolder extends RecyclerView.ViewHolder {
        TextView catalogueName;
        TextView catalogueType;
        TextView visitorsText;
        TextView prodCountText;
        ImageView prodImage1,prodImage2,prodImage3,prodImage4;
        ImageView shareImage;
        Switch publishSwitch;

        public CatalogueViewHolder(View itemView) {
            super(itemView);
            catalogueName = itemView.findViewById(R.id.tv_catalog_title);
            catalogueType = itemView.findViewById(R.id.tv_catalog_type);
            visitorsText = itemView.findViewById(R.id.tv_visitors);
            prodCountText = itemView.findViewById(R.id.tv_product_counter);
            prodImage1 = itemView.findViewById(R.id.iv_prod_img1);
            prodImage2 = itemView.findViewById(R.id.iv_prod_img2);
            prodImage3 = itemView.findViewById(R.id.iv_prod_img3);
            prodImage4 = itemView.findViewById(R.id.iv_prod_img4);
            shareImage = itemView.findViewById(R.id.iv_share);
            publishSwitch = itemView.findViewById(R.id.switch_publish);

        }

        public void bind(@NonNull final DocumentSnapshot snapshot, final CatalogueAdapter.OnItemClickListener listener) {
            Catalogue catalogue = snapshot.toObject(Catalogue.class);

            //Log.v("NIKUNJ RECYCLER", catalogue.getCatalogueTitle().toString());
            //productImageView.setImageBitmap(product.getImages().get());
            catalogueName.setText(catalogue.getCatalogueTitle());
            catalogueType.setText(catalogue.getCatalogueType());
            visitorsText.setText(catalogue.getVisitors());
            publishSwitch.setChecked(catalogue.getPublished());
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
