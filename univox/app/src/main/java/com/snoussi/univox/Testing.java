package com.snoussi.univox;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.RelativeLayout;

public class Testing extends AppCompatActivity {
    private RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing);

        relativeLayout = findViewById(R.id.parentlayout);
        relativeLayout.setAddStatesFromChildren(true);
    }
}