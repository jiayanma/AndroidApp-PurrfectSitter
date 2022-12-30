package com.example.team21.Sticker;

import com.example.team21.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UserData {
    public HashMap<String, Message> received = new HashMap<>();
    public List<StickerCounter> sent = new ArrayList<>();

    public UserData() {
        received = new HashMap<>();
        received.put("placeholder", new Message());
        sent = new ArrayList<>();
        for(String stickerName : FireBaseKeys.stickerNames) {
            sent.add(new StickerCounter(stickerName, 0));
        }
    }
}
