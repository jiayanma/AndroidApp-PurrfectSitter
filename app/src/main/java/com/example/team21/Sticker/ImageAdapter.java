package com.example.team21.Sticker;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.team21.R;

public class ImageAdapter extends BaseAdapter {

    private Context context;

//    public int[] imageArray = {
//            R.drawable.sticker_books,
//            R.drawable.sticker_love,
//            R.drawable.sticker_mad,
//            R.drawable.sticker_music,
//            R.drawable.sticker_pikachu,
//            R.drawable.sticker_pirate,
//            R.drawable.sticker_proud,
//            R.drawable.sticker_smile,
//            R.drawable.sticker_sunglasses,
//            R.drawable.sticker_tongue,
//            R.drawable.sticker_tree
//    };
    public int[] imageArray;

    public ImageAdapter(Context context) {
        this.context = context;
        int stickerSize = FireBaseKeys.stickerNames.size();
        imageArray = new int[stickerSize];
        for (int i = 0; i < stickerSize; i++) {
            int resourceId = context.getResources().getIdentifier("sticker_" + FireBaseKeys.stickerNames.get(i), "drawable", context.getPackageName());
            imageArray[i] = resourceId;
        }
    }

    @Override
    public int getCount() {
        return imageArray.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return (long) imageArray[i];
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ImageView imageview = new ImageView(context);
        imageview.setImageResource(imageArray[i]);
        imageview.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageview.setLayoutParams(new ViewGroup.LayoutParams(150,150));
        return imageview;
    }
}
