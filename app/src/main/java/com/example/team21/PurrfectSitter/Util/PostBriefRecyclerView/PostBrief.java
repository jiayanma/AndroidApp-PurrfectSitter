package com.example.team21.PurrfectSitter.Util.PostBriefRecyclerView;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.team21.PurrfectSitter.FirebaseClass.Post;

public class PostBrief implements Parcelable, Comparable<PostBrief> {
    private String postId;
    private final String coverPhoto;
    private final long timestamp;
    private final long requestStartTimestamp;
    private final long requestEndTimestamp;
    private final String location;
    private String pets;
    private final String TAG = "PostBrief init";

    public PostBrief(String postId, String coverPhoto, long timestamp, long requestStartTimestamp, long requestEndTimestamp, String location, String pets) {
        this.postId = postId;
        this.coverPhoto = coverPhoto;
        this.timestamp = timestamp;
        this.requestStartTimestamp = requestStartTimestamp;
        this.requestEndTimestamp = requestEndTimestamp;
        this.location = location;
        this.pets = pets;
    }

    public PostBrief(Post post) {
        this.postId = post.postId;
        this.coverPhoto = post.images.get(0);
        this.timestamp = post.timestamp;
        this.requestStartTimestamp = post.requestStartTimestamp;
        this.requestEndTimestamp = post.requestEndTimestamp;
        this.location = post.location.city + "," + post.location.state;
    }

    protected PostBrief(Parcel in) {
        this.postId = in.readString();
        this.coverPhoto = in.readString();
        this.timestamp = in.readLong();
        this.requestStartTimestamp = in.readLong();
        this.requestEndTimestamp = in.readLong();
        this.location = in.readString();
        this.pets = in.readString();
    }

    public String getPostId() {
        return postId;
    }

    public String getCoverPhoto() {return coverPhoto;}

    public long getTimestamp() {
        return timestamp;
    }

    public long getRequestStartTimestamp() {
        return requestStartTimestamp;
    }

    public long getRequestEndTimestamp() {
        return requestEndTimestamp;
    }

    public String getLocation() {
        return location;
    }

    public String getPets() {
        return pets;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public static final Creator<PostBrief> CREATOR = new Creator<PostBrief>() {
        @Override
        public PostBrief createFromParcel(Parcel in) {
            return new PostBrief(in);
        }

        @Override
        public PostBrief[] newArray(int size) {
            return new PostBrief[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(postId);
        parcel.writeString(coverPhoto);
        parcel.writeLong(requestStartTimestamp);
        parcel.writeLong(requestEndTimestamp);
        parcel.writeString(location);
        parcel.writeString(pets);
    }

    @Override
    public int compareTo(PostBrief postBrief) {
        return (int) (postBrief.timestamp - this.timestamp);
    }
}
