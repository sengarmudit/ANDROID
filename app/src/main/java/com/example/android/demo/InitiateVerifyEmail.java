package com.example.android.demo;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class InitiateVerifyEmail extends AsyncTask<Void, Void, String> {
    String response;
    String emailId = UserInformationActivity.emailJSON;

    @Override
    protected String doInBackground(Void... params) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        try {
            String initiateEmailVerify = "http://52.70.236.212:8080/service/user/initiate/verify/email/" + emailId;
            URL url = new URL(initiateEmailVerify);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("PUT");
            urlConnection.setRequestProperty("authToken", UserInformationActivity.authToken);
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
        }finally {
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
        if (s.contains("true")) {
            UserInformationActivity.verifyEmailText.setVisibility(View.VISIBLE);
            UserInformationActivity.verifyEmail.setVisibility(View.INVISIBLE);
        }
        else{
            UserInformationActivity.verifyEmailText.setText("email not sent");
            UserInformationActivity.verifyEmailText.setVisibility(View.VISIBLE);
        }
    }
}
