package com.example.team21.PurrfectSitter.Util.ReceivedApplyRecyclerView;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.team21.PurrfectSitter.FirebaseClass.Apply;

public class ReceivedApply implements Parcelable, Comparable<ReceivedApply> {
    private final String post_title;
    private final String applicant_name;
    private final String applicant_email;
    private final String message;
    private final Long time;
    private final String status;
    private final String applicantId;
    private final String postId;
    private final String avatar;


    protected ReceivedApply(Parcel in, String post_title, String applicant_name, String applicant_email, String message, Long time, String status, String applicantId, String postId,String avatar) {
        this.post_title = post_title;
        this.applicant_name = applicant_name;
        this.applicant_email = applicant_email;
        this.message = message;
        this.time = time;
        this.status = status;
        this.applicantId = applicantId;
        this.postId = postId;
        this.avatar = avatar;
    }

    public ReceivedApply(Parcel in) {
        post_title = in.readString();
        applicant_name = in.readString();
        applicant_email = in.readString();
        message = in.readString();
        time = in.readLong();
        status = in.readString();
        applicantId = in.readString();
        postId = in.readString();
        avatar = in.readString();
    }

    public ReceivedApply(Apply apply) {
        this.post_title = apply.postTitle;
        this.applicant_name = apply.applicantName;
        this.applicant_email = apply.applicantEmail;
        this.message = apply.message;
        this.time = apply.timeStamp;
        this.status = apply.status;
        this.applicantId = apply.applicantId;
        this.postId = apply.postId;
        this.avatar = apply.avatar;
    }


    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(post_title);
        parcel.writeString(applicant_name);
        parcel.writeString(applicant_email);
        parcel.writeString(message);
        parcel.writeLong(time);
        parcel.writeString(status);
        parcel.writeString(avatar);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getPost_title() {
        return post_title;
    }

    public String getApplicant_name() {
        return applicant_name;
    }

    public String getApplicant_email() {
        return applicant_email;
    }

    public String getMessage() {
        return message;
    }

    public Long getTime() {
        return time;
    }

    public String getStatus() { return status; }

    public String getApplicantId() {
        return applicantId;
    }

    public String getPostId() {
        return postId;
    }

    public String getAvatar() {
        return avatar;
    }

    public static final Creator<ReceivedApply> CREATOR = new Creator<ReceivedApply>() {
        @Override
        public ReceivedApply createFromParcel(Parcel in) {
            return new ReceivedApply(in);
        }

        @Override
        public ReceivedApply[] newArray(int size) {
            return new ReceivedApply[size];
        }
    };

    @Override
    public int compareTo(ReceivedApply receivedApply) {
        return (int) (receivedApply.time-this.time);
    }
}
