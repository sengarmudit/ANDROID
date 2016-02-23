package com.example.android.demo;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FamilyInformation extends AppCompatActivity {
    String authToken;
    EditText brotherCount, sisterCount, familyValues, familyType, familyStatus, familySummary, name, age,
            relation, professionalSummary, maritalStatus, livingWithFamily;
    Button submit;
    Button add;
    Button remove;
    LinearLayout [] linearLayouts = new LinearLayout[500];
    HashMap<Integer,LinearLayout> map = new HashMap<Integer,LinearLayout>();
    int i=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family_information);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final LinearLayout myLayout = (LinearLayout) findViewById(R.id.linearLayout3);
        authToken = getIntent().getStringExtra(WelcomeActivity.INTENT_KEY);

        brotherCount = (EditText) findViewById(R.id.brotherCount);
        sisterCount = (EditText) findViewById(R.id.sisterCount);
        familyValues = (EditText) findViewById(R.id.familyValues);
        familyType = (EditText) findViewById(R.id.familyType);
        familyStatus = (EditText) findViewById(R.id.familyStatus);
        familySummary = (EditText) findViewById(R.id.familySummary);
//        name = (EditText) findViewById(R.id.name);
//        age = (EditText) findViewById(R.id.age);
//        relation = (EditText) findViewById(R.id.relation);

        submit = (Button) findViewById(R.id.submit);
        add = (Button) findViewById(R.id.add);
        remove = (Button) findViewById(R.id.remove);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Load load = new Load();
                load.execute(brotherCount.getText().toString(), sisterCount.getText().toString(),
                        familyValues.getText().toString(), familyType.getText().toString(),
                        familyStatus.getText().toString(), familySummary.getText().toString());
                load.execute();
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                View hiddenInfo = getLayoutInflater().inflate(R.layout.family_attributes, myLayout, false);
//                myLayout.addView(hiddenInfo);
                linearLayouts[i] = new LinearLayout(FamilyInformation.this);
                linearLayouts[i].setOrientation(LinearLayout.VERTICAL);
                myLayout.addView(linearLayouts[i]);
                map.put(i,linearLayouts[i]);
                /*TextView t = new TextView(FamilyInformation.this);
                t.setText(getString(R.string.familyMemberDetails));
                linearLayouts[i].addView(t);*/

                EditText et = new EditText(FamilyInformation.this);
                et.setHint("Name");
                et.setId(0);
                linearLayouts[i].addView(et);

                EditText et1 = new EditText(FamilyInformation.this);
                et1.setHint("Age");
                linearLayouts[i].addView(et1);

                EditText et2 = new EditText(FamilyInformation.this);
                et2.setHint("Relation");
                linearLayouts[i].addView(et2);

                EditText et3 = new EditText(FamilyInformation.this);
                et3.setHint("Professional Summary");
                linearLayouts[i].addView(et3);

                EditText et4 = new EditText(FamilyInformation.this);
                et4.setHint("Marital Status");
                linearLayouts[i].addView(et4);

                EditText et5 = new EditText(FamilyInformation.this);
                et5.setHint("Living With Family");
                linearLayouts[i].addView(et5);

                Button removeE = new Button(FamilyInformation.this);
                removeE.setText("Remove");
                linearLayouts[i].addView(removeE);
                removeE.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ViewGroup myView = linearLayouts[i];
                        myView.removeAllViews();
                    }
                });
            }
        });

        /*remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup myView = (ViewGroup) findViewById(R.id.linearLayout3);
                for (int i = 0; i < myView.getChildCount(); i++) {
                    v = myView.getChildAt(i);
                    myView.removeView(v);
                }
            }
        });*/
    }


    class Load extends AsyncTask<Void, Void, String> {
        String brotherCount, sisterCount, familyValues, familyType, familyStatus, familySummary;

        public void execute(String brotherCount, String sisterCount, String familyValues, String familyType,
                            String familyStatus, String familySummary) {
            this.brotherCount = brotherCount;
            this.sisterCount = sisterCount;
            this.familyValues = familyValues;
            this.familyType = familyType;
            this.familyStatus = familyStatus;
            this.familySummary = familySummary;
        }

        @Override
        protected String doInBackground(Void... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader bufferedReader = null;
            String response = null;

            try {
                String baseURL = getString(R.string.addFamilyInfo);

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("brotherCount", brotherCount);
                jsonObject.put("sisterCount", sisterCount);
                jsonObject.put("familyValues", familyValues);
                jsonObject.put("familyType", familyType);
                jsonObject.put("familyStatus", familyStatus);
                jsonObject.put("familySummary", familySummary);

                URL url = new URL(baseURL);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.setRequestProperty("Content-type", "application/json");
                urlConnection.setRequestProperty("authToken", authToken);
                urlConnection.connect();

                OutputStream outputStream = urlConnection.getOutputStream();
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, "UTF-8");
                outputStreamWriter.write(jsonObject.toString());
                outputStreamWriter.flush();
                outputStreamWriter.close();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }

                bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                String line;
                while ((line = bufferedReader.readLine()) != null) {
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
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (final IOException e) {
                        Log.e("Placeholder fragment", "Error closing stream", e);
                    }
                }
            }
            return response;
        }
    }
}
