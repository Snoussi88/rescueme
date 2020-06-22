package com.snoussi.univox;


import java.util.ArrayList;
import java.util.List;

public class Post {
    private String txt, author,key;
    private Integer likes, numComms;
    private Double lat,longt;
    private String type;
    private String keyAuthor;
    private List<String> helpRequestSent;


    public Post() {
    }

    public Post(String txt, String author, Integer likes, Integer numComms, Double lat, Double longt, String type, String keyAuthor, List<String> helpRequestSent, String key) {
        this.txt = txt;
        this.author = author;
        this.likes = likes;
        this.numComms = numComms;
        this.lat = lat;
        this.longt = longt;
        this.type = type;
        this.keyAuthor = keyAuthor;
        this.helpRequestSent = helpRequestSent;
        this.key = key;
    }



    public Post(String txt, String author, Integer likes, Integer numComms, Double lat, Double longt, String type, String keyAuthor, List<String> helpRequestSent) {
        this.txt = txt;
        this.author = author;
        this.likes = likes;
        this.numComms = numComms;
        this.lat = lat;
        this.longt = longt;
        this.type = type;
        this.keyAuthor = keyAuthor;
        this.helpRequestSent = helpRequestSent;
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

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTxt() {
        return txt;
    }

    public void setTxt(String txt) {
        this.txt = txt;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public Integer getNumComms() {
        return numComms;
    }

    public void setNumComms(Integer numComms) {
        this.numComms = numComms;
    }


    public List<String> getHelpRequestSent() {
        return helpRequestSent;
    }

    public void setHelpRequestSent(List<String> helpRequestSent) {
        this.helpRequestSent = helpRequestSent;
    }

    public String getKeyAuthor() {
        return keyAuthor;
    }

    public void setKeyAuthor(String keyAuthor) {
        this.keyAuthor = keyAuthor;
    }
}
