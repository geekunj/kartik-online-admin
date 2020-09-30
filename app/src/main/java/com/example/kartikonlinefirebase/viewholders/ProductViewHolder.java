package com.example.kartikonlinefirebase.viewholders;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.kartikonlinefirebase.R;

public class ProductViewHolder extends RecyclerView.ViewHolder {
    public ImageView prodImage;
    public TextView prodTitleText;
    public TextView prodPriceText;
    public TextView prodDiscPriceText;
    public TextView prodDescText;
    public TextView prodSizeText;
    public TextView prodGenderText;
    public TextView prodSoleNameText;
    public TextView prodColorText;
    public ProgressBar imageUploadProgress;

    //CircleImageView messengerImageView;

    public ProductViewHolder(View v) {
        super(v);
        prodImage = (ImageView) itemView.findViewById(R.id.iv_prod_image);
        prodTitleText = (TextView) itemView.findViewById(R.id.tv_prod_title);
        prodPriceText = (TextView) itemView.findViewById(R.id.tv_prod_price);
        prodDiscPriceText = (TextView) itemView.findViewById(R.id.tv_prod_disc_price);
        prodDescText = (TextView) itemView.findViewById(R.id.tv_prod_desc);
        prodSizeText = (TextView) itemView.findViewById(R.id.tv_prod_size);
        prodGenderText = (TextView) itemView.findViewById(R.id.tv_prod_gender);
        prodSoleNameText = (TextView) itemView.findViewById(R.id.tv_prod_sole_name);
        prodColorText = (TextView) itemView.findViewById(R.id.tv_prod_color);
        imageUploadProgress = (ProgressBar) itemView.findViewById(R.id.pb_image_upload);

        //messengerImageView = (CircleImageView) itemView.findViewById(R.id.messengerImageView);
    }
}
