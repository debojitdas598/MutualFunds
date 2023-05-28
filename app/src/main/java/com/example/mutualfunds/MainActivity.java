package com.example.mutualfunds;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.example.mutualfunds.cryptocurreny.CryptoSearch;
import com.example.mutualfunds.mf.MutualFundsSearch;
import com.example.mutualfunds.stocks.StocksSearch;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Button mutualfunds,crypto,stocks;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mutualfunds = findViewById(R.id.mutualfunds);
        crypto = findViewById(R.id.cryptocurrency);
        stocks = findViewById(R.id.stocks);

        mutualfunds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MutualFundsSearch.class);
                startActivity(intent);
            }
        });
        crypto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CryptoSearch.class);
                startActivity(intent);
            }
        });
        stocks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), StocksSearch.class);
                startActivity(intent);
            }
        });

    }



}