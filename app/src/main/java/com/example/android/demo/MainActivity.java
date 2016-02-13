package com.example.android.demo;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    EditText password, userName;
    Button login, resister;
    public static final String INTENT_KEY = "INTENT_KEY";
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        password = (EditText) findViewById(R.id.editText2);
        userName = (EditText) findViewById(R.id.editText1);
        login = (Button) findViewById(R.id.button1);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = userName.getText().toString();
                final String passwd = password.getText().toString();
                if (username != null && passwd != null)
                    new AsyncTask<Void, Void, String>() {
                        @Override
                        protected String doInBackground(Void... params) {
                            try {
                                URL url = new URL("http://52.70.236.212:8080/service/user/login/matrimony");
                                HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
                                httpCon.setDoInput(true);
                                httpCon.setDoOutput(true);
                                httpCon.setRequestMethod("PUT");
                                httpCon.setRequestProperty("Content-Type", "application/json");
                                String aut = username + ":" + passwd;
                                byte[] b = aut.getBytes();

                                String encodeA = new String(Base64.encodeToString(b, Base64.DEFAULT));
                                httpCon.setRequestProperty("Authorization", "Basic " + encodeA);
                                httpCon.connect();
                                OutputStreamWriter out = new OutputStreamWriter(
                                        httpCon.getOutputStream());
                                out.write("Resource content");
                                out.close();
                                return httpCon.getInputStream().toString();
                            } catch (Exception e) {
                                e.printStackTrace();
                                return e.toString();
                            }
                        }

                        @Override
                        protected void onPostExecute(String aVoid) {
                            Intent intent = new Intent(MainActivity.this, WelcomeActivity.class);
                            intent.putExtra(INTENT_KEY,aVoid);
                            startActivity(intent);
                        }
                    };
                else {
                    Toast.makeText(MainActivity.this, "enter all field", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    private void loadurl() throws IOException {
        URL url = new URL("http://52.70.236.212:8080/web.matrimony/#/login");
        HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
        httpCon.setDoOutput(true);
        httpCon.setRequestProperty(
                "userName", "application/x-www-form-urlencoded");
        httpCon.setRequestMethod("PUT");
        httpCon.connect();
    }
}
