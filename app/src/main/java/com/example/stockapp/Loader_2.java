package com.example.stockapp;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Loader_2 extends AsyncTask<String,Integer, ArrayList<String>> {
    private static ArrayList<String> dataArray = new ArrayList<String>();



    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected ArrayList<String> doInBackground(String... ticker) {
        String url = "https://apidojo-yahoo-finance-v1.p.rapidapi.com/stock/v2/get-analysis?symbol=";
        url = url.concat(ticker[0]);
        ArrayList<String> keyWords = showData.getKeywords();
        ArrayList<String> price = showData.getPrice();
        RequestQueue queue = MainActivity.getQueue();
        dataArray.clear();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url,(String) null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if(response.has("financialData")) {
                            JSONObject SecondLayer = null;
                            try {
                                SecondLayer = response.getJSONObject("financialData");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            String trans = "";
                            for(int i = 0;i<keyWords.size();i++) {
                                String dataPoint = keyWords.get(i);
                                if(SecondLayer.has(dataPoint)){
                                    try {
                                        JSONObject data = SecondLayer.getJSONObject(dataPoint);
                                        trans = trans.concat(dataPoint + " " + data.get("fmt")+"\n");
                                        trans = trans.replaceAll("(.)([A-Z])", "$1 $2");
                                        trans = trans.substring(0, 1).toUpperCase() + trans.substring(1);
                                        dataArray.add(trans);
                                    }catch(JSONException e){
                                        try {
                                            trans = trans.concat(dataPoint + " "+ SecondLayer.get(dataPoint)+"\n");
                                            trans = trans.replaceAll("(.)([A-Z])", "$1 $2");
                                            trans = trans.substring(0, 1).toUpperCase() + trans.substring(1);
                                            dataArray.add(trans);
                                        } catch (JSONException jsonException) {
                                            jsonException.printStackTrace();
                                        }
                                    }
                                }
                                trans = "";
                            }
                        }
                        if(response.has("price")) {
                            JSONObject SecondLayer = null;
                            try {
                                SecondLayer = response.getJSONObject("price");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            String trans = "";
                            for(int i = 0;i<price.size();i++) {
                                String dataPoint = price.get(i);
                                if(SecondLayer.has(dataPoint)){
                                    try {
                                        JSONObject data = SecondLayer.getJSONObject(dataPoint);
                                        trans = trans.concat(dataPoint + " " + data.get("fmt")+"\n");
                                        trans = trans.replaceAll("(.)([A-Z])", "$1 $2");
                                        trans = trans.substring(0, 1).toUpperCase()+ trans.substring(1);
                                        dataArray.add(trans);
                                    }catch(JSONException e){
                                        try {
                                            trans = trans.concat(dataPoint + " "+ SecondLayer.get(dataPoint)+"\n");
                                            trans = trans.replaceAll("(.)([A-Z])", "$1 $2");
                                            trans = trans.substring(0, 1).toUpperCase()+ trans.substring(1);
                                            dataArray.add(trans);
                                        } catch (JSONException jsonException) {
                                            jsonException.printStackTrace();
                                        }
                                    }
                                }
                                trans = "";
                            }
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
                headers.put("x-rapidapi-key", "**********************"); //must use your own api key
                headers.put("x-rapidapi-host", "apidojo-yahoo-finance-v1.p.rapidapi.com");

                return headers;
            };
        };

        queue.add(request);

        return dataArray;
    }

    @Override
    protected void onPostExecute(ArrayList<String> strings) {
        super.onPostExecute(strings);
        if(strings.size() > 0){
            System.out.println("Data is not null");
            System.out.println(strings.toString());
        }else{
            System.out.println("Data is null");
        }
    }

    public static ArrayList<String> getDataArray(){
        return dataArray;
    }
}
