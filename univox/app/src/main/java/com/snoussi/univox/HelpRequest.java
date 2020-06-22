package com.snoussi.univox;

import java.util.ArrayList;

public class HelpRequest {
    private String from;
    private String to;
    private double latUser,longtUser,latFrom,longtFrom;


    public HelpRequest() {
    }

    public HelpRequest(String from, String to) {
        this.from = from;
        this.to = to;
    }

    public HelpRequest(String from, String to, double latUser, double longtUser, double latFrom, double longtFrom) {
        this.from = from;
        this.to = to;
        this.latUser = latUser;
        this.longtUser = longtUser;
        this.latFrom = latFrom;
        this.longtFrom = longtFrom;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }


    public double getLatUser() {
        return latUser;
    }

    public void setLatUser(double latUser) {
        this.latUser = latUser;
    }

    public double getLongtUser() {
        return longtUser;
    }

    public void setLongtUser(double longtUser) {
        this.longtUser = longtUser;
    }

    public double getLatFrom() {
        return latFrom;
    }

    public void setLatFrom(double latFrom) {
        this.latFrom = latFrom;
    }

    public double getLongtFrom() {
        return longtFrom;
    }

    public void setLongtFrom(double longtFrom) {
        this.longtFrom = longtFrom;
    }
}
