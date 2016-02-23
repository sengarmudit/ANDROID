package com.example.android.demo;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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

public class RegisterActivity extends AppCompatActivity {

    EditText firstName, lastName, userName, gender, email, mobileNumber, password;
    Button register1, cancel;
    TextView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        firstName = (EditText) findViewById(R.id.firstName);
        lastName = (EditText) findViewById(R.id.lastName);
        userName = (EditText) findViewById(R.id.userName);
        gender = (EditText) findViewById(R.id.gender);
        password = (EditText) findViewById(R.id.password);
        email = (EditText) findViewById(R.id.email);
        mobileNumber = (EditText) findViewById(R.id.mobile);
        register1 = (Button) findViewById(R.id.register1);
        cancel = (Button) findViewById(R.id.cancel);
        view = (TextView) findViewById(R.id.fail);

        register1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mobileNumber.getText().toString().length() != 10) {
                    mobileNumber.setError(getString(R.string.mobileNumberEror));
                }
                if (!email.getText().toString().trim().matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")) {
                    email.setError(getString(R.string.emailError));
                }
                LoadRegister reg = new LoadRegister();
                reg.execute(firstName.getText().toString(), lastName.getText().toString(), userName.getText().toString(),
                        gender.getText().toString(), password.getText().toString(), email.getText().toString(),
                        mobileNumber.getText().toString(), view);
                reg.execute();
            }
        });

        firstName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enableRegisterButton();
            }
        });

        lastName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enableRegisterButton();
            }
        });

        userName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enableRegisterButton();
            }
        });

        gender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enableRegisterButton();
            }
        });

        password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enableRegisterButton();
            }
        });

        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enableRegisterButton();
            }
        });

        mobileNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enableRegisterButton();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    class LoadRegister extends AsyncTask<Void, Void, String> {

        String firstName, lastName, userName, gender, password, email, mobileNumber;
        String response = null;
        TextView view;

        public void execute(String firstName, String lastName, String userName, String gender,
                            String password, String email, String mobileNumber, TextView view) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.userName = userName;
            this.gender = gender;
            this.password = password;
            this.email = email;
            this.mobileNumber = mobileNumber;
            this.view = view;
        }

        @Override
        protected String doInBackground(Void... params) {
            HttpURLConnection urlConnection1 = null;
            BufferedReader reader1 = null;
            int code;
            try {
                String baseUrl = getString(R.string.registerBaseUrl);
                JSONObject jobj = new JSONObject();
                jobj.put("firstName", firstName);
                jobj.put("lastName", lastName);
                jobj.put("userName", userName);
                jobj.put("email", email);
                jobj.put("mobileNumber", mobileNumber);
//                jobj.put("gender", gender);
                jobj.put("password", password);

                URL url = new URL(baseUrl);

                urlConnection1 = (HttpURLConnection) url.openConnection();
                urlConnection1.setRequestMethod("POST");
                urlConnection1.setRequestProperty("Content-Type", "application/json");
                urlConnection1.setRequestProperty("Accept", "application/json");
                urlConnection1.connect();

                OutputStream os = urlConnection1.getOutputStream();
                OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
                osw.write(jobj.toString());
                osw.flush();
                osw.close();

                InputStream inputStream = urlConnection1.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }
                reader1 = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                String line;
                while ((line = reader1.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                this.response = buffer.toString();

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection1 != null) {
                    urlConnection1.disconnect();
                }
                if (reader1 != null) {
                    try {
                        reader1.close();
                    } catch (final IOException e) {
                        Log.e("Placeholder fragment", "Error closing stream", e);
                    }
                }
            }
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s.contains("true")) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            } else {
                view.setEnabled(true);
            }
        }
    }

    public void enableRegisterButton (){
        if (!firstName.getText().toString().isEmpty() && !lastName.getText().toString().isEmpty()
                && !userName.getText().toString().isEmpty() && !gender.getText().toString().isEmpty()
                && !password.getText().toString().isEmpty() && !email.getText().toString().isEmpty()
                && !mobileNumber.getText().toString().isEmpty()) {
                    register1.setEnabled(true);
        }
    }
}