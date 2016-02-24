package com.example.android.demo;

import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PaymentActivity extends AppCompatActivity {
    public static EditText amount, plan;
    public Button pay;
    String amountValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        amount = (EditText) findViewById(R.id.amount);
        plan = (EditText) findViewById(R.id.plan);
        pay = (Button) findViewById(R.id.pay);

        amountValue = amount.getText().toString();


        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Payment payment = new Payment();
                payment.execute();*/


            }
        });
    }


    class Payment extends AsyncTask<Void, Void, String> {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String response = null;

        @Override
        protected String doInBackground(Void... params) {
            try {
                String baseUrl ="https://secure.ccavenue.com/transaction/transaction.do";
//                        getString(R.string.redirectURL);
                URL url = new URL(baseUrl);

                JSONObject obj = new JSONObject();
                obj.put("access_code", "AVBI00DA31AO65IBOA");
                obj.put("currency", "INR");
                obj.put("amount", amountValue);

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("Accept", "application/json");

                urlConnection.connect();

                OutputStream os = urlConnection.getOutputStream();
                OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
                osw.write(obj.toString());
                osw.flush();
                osw.close();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }
                response = buffer.toString();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("PlaceholderFragment", "Error closing stream", e);
                    }
                }
            }
            Log.d("responseIs",response);
            return response;
        }
    }
}
