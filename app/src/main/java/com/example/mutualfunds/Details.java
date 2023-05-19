package com.example.mutualfunds;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

public class Details extends AppCompatActivity {
    String schemeCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        schemeCode = getIntent().getStringExtra("schemeCode").toString();

    }
}