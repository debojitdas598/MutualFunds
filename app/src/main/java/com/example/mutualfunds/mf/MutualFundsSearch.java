package com.example.mutualfunds.mf;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.example.mutualfunds.Item;
import com.example.mutualfunds.ItemAdapter;
import com.example.mutualfunds.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MutualFundsSearch extends AppCompatActivity {

    AutoCompleteTextView autoCompleteTextView;
    Button search;
    ArrayList<String> listSchemeName, listSchemeCode;
    RecyclerView recyclerView;
    FirebaseAuth auth;
    CardView cv;
    TextView lastchecked;
    FrameLayout loading;
    FirebaseUser user;
    List<Item> items;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://financemonitor-626d9-default-rtdb.firebaseio.com/");
    private ItemAdapter itemAdapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mutual_funds_search);
        int layout = android.R.layout.simple_list_item_1;


        initializer();
        lastcheckedhistory();
        onSearch();
        listSchemeName = new ArrayList<String>();
        listSchemeName = schemeName();
        listSchemeCode = new ArrayList<String>();
        listSchemeCode = schemeCode();
        loading.setVisibility(View.VISIBLE);



//        AutoCompleteMutualFunds autoCompleteMutualFunds = new AutoCompleteMutualFunds(this);



        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, listSchemeName);
        autoCompleteTextView = findViewById(R.id.autoCompleteTextView);
        autoCompleteTextView.setAdapter(adapter);
        autoCompleteTextView.clearFocus();


    }

    private void bookmarkDisplay() {
        items = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setVisibility(View.GONE);
        databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String mffavourite = snapshot.child(user.getUid()).child("mffavourite").getValue(String.class).toString();
                String temp = "";
                for (int i = 0; i < mffavourite.length(); i++) {
                    if(mffavourite.charAt(i)!=';'){
                        temp = temp + mffavourite.charAt(i);
                    }
                    else {
                        items.add(new Item(temp));
                        temp = "";
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        itemAdapter = new ItemAdapter(items);
        recyclerView.setAdapter(itemAdapter);
        recyclerView.setVisibility(View.VISIBLE);

    }


    private void onSearch() {

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        String userId = user.getUid().toString();
        search = findViewById(R.id.searchButton);
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

                    databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.hasChild(user.getUid())){

                                databaseReference.child("users")
                                        .child(userId)
                                        .child("mfhistory")
                                        .setValue(autoCompleteTextView.getText().toString().trim());

                                lastchecked.setText(autoCompleteTextView.getText().toString().trim());
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });



                    Intent intent = new Intent(MutualFundsSearch.this, DetailsMF.class);
                    intent.putExtra("schemeCode",schemeCode.toString());
                    intent.putExtra("schemeName",autoCompleteTextView.getText().toString());
                    startActivity(intent);
                }
            }
        });
    }

    private void lastcheckedhistory() {

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        String userId = user.getUid().toString();

        databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild(user.getUid())){
                    String history = snapshot.child(userId).child("mfhistory").getValue(String.class).toString();
                    cv.setVisibility(View.VISIBLE);
                    lastchecked.setText(history);
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initializer() {
        loading = findViewById(R.id.loadLayout);
        lastchecked = findViewById(R.id.history);
        cv = findViewById(R.id.cv);
        recyclerView = findViewById(R.id.recyclerView);
    }

    public ArrayList<String> schemeName(){
        ArrayList<String> listSchemeName = new ArrayList<>();
        String url = "https://api.mfapi.in/mf";
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        loading.setVisibility(View.GONE);
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                String schemeName = jsonObject.getString("schemeName").toString();
                                listSchemeName.add(schemeName);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

        queue.add(jsonArrayRequest);
        return listSchemeName;

    }

    public ArrayList<String> schemeCode(){
        ArrayList<String> listSchemeCode = new ArrayList<>();
        String url = "https://api.mfapi.in/mf";
        RequestQueue queue = Volley.newRequestQueue(this);


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                String schemeCode = jsonObject.getString("schemeCode").toString();
                                listSchemeCode.add(schemeCode);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

        queue.add(jsonArrayRequest);
        return listSchemeCode;

    }

    @Override
    protected void onResume() {
        super.onResume();

        bookmarkDisplay();
    }
}