package com.example.stockapp;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Loader extends AsyncTask<URL,Integer, ArrayList<String>>{
    private static ArrayList<String> trendingTickers = new ArrayList<String>();
    private static ArrayList<String> tickers = new ArrayList<String>();

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected ArrayList<String> doInBackground(URL... urls) {
        String url = "https://apidojo-yahoo-finance-v1.p.rapidapi.com/market/get-trending-tickers?region=US";

        ArrayList<String> companies = new ArrayList<String>();
        RequestQueue queue = MainActivity.getQueue();
        DecimalFormat dollar = new DecimalFormat("#,###,##0.00");
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url,(String) null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray firstLayerNames = response.names();
                            if(response.has(firstLayerNames.getString(0))) {
                                JSONObject SecondLayer = response.getJSONObject(firstLayerNames.getString(0));
                                JSONArray SecondLayerNames = SecondLayer.names();
                                if (SecondLayer.has(SecondLayerNames.getString(0))) {
                                    JSONArray thirdLayer = SecondLayer.getJSONArray(SecondLayerNames.getString(0));
                                    JSONObject thirdLayerParsed = thirdLayer.getJSONObject(0);
                                    JSONArray quotesOfCompanies = thirdLayerParsed.getJSONArray("quotes");
                                    for (int i = 0; i < quotesOfCompanies.length(); i++) {
                                        if (quotesOfCompanies.getJSONObject(i) != null) {
                                            JSONObject trendingStocks = quotesOfCompanies.getJSONObject(i);
                                            try {
                                                double priceOfTrending = trendingStocks.getDouble("regularMarketPrice");
                                                trendingTickers.add(trendingStocks.getString("shortName") + " $" + dollar.format(priceOfTrending));
                                                tickers.add(trendingStocks.getString("symbol"));
                                            } catch (JSONException e) {
                                                continue;
                                            }
                                        }
                                    }
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();

            }
        }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  headers = new HashMap<String, String>();
                headers.put("x-rapidapi-key", "f713c8ca8dmsh3facc2d11a4d8c1p1a61e6jsn68de2705e792");
                headers.put("x-rapidapi-host", "apidojo-yahoo-finance-v1.p.rapidapi.com");

                return headers;
            };
        };
        queue.add(request);
    return null;
    }

    public static ArrayList<String> getTrendingTickers() {
        return trendingTickers;
    }
    public static ArrayList<String> getTickers(){
        return tickers;
    }
}
