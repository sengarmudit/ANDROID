package com.example.android.demo;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {
    EditText password, userName;
    Button login, register;
    public static final String INTENT_KEY1 = "INTENT_KEY1";
    public static String authenticationToken, userId, status;
    TextView visible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        password = (EditText) findViewById(R.id.editText2);
        userName = (EditText) findViewById(R.id.editText1);
        login = (Button) findViewById(R.id.button1);
        visible = (TextView) findViewById(R.id.visible);
        register = (Button) findViewById(R.id.register);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkInternetConenction()) {
                    if (userName.getText().toString().isEmpty()) {
                        userName.setError(getString(R.string.userName_error));
                    }
                    if (password.getText().toString().isEmpty()) {
                        password.setError(getString(R.string.userName_error));
                    } else {
                        Loadurl aVoid = new Loadurl();
                        aVoid.execute(userName.getText().toString(), password.getText().toString(), visible);
                        aVoid.execute();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, getString(R.string.internet_error), Toast.LENGTH_LONG).show();
                }
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkInternetConenction()) {
                    startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                } else {
                    Toast.makeText(LoginActivity.this, getString(R.string.internet_error), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    class Loadurl extends AsyncTask<Void, Void, String> {
        String userName;
        String password;
        String forecastJsonStr = null;
        TextView view;

        public void execute(String userName, String password, TextView view) {
            this.userName = userName;
            this.password = password;
            this.view = view;
        }

        @Override
        protected String doInBackground(Void... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            int code = 0;
            try {
                String baseUrl = getString(R.string.login_base_url);

                JSONObject obj = new JSONObject();

                obj.put("userName", userName);
                obj.put("password", password);

                URL url = new URL(baseUrl);

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("PUT");

                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.connect();

                OutputStream os = urlConnection.getOutputStream();
                OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
                osw.write(obj.toString());
                osw.flush();
                osw.close();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuilder buffer = new StringBuilder();
                if (inputStream == null) {
                    return null;
                }

                reader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                this.forecastJsonStr = buffer.toString();

            } catch (IOException e) {
                Log.e("PlaceholderFragment", "Error ", e);
                return e.getMessage();
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
            return forecastJsonStr;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(forecastJsonStr);
                authenticationToken = jsonObject.getString("authenticationToken");
                userId = jsonObject.getString("userId");
                status = jsonObject.getString("status");

            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (s.contains("errorCode")) {
                view.setVisibility(View.VISIBLE);
            } else {
                Intent intent = new Intent(LoginActivity.this, WelcomeActivity.class);
                intent.putExtra(INTENT_KEY1, userName);
                startActivity(intent);
            }
        }
    }

    public boolean checkInternetConenction() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(getBaseContext().CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        } else return false;
    }
}