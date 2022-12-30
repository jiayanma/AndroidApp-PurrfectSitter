package com.example.team21.Sticker;

public class Message implements Comparable<Message> {
    public String senderUsername;
    public long timestamp;
    public String stickerName;
    public int version;

    public Message() {
        this.version = FireBaseKeys.VERSION;
    }

    public Message(String senderUsername, long timestamp, String stickerName) {
        this.senderUsername = senderUsername;
        this.timestamp = timestamp;
        this.stickerName = stickerName;
        this.version = FireBaseKeys.VERSION;
    }

    @Override
    public int compareTo(Message message) {
        return (int) (message.timestamp - this.timestamp);
    }
}
