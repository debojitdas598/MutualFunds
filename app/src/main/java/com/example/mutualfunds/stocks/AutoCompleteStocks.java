package com.example.mutualfunds.stocks;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.mutualfunds.cryptocurreny.AutocompleteCrypto;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AutoCompleteStocks {

    Context context;


    public AutoCompleteStocks(Context context){ this.context = context; }

    public ArrayList<String> stockName(){
        ArrayList<String> stocksNameList = new ArrayList<>();
        String url = "https://latest-stock-price.p.rapidapi.com/any";
        RequestQueue queue = Volley.newRequestQueue(context);

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject item = response.getJSONObject(i);
                        String symbol = item.getString("symbol");
                        stocksNameList.add(symbol);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "doesnt works", Toast.LENGTH_SHORT).show();
            }
        }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("X-RapidAPI-Key","c067ca75dcmsh429011dd9300cb1p112e5fjsnb7140aa4edd3");
                headers.put("X-RapidAPI-Host","latest-stock-price.p.rapidapi.com");
                return headers;
            }
        };
        queue.add(request);

        return stocksNameList;
    }


 public ArrayList<String> stockIdentifier(){
        ArrayList<String> stocksIdentifierList = new ArrayList<>();
        String url = "https://latest-stock-price.p.rapidapi.com/any";
        RequestQueue queue = Volley.newRequestQueue(context);

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject item = response.getJSONObject(i);
                        String symbol = item.getString("identifier");
                        stocksIdentifierList.add(symbol);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "doesnt works", Toast.LENGTH_SHORT).show();
            }
        }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("X-RapidAPI-Key","c067ca75dcmsh429011dd9300cb1p112e5fjsnb7140aa4edd3");
                headers.put("X-RapidAPI-Host","latest-stock-price.p.rapidapi.com");
                return headers;
            }
        };
        queue.add(request);

        return stocksIdentifierList;
    }


}
