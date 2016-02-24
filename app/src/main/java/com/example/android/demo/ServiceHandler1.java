package com.example.android.demo;


import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ServiceHandler1 {
    static String response = null;
    public final static int GET = 1;
    public final static int POST = 2;
    HttpURLConnection urlConnection = null;
    BufferedReader reader = null;
    Context context;
    HashMap<String,String> hashMap = new HashMap<String ,String>();

    public String makeServiceCall(String url, int method) {
        hashMap.put("access_code","AVBI00DA31AO65IBOA");
        hashMap.put("currency","INR");
        hashMap.put("amount",PaymentActivity.amount.getText().toString());
        return this.makeServiceCall(url, method, null);
    }

    public String makeServiceCall (String url, int method, HashMap<String,String > hashMap){
        try{
            URL url1 = new URL(url);
            urlConnection = (HttpURLConnection) url1.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

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
        return response;
    }

    public void set(Context context) {
        this.context = context;
    }
}
