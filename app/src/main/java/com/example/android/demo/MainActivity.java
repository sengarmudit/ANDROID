package com.example.android.demo;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    EditText password, userName;
    Button login;
    public static final String INTENT_KEY = "INTENT_KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        password = (EditText) findViewById(R.id.editText2);
        userName = (EditText) findViewById(R.id.editText1);
        login = (Button) findViewById(R.id.button1);
        userName.setText("mudit");
        password.setText("neeraj");
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Loadurl aVoid = new Loadurl();
                aVoid.execute(userName.getText().toString(), password.getText().toString());
                aVoid.execute();
            }
        });
    }

    class Loadurl extends AsyncTask<Void, Void, String> {
        String userName;
        String password;

        public void execute(String userName, String password) {
            this.userName = userName;
            this.password = password;
        }

        @Override
        protected String doInBackground(Void... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String forecastJsonStr = null;

            try {
                String baseUrl = "http://52.70.236.212:8080/service/user/login/matrimony";


//                Authenticator.setDefault(new Authenticator() {
//                    protected PasswordAuthentication getPasswordAuthentication() {
//                        return new PasswordAuthentication("mudit", "neeraj".toCharArray());
//                    }
//                });
//                urlConnection = (HttpURLConnection) new URL(baseUrl).openConnection();
//                urlConnection.setUseCaches(false);
//                urlConnection.connect();


                URL url = new URL(baseUrl);

                String authString = "mudit:neeraj";


                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("PUT");

                String encodeA = new String(Base64.encode(authString.getBytes(), Base64.DEFAULT));

//                urlConnection.addRequestProperty("userName","mudit");
//                urlConnection.addRequestProperty("password","neeraj");


                urlConnection.setRequestProperty("Authorization", "basic " + encodeA);


                urlConnection.connect();

//                Log.d("qwer", urlConnection.getResponseCode() + "");

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }

                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                forecastJsonStr = buffer.toString();
            } catch (IOException e) {
                Log.e("PlaceholderFragment", "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return e.getMessage();
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
            return forecastJsonStr;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Intent intent = new Intent(MainActivity.this, WelcomeActivity.class);
            intent.putExtra(INTENT_KEY, s);
            startActivity(intent);
        }
    }
}
