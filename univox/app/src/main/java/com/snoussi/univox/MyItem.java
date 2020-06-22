package com.snoussi.univox;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class MyItem implements ClusterItem {
    private  LatLng mPosition;
    private  String mTitle;
    private  String mSnippet;


    public MyItem(double lat, double lng) {
        mPosition = new LatLng(lat,lng);
    }

    public MyItem(double lat, double lng,String mTitle, String mSnippet) {
        mPosition = new LatLng(lat,lng);
        this.mSnippet = mSnippet;
        this.mTitle = mTitle;
    }



    public LatLng getPosition() {
        return mPosition;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getSnippet() {
        return mSnippet;
    }

}
