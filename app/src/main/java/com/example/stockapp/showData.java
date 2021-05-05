package com.example.stockapp;

import android.content.Context;
import android.os.Bundle;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import androidx.appcompat.app.AppCompatActivity;

import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class showData extends AppCompatActivity {
    private static ArrayList<String> keyWords= new ArrayList<String>();
    private static ArrayList<String> price = new ArrayList<String>();
    private static ArrayList<String> dataArray = new ArrayList<String>();
    private static Context context;
    private ListView listview;
    private Button button2;
    private TextView stockName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_data2);
        button2 = findViewById(R.id.button2);
        listview = (ListView)findViewById(R.id.dataList);
        dataArray = Loader_2.getDataArray();
        stockName = findViewById(R.id.stockName);
        stockName.setText(MainActivity.getTickerSymbol());

    }

    @Override
    protected void onResume() {
        super.onResume();
        button2.setOnClickListener(v->{
            dataArray = MainActivity.getDataArray(); //here I am getting the dataArray from the main activity and setting it to the listview on button click in the second activity
            ArrayAdapter arrayAdapter = new ArrayAdapter<String>(this, R.layout.list_item, R.id.txtitem, dataArray);
            listview.setAdapter(arrayAdapter);
            arrayAdapter.notifyDataSetChanged();
        });
    }

    @Override
    protected void onStart() {
        super.onStart();


    }

    public static void setKeywords(){
        keyWords.add("currentPrice");
        keyWords.add("revenueGrowth");
        keyWords.add("returnOnEquity");
        keyWords.add("debtToEquity");
        keyWords.add("targetLowPrice");
        keyWords.add("targetMeanPrice");
        keyWords.add("targetHighPrice");
        keyWords.add("recommendationKey");
    }
    public static ArrayList<String> getKeywords(){
        return keyWords;
    }
    public static ArrayList<String> getPrice(){
        return price;
    }


    public static boolean dataArrayIsEmpty() {
        if(dataArray.isEmpty()){
            return true;
        }else{
            return false;
        }
    }


}