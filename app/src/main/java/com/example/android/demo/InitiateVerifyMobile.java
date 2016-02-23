package com.example.android.demo;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

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

public class InitiateVerifyMobile extends AsyncTask<Void, Void, String> {
    String response, status;
    String mobileNumber = UserInformationActivity.mobileNumberJSON;

    @Override
    protected String doInBackground(Void... params) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        try {
            String initiateMobileVerify = "http://52.70.236.212:8080/service/user/initiate/verify/mobile/number";

            JSONObject obj = new JSONObject();
            obj.put("mobileNumber", mobileNumber);

            URL url = new URL(initiateMobileVerify);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("PUT");
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setRequestProperty("authToken", UserInformationActivity.authToken);
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
        return response;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        try {
            JSONObject jsonObject = new JSONObject(s);
            status = jsonObject.getString("status");
            Log.d("status",status);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (status.equals("true")) {
            UserInformationActivity.verifyMobile.setVisibility(View.INVISIBLE);
            UserInformationActivity.verifyCode.setVisibility(View.VISIBLE);
            UserInformationActivity.enterCode.setVisibility(View.VISIBLE);
        }
    }
}
