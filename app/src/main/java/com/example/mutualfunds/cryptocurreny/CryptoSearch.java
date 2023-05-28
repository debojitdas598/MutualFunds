package com.example.mutualfunds.cryptocurreny;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.example.mutualfunds.R;

import java.util.ArrayList;

public class CryptoSearch extends AppCompatActivity {

    AutoCompleteTextView autoCompleteTextView;
    Button search;
    ArrayList<String> listCoinName;
    ArrayList<String> listCoinCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crypto_search);

        search = findViewById(R.id.searchButton);
        AutocompleteCrypto autocompleteCrypto = new AutocompleteCrypto(this);

        listCoinName = new ArrayList<String>();
        listCoinName = autocompleteCrypto.coinName();

        listCoinCode = new ArrayList<String>();
        listCoinCode = autocompleteCrypto.coinCode();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, listCoinName);
        autoCompleteTextView = findViewById(R.id.autoCompleteTextView);
        autoCompleteTextView.setAdapter(adapter);
    }
}