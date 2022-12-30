package com.example.team21.PurrfectSitter.FirebaseClass;

import com.example.team21.Sticker.Message;

public class Apply{
    public String applyId;
    public String postId;
    public String postTitle;
    public String applyCreatorId;
    public String applicantId;
    public String applicantName;
    public String applicantEmail;
    public String message;
    public Long timeStamp;
    public String status;
    public String avatar;

    public Apply() {
    }

    public Apply(String postId, String postTitle, String applyCreatorId, String applicantId, String applicantName, String applicantEmail, String message, String status, String avatar) {
        this.applyId = "placeholder";
        this.postId = postId;
        this.applicantName = applicantName;
        this.applicantEmail = applicantEmail;
        this.message = message;
        this.postTitle = postTitle;
        this.applyCreatorId = applyCreatorId;
        this.applicantId = applicantId;
        this.timeStamp = System.currentTimeMillis();
        this.status = status;
        this.avatar = avatar;
    }

}
