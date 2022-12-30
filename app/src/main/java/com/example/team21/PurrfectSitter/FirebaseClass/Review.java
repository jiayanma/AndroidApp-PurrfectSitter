package com.example.team21.PurrfectSitter.FirebaseClass;

public class Review {
    public String postId;
    public String reviewerId;
    public String revieweeId;
    public int role; // 1 is host, 2 is sitter
    public String reviewText;
    public long timeStamp;

    public Review() {};

    public Review (String postId, String reviewerId, String revieweeId,
                    int role, String reviewText, long timeStamp) {
        this.postId = postId;
        this.reviewerId = reviewerId;
        this.revieweeId = revieweeId;
        this.role = role;
        this.reviewText = reviewText;
        this.timeStamp = System.currentTimeMillis();
    }
}
