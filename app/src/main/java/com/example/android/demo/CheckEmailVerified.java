package com.example.android.demo;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class CheckEmailVerified extends AsyncTask<Void, Void, String> {
    String response,emailVerified;
    HttpURLConnection urlConnection = null;
    BufferedReader reader = null;
    Context context;

    @Override
    protected String doInBackground(Void... params) {
        try {

            String checkEmailVerified = context.getString(R.string.checkEmailVerified);
            URL url = new URL(checkEmailVerified);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("authToken", WelcomeActivity.authToken);
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

    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        try {
            JSONObject jsonObject = new JSONObject(s);
            emailVerified = jsonObject.getString("emailVerified");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("email",emailVerified);
//        String[] jsonResponse = s.split("\"");
        if (emailVerified.equals("true")) {
            UserInformationActivity.verifyEmailText.setVisibility(View.VISIBLE);
            UserInformationActivity.verifyEmailText.setText("Verified");
            UserInformationActivity.verifyEmail.setVisibility(View.INVISIBLE);
        } else {
//            UserInformationActivity.verifyEmailText.setVisibility(View.VISIBLE);


        }
    }

    public void set(Context context) {
        this.context = context;
    }
}
