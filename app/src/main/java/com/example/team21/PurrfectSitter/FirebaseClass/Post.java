package com.example.team21.PurrfectSitter.FirebaseClass;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Post implements Parcelable, Comparable<Post>{
    public String postId;
    public String creatorId;
    public String title;
    public long timestamp;
    public String description;
    public long requestStartTimestamp;
    public long requestEndTimestamp;
    public Location location;
    public List<Double> locationCoord;
    public List<String> images;
    public boolean applied;
    public boolean reviewedByOwner;
    public boolean reviewedBySitter;
    public String sitter;

    public Post() {

    }

    public Post(String creatorId, String title, String description, long requestStartTimestamp, long requestEndTimestamp,
                Location location, List<Double> locationCoord, List<String> images) {
        this.postId = "placeholder";
        this.title = title;
        this.creatorId = creatorId;
        this.timestamp = System.currentTimeMillis();
        this.description = description;
        this.requestStartTimestamp = requestStartTimestamp;
        this.requestEndTimestamp = requestEndTimestamp;
        this.location = location;
        this.locationCoord = locationCoord;
        this.images = images;
        this.applied = false;
        this.reviewedByOwner = false;
        this.reviewedBySitter = false;
        this.sitter = Config.SITTER_PLACEHOLDER;
    }

    protected Post(Parcel in) {
        postId = in.readString();
        creatorId = in.readString();
        title = in.readString();
        timestamp = in.readLong();
        description = in.readString();
        requestStartTimestamp = in.readLong();
        requestEndTimestamp = in.readLong();
        images = in.createStringArrayList();
        applied = in.readByte() != 0;
        reviewedByOwner = in.readByte() != 0;
        reviewedBySitter = in.readByte() != 0;;
        sitter = in.readString();
    }

    public static final Creator<Post> CREATOR = new Creator<Post>() {
        @Override
        public Post createFromParcel(Parcel in) {
            return new Post(in);
        }

        @Override
        public Post[] newArray(int size) {
            return new Post[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(postId);
        parcel.writeString(creatorId);
        parcel.writeString(title);
        parcel.writeLong(timestamp);
        parcel.writeString(description);
        parcel.writeLong(requestStartTimestamp);
        parcel.writeLong(requestEndTimestamp);
        parcel.writeStringList(images);
        parcel.writeByte((byte) (applied ? 1 : 0));
        parcel.writeByte((byte) (reviewedByOwner ? 1 : 0));
        parcel.writeByte((byte) (reviewedBySitter ? 1 : 0));
    }

    @Override
    public int compareTo(Post post) {
        return (int) (post.timestamp - this.timestamp);
    }
}
