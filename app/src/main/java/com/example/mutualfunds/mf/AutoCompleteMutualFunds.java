package com.example.mutualfunds.mf;

import android.content.Context;
import android.widget.FrameLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AutoCompleteMutualFunds {

    Context context;
    ArrayList<String> strschemename;
    ArrayList<String> strschemecode;
    public AutoCompleteMutualFunds(Context context){
        this.context = context;
    }

    public ArrayList<String> schemeName(){
        strschemename = new ArrayList<>();
        String url = "https://api.mfapi.in/mf";
        RequestQueue queue = Volley.newRequestQueue(context);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                String schemeName = jsonObject.getString("schemeName").toString();
                                strschemename.add(schemeName);

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
        return strschemename;

    }

    public ArrayList<String> schemeCode(){
        strschemecode = new ArrayList<>();
        String url = "https://api.mfapi.in/mf";
        RequestQueue queue = Volley.newRequestQueue(context);


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                String schemeCode = jsonObject.getString("schemeCode").toString();
                                strschemecode.add(schemeCode);
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
        return strschemecode;

    }

}
