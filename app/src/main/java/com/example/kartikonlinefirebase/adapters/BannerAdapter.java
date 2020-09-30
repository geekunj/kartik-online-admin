package com.example.kartikonlinefirebase.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.kartikonlinefirebase.R;
import com.example.kartikonlinefirebase.models.Banner;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class BannerAdapter extends FirestoreAdapter<BannerAdapter.BannerViewHolder> {

    private StorageReference storageReference;
    private OnItemClickListener listener;
    private Context context;

    public BannerAdapter(Query query, OnItemClickListener listener, Context context) {
        super(query);
        this.listener = listener;
        this.context = context;
    }

    public interface OnItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    @NonNull
    @Override
    public BannerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new BannerViewHolder(inflater.inflate(R.layout.banner_image_item, parent, false));
    }

    public void deleteBanner(int position){
        String bannerImageUrl;
        bannerImageUrl = getSnapshot(position).getString("bannerImageUrl");
        if(bannerImageUrl != null && !TextUtils.isEmpty(bannerImageUrl)){

            storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(bannerImageUrl);
            storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {

                    Toast.makeText(context, "Banner Deleted", Toast.LENGTH_SHORT).show();

                }

            });
        }
        getSnapshot(position).getReference().delete();
    }

    @Override
    public void onBindViewHolder(@NonNull BannerViewHolder holder, int position) {
        holder.bind(getSnapshot(position), listener);
    }

    class BannerViewHolder extends RecyclerView.ViewHolder {
        ImageView bannerImage;
        ImageView deleteBannerImage;

        public BannerViewHolder(View itemView) {
            super(itemView);
            bannerImage = itemView.findViewById(R.id.iv_banner_item);
            deleteBannerImage = itemView.findViewById(R.id.iv_delete_banner);
        }

        public void bind(@NonNull final DocumentSnapshot snapshot, final OnItemClickListener listener) {
            Banner banner = snapshot.toObject(Banner.class);
            //productImageView.setImageBitmap(product.getImages().get());
            Glide.with(context)
                    .load(banner.getBannerImageUrl())
                    .into(bannerImage);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION && listener != null){
                        listener.onItemClick(getSnapshot(position), position);

                        if(v == deleteBannerImage){
                            deleteBanner(position);
                        }
                    }
                }
            });

        }

    }

}
