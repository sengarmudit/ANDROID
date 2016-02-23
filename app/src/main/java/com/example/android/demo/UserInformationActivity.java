package com.example.android.demo;

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
import java.net.ProtocolException;
import java.net.URL;

public class UserInformationActivity extends AppCompatActivity {
    public static EditText firstName, lastName, email, mobile, enterCode;
    public static Button submit, verifyEmail, verifyMobile, verifyCode;
    public static String authToken, userID;
    public static TextView invisible, verifyEmailText;
    public static String emailVerifiedJSON, mobileVerifiedJSON, firstNameJSON, lastNameJSON, emailJSON, mobileNumberJSON, statusJSON;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        GetText getText = new GetText();
        getText.execute();

        firstName = (EditText) findViewById(R.id.firstName);
        lastName = (EditText) findViewById(R.id.lastName);
        email = (EditText) findViewById(R.id.email);
        mobile = (EditText) findViewById(R.id.mobile);
        submit = (Button) findViewById(R.id.submit);
        invisible = (TextView) findViewById(R.id.invisible);
        authToken = LoginActivity.authenticationToken;
        userID = LoginActivity.userId;
        verifyEmail = (Button) findViewById(R.id.verifyEmail);
        verifyMobile = (Button) findViewById(R.id.verifyMobie);
        verifyEmailText = (TextView) findViewById(R.id.verifyEmailText);
        verifyCode = (Button) findViewById(R.id.verifyCode);
        enterCode = (EditText) findViewById(R.id.enterCode);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateBasicInfo basicInfo = new UpdateBasicInfo();
                basicInfo.execute(firstName.getText().toString(), lastName.getText().toString(),
                        email.getText().toString(), mobile.getText().toString());
                basicInfo.execute();
            }
        });

        verifyEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InitiateVerifyEmail initiateVerifyEmail = new InitiateVerifyEmail();
                initiateVerifyEmail.execute();
            }
        });

        verifyMobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InitiateVerifyMobile initiateVerifyMobile = new InitiateVerifyMobile();
                initiateVerifyMobile.execute();
            }
        });
    }

    class UpdateBasicInfo extends AsyncTask<Void, Void, String> {
        String firstName, lastName, email, mobileNumber;
        String response;

        public void execute(String firstName, String lastName, String email, String mobileNumber) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.email = email;
            this.mobileNumber = mobileNumber;
        }

        @Override
        protected String doInBackground(Void... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            int code;
            try {
                String baseUrl = getString(R.string.userInfoURL);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("firstName", firstName);
                jsonObject.put("lastName", lastName);
                jsonObject.put("email", email);
                jsonObject.put("mobileNumber", mobileNumber);

                URL url = new URL(baseUrl);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("PUT");
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.setRequestProperty("authToken", authToken);
                urlConnection.connect();

                OutputStream os = urlConnection.getOutputStream();
                OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
                osw.write(jsonObject.toString());
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

                this.response = buffer.toString();
            } catch (JSONException e) {
                e.printStackTrace();
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

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s.contains("true")) {
                invisible.setVisibility(View.VISIBLE);
//                startActivity(new Intent(UserInformationActivity.this, UserInformationActivity.class));
            }
        }
    }


    class GetText extends AsyncTask<Void, Void, String> {
        String basicInfo = null;


        @Override
        protected String doInBackground(Void... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            firstName = (EditText) findViewById(R.id.firstName);
            lastName = (EditText) findViewById(R.id.lastName);
            email = (EditText) findViewById(R.id.email);
            mobile = (EditText) findViewById(R.id.mobile);
            try {
                String getInfoURL = getString(R.string.getBasicInfoURL) + userID;

                URL url = new URL(getInfoURL);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setRequestProperty("authToken", authToken);
                urlConnection.connect();

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

                basicInfo = buffer.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
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
            return basicInfo;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONObject basicInfoMoreAttrs = jsonObject.getJSONObject("basicInfoMoreAttrs");
                firstNameJSON = basicInfoMoreAttrs.getString("firstName");
                firstName.setText(firstNameJSON);
                lastNameJSON = basicInfoMoreAttrs.getString("lastName");
                lastName.setText(lastNameJSON);
                emailJSON = basicInfoMoreAttrs.getString("email");
                email.setText(emailJSON);
                mobileNumberJSON = basicInfoMoreAttrs.getString("mobileNumber");
                mobile.setText(mobileNumberJSON);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            /*infoDetails = s.split("\"");
            firstName.setText(infoDetails[9]);
            lastName.setText(infoDetails[13]);
            email.setText(infoDetails[17]);
            mobile.setText(infoDetails[21]);*/
        }
    }


}