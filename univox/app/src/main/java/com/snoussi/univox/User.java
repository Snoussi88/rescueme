package com.snoussi.univox;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String key;
    private String nickname;
    private String name;
    private String mail;
    private int reputation;
    private List<String> postsLiked;
    private Double lat;
    private Double longt;
    private ArrayList<String> followers;
    private ArrayList<String> following;
    private int scrollPosition;

    public User(String key, String name, String mail, int reputation,List<String> postsLiked,Double lat,Double longt) {
        this.key = key;
        this.name = name;
        this.mail = mail;
        this.reputation = reputation;
        this.postsLiked = postsLiked;
        this.lat = lat;
        this.longt = longt;
    }


    public User() {
    }

    public User(String key, String name, String mail, int reputation,List<String> postsLiked,
                Double lat,Double longt, ArrayList<String> followers,ArrayList<String> following) {

        this.key = key;
        this.name = name;
        this.mail = mail;
        this.reputation = reputation;
        this.postsLiked = postsLiked;
        this.lat = lat;
        this.longt = longt;
        this.followers = followers;
        this.following = following;
    }

    public int getScrollPosition() {
        return scrollPosition;
    }

    public void setScrollPosition(int scrollPosition) {
        this.scrollPosition = scrollPosition;
    }


//getters and setters

    public ArrayList<String> getFollowers() {
        return followers;
    }

    public void setFollowers(ArrayList<String> followers) {
        this.followers = followers;
    }

    public ArrayList<String> getFollowing() {
        return following;
    }

    public void setFollowing(ArrayList<String> following) {
        this.following = following;
    }

    public String getKey() {
        return key;
    }

    public int getReputation() {
        return reputation;
    }

    public void setReputation(int reputation) {
        this.reputation = reputation;
    }

    public List<String> getPostsLiked() {
        return postsLiked;
    }

    public void setPostsLiked(List<String> postsLiked) {
        this.postsLiked = postsLiked;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLongt() {
        return longt;
    }

    public void setLongt(Double longt) {
        this.longt = longt;
    }
}
