package com.example.mutualfunds.stocks;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.example.mutualfunds.R;
import com.example.mutualfunds.cryptocurreny.AutocompleteCrypto;

import java.util.ArrayList;

public class StocksSearch extends AppCompatActivity {


    AutoCompleteTextView autoCompleteTextView;
    Button search;
    ArrayList<String> listStockName;
    ArrayList<String> listStocksIdentifier;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stocks_search);

        search = findViewById(R.id.search);
        AutoCompleteStocks autoCompleteStocks = new AutoCompleteStocks(this);

        listStockName = new ArrayList<String>();
        listStockName = autoCompleteStocks.stockName();
        listStocksIdentifier = new ArrayList<String>();
        listStocksIdentifier = autoCompleteStocks.stockIdentifier();



        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, listStockName);
        autoCompleteTextView = findViewById(R.id.stockssearch);
        autoCompleteTextView.setAdapter(adapter);

        if(!autoCompleteTextView.getText().toString().isEmpty()){
            search.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listStockName.contains(autoCompleteTextView.getText().toString().toUpperCase())){

                    }
                }
            });
        }
    }
}