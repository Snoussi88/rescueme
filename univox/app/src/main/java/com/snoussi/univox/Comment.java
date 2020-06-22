package com.snoussi.univox;

public class Comment {
    private String comment;
    private String username;
    private String key;

    public Comment() {
    }


    public Comment(String comment, String username,String key) {
        this.comment = comment;
        this.username = username;
        this.key = key;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}



