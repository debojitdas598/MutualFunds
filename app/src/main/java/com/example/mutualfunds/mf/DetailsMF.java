package com.example.mutualfunds.mf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.appsearch.AppSearchResult;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class DetailsMF extends AppCompatActivity {
    FirebaseAuth auth;
    ImageView backbutton, useracc, bookmark;
    TextView email, scheme;
    ArrayList<String> dates;
    ArrayList<String> navs;
    LineChart lineChart;
    TextView  schemeType, schemeCategory,returnpercent,changepercent,date,nav,detailstype;
    TextView onemonth,sixmonths,oneyear,threeyear,fiveyear,all;
    FrameLayout loading;
    FirebaseUser user;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://financemonitor-626d9-default-rtdb.firebaseio.com/");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_details_mf);

        initializer();
        onPress();
        bottomMenu();
        Intent intent  = getIntent();
        String schemeName = intent.getStringExtra("schemeName");
        String schemeCode = intent.getStringExtra("schemeCode");
        scheme.setText(schemeName);
        bookmarker(schemeName);
        apiCall(schemeName,schemeCode);
        detailstype.setText("1M return");


    }

    private void returnPercentCalculator(int days, ArrayList<String> navssent , ArrayList<String> datessent ) {

        int monthstoSubtract = days/30;
        double presentNAV = Math.round((Double.parseDouble(navssent.get(0).toString()))*100.0)/100.0;
        int indexOfInitial = 0;
        String presentDate = datessent.get(0);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate localDate = LocalDate.parse(presentDate, formatter);
        LocalDate initiallocaldate = localDate.minusMonths(monthstoSubtract);
        String initialdate = initiallocaldate.format(formatter);
        while(!datessent.contains(initialdate)){
           LocalDate localDateTemp = LocalDate.parse(initialdate,formatter);
           LocalDate initiallocaldatetemp = localDateTemp.minusDays(1);
           initialdate = initiallocaldatetemp.format(formatter);
        }
        indexOfInitial = datessent.indexOf(initialdate);



        double percentINC = (Double.valueOf(navssent.get(0)) - Double.valueOf(navssent.get(1)))/Double.valueOf(navssent.get(1));
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        String percentChangeString = decimalFormat.format(percentINC*100.0);
        if(percentINC<0) {
            changepercent.setText(percentChangeString + "%");
            returnpercent.setTextColor(Color.parseColor("#f44336"));
        }
        else {
            changepercent.setText("+" + percentChangeString + "%");
            returnpercent.setTextColor(Color.parseColor("#0acf19"));
        }


        nav.setText(Double.toString(presentNAV));
        date.setText("NAV : "+datessent.get(0));
        if(navssent.size()<days){
            returnpercent.setText("-- %");
        }
        else{
            double initialNAV =  Math.round((Double.parseDouble(navssent.get(indexOfInitial).toString()))*100.0)/100.0;
            double absoluteNAV = ((presentNAV - initialNAV)/initialNAV);
            double annualisedNAV = (Math.pow((1+absoluteNAV),(365.0/Double.valueOf(days))))-1;
            if(days <365){
                if(absoluteNAV<0){
                    String formattedNumber = decimalFormat.format(absoluteNAV*100.0);
                    returnpercent.setText(formattedNumber+"%");
                    returnpercent.setTextColor(Color.parseColor("#f44336"));
                } else{
                    String formattedNumber = decimalFormat.format(absoluteNAV*100.0);
                    returnpercent.setText("+"+formattedNumber+"%");
                    returnpercent.setTextColor(Color.parseColor("#0acf19"));
                }

            }


             else{
                if(annualisedNAV<0){
                    String formattedNumber = decimalFormat.format(annualisedNAV*100.0);
                    returnpercent.setText(formattedNumber+"%");
                    returnpercent.setTextColor(Color.parseColor("#f44336"));
                } else{
                    String formattedNumber = decimalFormat.format(annualisedNAV*100.0);
                    returnpercent.setText("+"+formattedNumber+"%");
                    returnpercent.setTextColor(Color.parseColor("#0acf19"));
                }
            }
        }



    }

    private void bookmarker(String schemeName) {
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        String userId = user.getUid().toString();
        String present = "";
        databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild(user.getUid())) {
                    String mffavourite = snapshot.child(userId).child("mffavourite").getValue(String.class).toString();
                  if(mffavourite.contains(schemeName)){
                      bookmark.setImageResource(R.drawable.bookmark2);
                  }
                  else bookmark.setImageResource(R.drawable.bookmark1);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    bookmark.setImageResource(R.drawable.bookmark2);
                    databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String mffavourite = snapshot.child(userId).child("mffavourite").getValue(String.class).toString();
                            if(!mffavourite.contains(schemeName)) {
                                databaseReference.child("users").child(user.getUid()).child("mffavourite").setValue(mffavourite+schemeName+";");
                                bookmark.setImageResource(R.drawable.bookmark2);
                            }
                            else{
                                bookmark.setImageResource(R.drawable.bookmark1);
                                String modifiedfavourite = mffavourite.replace(schemeName+";","");
                                databaseReference.child("users").child(user.getUid()).child("mffavourite").setValue(modifiedfavourite);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


            }
        });




    }

    private void bottomMenu() {

        onemonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onemonth.setTextColor(Color.BLACK);
                onemonth.setClickable(false);
                graph("1M",navs,dates);
                detailstype.setText("1M return");
                returnPercentCalculator(30,navs,dates);
                oneyear.setTextColor(Color.parseColor("#808080"));
                oneyear.setClickable(true);
                sixmonths.setTextColor(Color.parseColor("#808080"));
                sixmonths.setClickable(true);
                threeyear.setTextColor(Color.parseColor("#808080"));
                threeyear.setClickable(true);
                fiveyear.setTextColor(Color.parseColor("#808080"));
                fiveyear.setClickable(true);
                all.setTextColor(Color.parseColor("#808080"));
                all.setClickable(true);
            }
        });
        sixmonths.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sixmonths.setTextColor(Color.BLACK);
                sixmonths.setClickable(false);
                graph("6M",navs,dates);
                detailstype.setText("6M return");
                returnPercentCalculator(30*6,navs,dates);
                onemonth.setTextColor(Color.parseColor("#808080"));
                onemonth.setClickable(true);
                oneyear.setTextColor(Color.parseColor("#808080"));
                oneyear.setClickable(true);
                threeyear.setTextColor(Color.parseColor("#808080"));
                threeyear.setClickable(true);
                fiveyear.setTextColor(Color.parseColor("#808080"));
                fiveyear.setClickable(true);
                all.setTextColor(Color.parseColor("#808080"));
                all.setClickable(true);
            }
        });
        oneyear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oneyear.setTextColor(Color.BLACK);
                oneyear.setClickable(false);
                graph("1Y",navs,dates);
                detailstype.setText("1Y annualised");
                returnPercentCalculator(365,navs,dates);
                onemonth.setTextColor(Color.parseColor("#808080"));
                onemonth.setClickable(true);
                sixmonths.setTextColor(Color.parseColor("#808080"));
                sixmonths.setClickable(true);
                threeyear.setTextColor(Color.parseColor("#808080"));
                threeyear.setClickable(true);
                fiveyear.setTextColor(Color.parseColor("#808080"));
                fiveyear.setClickable(true);
                all.setTextColor(Color.parseColor("#808080"));
                all.setClickable(true);
            }
        });
         threeyear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                threeyear.setTextColor(Color.BLACK);
                threeyear.setClickable(false);
                graph("3Y",navs,dates);
                detailstype.setText("3Y annualised");
                returnPercentCalculator(365*3,navs,dates);
                onemonth.setTextColor(Color.parseColor("#808080"));
                onemonth.setClickable(true);
                sixmonths.setTextColor(Color.parseColor("#808080"));
                sixmonths.setClickable(true);
                oneyear.setTextColor(Color.parseColor("#808080"));
                oneyear.setClickable(true);
                fiveyear.setTextColor(Color.parseColor("#808080"));
                fiveyear.setClickable(true);
                all.setTextColor(Color.parseColor("#808080"));
                all.setClickable(true);
            }
        });
          fiveyear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fiveyear.setTextColor(Color.BLACK);
                fiveyear.setClickable(false);
                graph("5Y",navs,dates);
                detailstype.setText("5Y annualised");
                returnPercentCalculator(365*5,navs,dates);
                onemonth.setTextColor(Color.parseColor("#808080"));
                onemonth.setClickable(true);
                sixmonths.setTextColor(Color.parseColor("#808080"));
                sixmonths.setClickable(true);
                threeyear.setTextColor(Color.parseColor("#808080"));
                threeyear.setClickable(true);
                oneyear.setTextColor(Color.parseColor("#808080"));
                oneyear.setClickable(true);
                all.setTextColor(Color.parseColor("#808080"));
                all.setClickable(true);
            }
        });
          all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                all.setTextColor(Color.BLACK);
                all.setClickable(false);
                graph("5Y",navs,dates);
                detailstype.setText("annualised");
                returnPercentCalculator(navs.size(), navs,dates);
                onemonth.setTextColor(Color.parseColor("#808080"));
                onemonth.setClickable(true);
                sixmonths.setTextColor(Color.parseColor("#808080"));
                sixmonths.setClickable(true);
                threeyear.setTextColor(Color.parseColor("#808080"));
                threeyear.setClickable(true);
                fiveyear.setTextColor(Color.parseColor("#808080"));
                fiveyear.setClickable(true);
                oneyear.setTextColor(Color.parseColor("#808080"));
                oneyear.setClickable(true);
            }
        });

    }

    private void graph(String type, ArrayList<String> navssent , ArrayList<String> datessent) {
        int days = 30;
        if(type == "1M")
            days = 30;
        else if (type == "6M") {
            if(navssent.size()>=180){
                days = 180;
            }
            else{
                returnPercentCalculator(180, navssent,datessent);
                return;
            }
        } else if (type == "1Y") {
            if(navssent.size()>=365){
                days = 365;
            }
            else{
                returnPercentCalculator(365, navssent,datessent);
                return;
            }
        } else if (type =="3Y") {
            if(navssent.size()>=365*3){
                days = 365*3;
            }
            else{
                returnPercentCalculator(365*3, navssent,datessent);
                return;
            }
        } else if (type == "5Y") {
            if(navssent.size()>=365*5){
                days = 365*5;
            }
            else{
                returnPercentCalculator(365*5, navssent,datessent);
                return;
            }
        }
        else{
            days = dates.size();
            returnPercentCalculator(days,navssent,datessent);
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
        lineDataSet.setLineWidth(3.0f);


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
                   schemeType.setText(meta.getString("scheme_type")+"  •  ");
                   String schemecategory = meta.getString("scheme_category");

//                   Log.d("TAG", "onResponse: "+);
                   schemeCategory.setText(schemecategory.replace("Equity Scheme - ",""));
                   JSONArray datejsonArray = response.getJSONArray("data");
                   for (int i = 0; i <datejsonArray.length() ; i++) {
                       JSONObject jsonObject = datejsonArray.getJSONObject(i);
                       dates.add(jsonObject.getString("date"));
                       navs.add(jsonObject.getString("nav"));
                   }

                   graph("1M",navs,dates);
                   returnPercentCalculator(30,navs,dates);
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
        onemonth = findViewById(R.id.onemonth);
        sixmonths = findViewById(R.id.sixmonth);
        oneyear = findViewById(R.id.oneyear);
        threeyear = findViewById(R.id.threeyear);
        fiveyear = findViewById(R.id.fiveyear);
        all = findViewById(R.id.all);
        bookmark = findViewById(R.id.bookmark);
        detailstype = findViewById(R.id.type);

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