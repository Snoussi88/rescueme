package com.snoussi.univox;

public class NotificationClassifier {
    private String content;
    private String type;
    private String postId;

    public NotificationClassifier(String content, String type) {
        this.content = content;
        this.type = type;
    }

    public NotificationClassifier(String content, String type, String postId) {
        this.content = content;
        this.type = type;
        this.postId = postId;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
