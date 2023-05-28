package com.example.mutualfunds.mf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.mutualfunds.R;
import com.example.mutualfunds.authentication.Login;
import com.example.mutualfunds.authentication.UserAccount;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
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

public class DetailsMF extends AppCompatActivity {
    FirebaseAuth auth;
    ImageView backbutton, useracc;
    TextView email, scheme;
    ArrayList<String> dates;
    ArrayList<String> navs;
    LineChart lineChart;
    TextView  schemeType, schemeCategory,returnpercent,changepercent,date,nav;
    FrameLayout loading;
    FirebaseUser user;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://financemonitor-626d9-default-rtdb.firebaseio.com/");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_details_mf);

        initializer();
        onPress();

        Intent intent  = getIntent();
        String schemeName = intent.getStringExtra("schemeName");
        String schemeCode = intent.getStringExtra("schemeCode");
        scheme.setText(schemeName);

        apiCall(schemeName,schemeCode);


    }

    private void graph(String type, ArrayList<String> navssent , ArrayList<String> datessent) {
        int days = 30;
        if(type == "1M")
            days = 30;
        else if (type == "6M") {
            days = 180;
        } else if (type == "1Y") {
            days = 365;
        } else if (type =="3Y") {
            days = 365*3;
        } else if (type == "5Y") {
            days = 365*5;
        }
        else{
            days = dates.size();
        }

        ArrayList<Entry> entries = new ArrayList<>();
        float x = 0;
        for (int i=days-1;i > -1;i--){
            float y = Float.parseFloat(navs.get(i));
            entries.add(new Entry(x,y));
            x++;

        }
        LineDataSet lineDataSet = new LineDataSet(entries,"NAV");
        lineDataSet.setColor(Color.BLACK);
        LineData lineData = new LineData();
        lineData.addDataSet(lineDataSet);
        lineChart.setData(lineData);
        lineChart.setTouchEnabled(true);
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);
        lineChart.animateX(500);


        lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        lineDataSet.setCubicIntensity(0.2f);
        lineDataSet.setDrawCircles(false);
        lineDataSet.setDrawValues(false);
        lineDataSet.setLineWidth(3.5f);


        XAxis xAxis = lineChart.getXAxis();
        xAxis.setEnabled(false);
        xAxis.setDrawGridLines(false);
        YAxis yAxisLeft = lineChart.getAxisLeft();
        yAxisLeft.setEnabled(false);
        yAxisLeft.setDrawGridLines(false);
        YAxis yAxisRight = lineChart.getAxisRight();
        yAxisRight.setDrawGridLines(false);


    }


    private void apiCall(String schemeName, String schemeCode) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = "https://api.mfapi.in/mf/"+schemeCode;
        dates = new ArrayList<>();
        navs = new ArrayList<>();
        loading.setVisibility(View.VISIBLE);

       JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
           @Override
           public void onResponse(JSONObject response) {
               try {
                   JSONObject meta = response.getJSONObject("meta");
                   schemeType.setText(meta.getString("scheme_type"));
                   schemeCategory.setText(meta.getString("scheme_category"));
                   JSONArray datejsonArray = response.getJSONArray("data");
                   for (int i = 0; i <datejsonArray.length() ; i++) {
                       JSONObject jsonObject = datejsonArray.getJSONObject(i);
                       dates.add(jsonObject.getString("date"));
                       navs.add(jsonObject.getString("nav"));
                   }

                   graph("1M",navs,dates);
                   loading.setVisibility(View.GONE);

               } catch (JSONException e) {
                   throw new RuntimeException(e);
               }

           }
       }, new Response.ErrorListener() {
           @Override
           public void onErrorResponse(VolleyError error) {

           }
       });

        requestQueue.add(jsonObjectRequest);
    }


    private void initializer() {
        auth = FirebaseAuth.getInstance();
        lineChart = findViewById(R.id.lineChart);
        useracc = findViewById(R.id.useracc);
        backbutton = findViewById(R.id.backbutton);
        loading = findViewById(R.id.loadLayout);
        scheme = findViewById(R.id.name);
        schemeType = findViewById(R.id.schemetype);
        schemeCategory = findViewById(R.id.schemecategory);
        returnpercent = findViewById(R.id.returnpercent);
        changepercent = findViewById(R.id.changepercent);
        date = findViewById(R.id.date);
        nav = findViewById(R.id.nav);



    }

    private void onPress() {

        useracc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UserAccount.class);
                startActivity(intent);
            }
        });

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MutualFundsSearch.class);
                startActivity(intent);
            }
        });

    }


}