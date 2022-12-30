package com.example.team21.PurrfectSitter.Util;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.net.URLEncoder;

public class Helper {
    public static void loadImageViewFromURL(Context context, String url, ImageView imageView) {
        String[] imagePath = url.split("\\?")[0].split("/o/");
        String encodedPath = imagePath[0] + "/o/" + URLEncoder.encode(imagePath[1]);
        StorageReference sr = FirebaseStorage.getInstance().getReferenceFromUrl(encodedPath);
        Glide.with(context).load(sr).into(imageView);
    }
}
