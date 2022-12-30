package com.example.team21.Sticker;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.team21.R;

public class ReceivedStickerViewHolder extends RecyclerView.ViewHolder {
    public TextView sender_username;
    public ImageView sticker;
    public TextView timestamp;

    public ReceivedStickerViewHolder(@NonNull View itemView) {
        super(itemView);
        this.sender_username = itemView.findViewById(R.id.textView_sender);
        this.sticker = itemView.findViewById(R.id.imageView_sticker);
        this.timestamp = itemView.findViewById(R.id.textView_timestamp);
    }
}
