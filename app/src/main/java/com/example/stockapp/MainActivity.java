package com.example.stockapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    private ArrayList<String> companies = new ArrayList<String>();
    private static RequestQueue queue;
    private TextView textView2;
    private Button button;
    private Button selectedTicker;
    private EditText tickerText;
    private ListView listview;
    private ArrayList<String> trendingStockList;
    private static String tickerOfStock;
    private static ArrayList<String> dataArray;

    public static String getTickerSymbol() {
        return tickerOfStock;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listview = (ListView)findViewById(R.id.listview);
        showData.setKeywords();
        queue = Volley.newRequestQueue(this);
        new Loader().execute();
        trendingStockList = Loader.getTrendingTickers();
        button = findViewById(R.id.button);
        selectedTicker = findViewById(R.id.selectedTicker);
        tickerText = findViewById(R.id.tickerText);

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() { //this is the on resume of the main activity where I initially ask for the data array of stock information
        super.onResume();

        selectedTicker.setOnClickListener(v->{
            String ticker = String.valueOf(tickerText.getText());
            try {
                dataArray = new Loader_2().execute(ticker).get(); //Loader 2 is the class where I get the arraylist of data, my problem is when I put this code in on create without the event listener it does not work.
                tickerOfStock = ticker;                 //this is the ticker of the stock being set to an instance variable
                Intent intent = new Intent(this, showData.class);
                startActivity(intent);
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        });

        button.setOnClickListener(v->{
            ArrayAdapter arrayAdapter = new ArrayAdapter<String>(this, R.layout.list_item, R.id.txtitem, trendingStockList);
            listview.setAdapter(arrayAdapter);
        });

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) { //any stock you click will bring you to a page with its important data
                ArrayList<String> tickerList = Loader.getTickers();
                String ticker = tickerList.get(position);
                try {
                    dataArray = new Loader_2().execute(ticker).get();
                    tickerOfStock = ticker;
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(MainActivity.this, showData.class);
                startActivity(intent);
            }
        });

    }
    public static ArrayList<String> getDataArray(){
        return dataArray;
    }
    protected static RequestQueue getQueue(){
        return queue;
    }
}