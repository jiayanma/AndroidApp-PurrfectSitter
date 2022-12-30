package com.example.team21.Sticker;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.team21.R;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

public class ReceivedStickerAdapter extends RecyclerView.Adapter<ReceivedStickerViewHolder> {
    private List<ReceivedSticker> received_sticker;
    private Context context;
    Format format;
    private Hashtable<String, Integer> stickerMap;

    public ReceivedStickerAdapter(List<ReceivedSticker> received_sticker, Context context) {
        this.received_sticker = received_sticker;
        this.context = context;
        this.format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.stickerMap = new Hashtable<String, Integer>();
        for (String name : FireBaseKeys.stickerNames) {
            int resourceId = context.getResources().getIdentifier("sticker_" + name, "drawable", context.getPackageName());
            this.stickerMap.put(name, resourceId);
        }
//        this.stickerMap.put("books", R.drawable.sticker_books);
//        this.stickerMap.put("love", R.drawable.sticker_love);
//        this.stickerMap.put("mad", R.drawable.sticker_mad);
//        this.stickerMap.put("music", R.drawable.sticker_music);
//        this.stickerMap.put("pikachu", R.drawable.sticker_pikachu);
//        this.stickerMap.put("pirate", R.drawable.sticker_pirate);
//        this.stickerMap.put("proud", R.drawable.sticker_proud);
//        this.stickerMap.put("smile", R.drawable.sticker_smile);
//        this.stickerMap.put("sunglasses", R.drawable.sticker_sunglasses);
//        this.stickerMap.put("tongue", R.drawable.sticker_tongue);
//        this.stickerMap.put("tree", R.drawable.sticker_tree);
    }

    @NonNull
    @Override
    public ReceivedStickerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ReceivedStickerViewHolder(LayoutInflater.from(context).inflate(R.layout.received_sticker, null));
    }


    @Override
    public void onBindViewHolder(@NonNull ReceivedStickerViewHolder holder, int position) {
        holder.sender_username.setText("from: " + received_sticker.get(position).getSenderUsername());
        Date date = new Date(received_sticker.get(position).getTimestamp());
        holder.timestamp.setText(this.format.format(date));
        String sticker_name = received_sticker.get(position).getStickerName();
        if (sticker_name != null && this.stickerMap.containsKey(sticker_name)) {
            holder.sticker.setImageResource(this.stickerMap.get(sticker_name));
        } else {
            holder.sticker.setImageResource(R.drawable.question);
        }
    }

    @Override
    public int getItemCount() {
        return received_sticker.size();
    }
}
