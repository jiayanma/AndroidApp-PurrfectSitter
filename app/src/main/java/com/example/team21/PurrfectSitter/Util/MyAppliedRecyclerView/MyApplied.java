package com.example.team21.PurrfectSitter.Util.MyAppliedRecyclerView;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.team21.PurrfectSitter.FirebaseClass.Apply;

public class MyApplied implements Parcelable, Comparable<MyApplied>{
    private String postId;
    private String title;
    private Long requestStartTimestamp;
    private Long requestEndTimestamp;
    private Long timestamp;
    private String host;
    private String status;

    public MyApplied(String postId, String title, Long requestStartTimestamp, Long requestEndTimestamp, Long timestamp, String host, String status) {
        this.postId = postId;
        this.title = title;
        this.requestStartTimestamp = requestStartTimestamp;
        this.requestEndTimestamp = requestEndTimestamp;
        this.timestamp = timestamp;
        this.host = host;
        this.status = status;
    }

    public MyApplied(Apply apply) {
        this.postId = apply.postId;
        this.title = apply.postTitle;
        this.timestamp = apply.timeStamp;
    }

    protected MyApplied(Parcel in) {
        postId = in.readString();
        title = in.readString();
        if (in.readByte() == 0) {
            requestStartTimestamp = null;
        } else {
            requestStartTimestamp = in.readLong();
        }
        if (in.readByte() == 0) {
            requestEndTimestamp = null;
        } else {
            requestEndTimestamp = in.readLong();
        }
        if (in.readByte() == 0) {
            timestamp = null;
        } else {
            timestamp = in.readLong();
        }
        host = in.readString();
        status = in.readString();
    }

    public static final Creator<MyApplied> CREATOR = new Creator<MyApplied>() {
        @Override
        public MyApplied createFromParcel(Parcel in) {
            return new MyApplied(in);
        }

        @Override
        public MyApplied[] newArray(int size) {
            return new MyApplied[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(postId);
        parcel.writeString(title);
        if (requestStartTimestamp == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeLong(requestStartTimestamp);
        }
        if (requestEndTimestamp == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeLong(requestEndTimestamp);
        }
        if (timestamp == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeLong(timestamp);
        }
        parcel.writeString(host);
        parcel.writeString(status);
    }

    public String getPostId() {
        return postId;
    }

    public String getTitle() {
        return title;
    }

    public Long getRequestStartTimestamp() {
        return requestStartTimestamp;
    }

    public Long getRequestEndTimestamp() {
        return requestEndTimestamp;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public String getHost() {
        return host;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public int compareTo(MyApplied myApplied) {
        return (int) (myApplied.timestamp-this.timestamp);
    }
}
