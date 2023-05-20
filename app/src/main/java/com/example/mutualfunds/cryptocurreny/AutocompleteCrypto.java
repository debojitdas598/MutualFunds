package com.example.mutualfunds.cryptocurreny;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AutocompleteCrypto {
    Context context;
    ArrayList<String> strcoinname;
    ArrayList<String> strcoincode;

    public AutocompleteCrypto(Context context){
        this.context = context;
    }

    public ArrayList<String> coinName(){
        strcoinname = new ArrayList<>();
        String url = "https://api.coingecko.com/api/v3/search";
        RequestQueue queue = Volley.newRequestQueue(context);


        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String  response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray coinsArray = jsonObject.getJSONArray("coins");
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject coinObject = coinsArray.getJSONObject(i);
                                String coinName = coinObject.getString("name");
                                strcoinname.add(coinName);
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

        queue.add(stringRequest);
        return strcoinname;
    }

    public ArrayList<String> coinCode(){
        strcoincode = new ArrayList<>();
        String url = "https://api.coingecko.com/api/v3/search";
        RequestQueue queue = Volley.newRequestQueue(context);


        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject jsonObject = new JSONObject(String.valueOf(response));
                            JSONArray coinsArray = jsonObject.getJSONArray("coins");
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject coinObject = coinsArray.getJSONObject(i);
                                String coinCode = coinObject.getString("code");
                                strcoincode.add(coinCode);
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

        queue.add(stringRequest);
        return strcoincode;

    }

}
