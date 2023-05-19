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

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    AutoCompleteTextView autoCompleteTextView;
    Button search;
    ArrayList<String> listSchemeName;
    ArrayList<String> listSchemeCode;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        int layout = android.R.layout.simple_list_item_1;

        search = findViewById(R.id.searchButton);
        AutoComplete autoComplete = new AutoComplete(this);

        listSchemeName = new ArrayList<String>();
        listSchemeName = autoComplete.schemeName();

        listSchemeCode = new ArrayList<String>();
        listSchemeCode = autoComplete.schemeCode();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, listSchemeName);
        autoCompleteTextView = findViewById(R.id.autoCompleteTextView);
        autoCompleteTextView.setAdapter(adapter);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(autoCompleteTextView.getText().toString()==""){
                    Toast.makeText(getApplicationContext(),"Enter a MF name",Toast.LENGTH_SHORT).show();
                }
                else if(listSchemeName.indexOf(autoCompleteTextView.getText().toString())==-1){
                    Toast.makeText(getApplicationContext(),"Enter a MF name",Toast.LENGTH_SHORT).show();
                }
                else{
                    int index =  listSchemeName.indexOf(autoCompleteTextView.getText().toString().trim());
                    String schemeCode = listSchemeCode.get(index);
                    Toast.makeText(getApplicationContext(),schemeCode,Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this,Details.class);
                    intent.putExtra("schemeCode",schemeCode);
                    startActivity(intent);
                }
            }
        });
    }



}