package com.example.team21.Sticker;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class ReceivedSticker implements Parcelable {
    private final String sender_username;
    private final Long timestamp;
    private final String sticker_name;
    private final int version;

    protected ReceivedSticker(Parcel in, String sender_username, Long timestamp, String sticker_name, int version) {
        this.sender_username = sender_username;
        this.timestamp = timestamp;
        this.sticker_name = sticker_name;
        this.version = version;
    }

    public ReceivedSticker(Message message) {
        this.sender_username = message.senderUsername;
        this.timestamp = message.timestamp;
        this.sticker_name = message.stickerName;
        this.version = message.version;
    }

    public ReceivedSticker(Parcel in) {
        sender_username = in.readString();
        timestamp = in.readLong();
        sticker_name = in.readString();
        version = in.readInt();
    }

    public static final Creator<ReceivedSticker> CREATOR = new Creator<ReceivedSticker>() {
        @Override
        public ReceivedSticker createFromParcel(Parcel in) {
            return new ReceivedSticker(in);
        }

        @Override
        public ReceivedSticker[] newArray(int size) {
            return new ReceivedSticker[size];
        }
    };



    public String getSenderUsername(){
        return sender_username;
    }

    public Long getTimestamp(){
        return timestamp;
    }

    public String getStickerName() {
        return sticker_name;

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(sender_username);
        parcel.writeLong(timestamp);
        parcel.writeString(sticker_name);
    }
}
