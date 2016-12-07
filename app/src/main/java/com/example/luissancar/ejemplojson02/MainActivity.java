package com.example.luissancar.ejemplojson02;

/*
http://www.androidhive.info/2012/01/android-json-parsing-tutorial/
 */


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private String TAG = MainActivity.class.getSimpleName();

    private ProgressDialog pDialog;
    private ListView lv;

    // URL to get contacts JSON
    //private static String url = "http://api.androidhive.info/contacts/";
    private static String url = "http://iesayala.ddns.net/json/json01.php";
   // ArrayList<HashMap<String, String>> contactList;
   ArrayList<HashMap<String, String>> guitarList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //contactList = new ArrayList<>();
        guitarList = new ArrayList<>();

        lv = (ListView) findViewById(R.id.list);

        new GetContacts().execute();
    }

    /**
     * Async task class to get json by making HTTP call
     */
    private class GetContacts extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    //JSONObject jsonObj = new JSONObject(jsonStr);
                    Log.e(TAG, "0");
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    Log.e(TAG, "1");

                    // Getting JSON Array node
                   // JSONArray contacts = jsonObj.getJSONArray("contacts");
                    JSONArray guitars = jsonObj.getJSONArray("guitarras");

                    Log.e(TAG, "2");

                    // looping through All Contacts
                    for (int i = 0; i < guitars.length(); i++) {
                        JSONObject c = guitars.getJSONObject(i);

                        String marca = c.getString("marca");
                        String modelo = c.getString("modelo");
                        String cantidad = c.getString("cantidad");


                        // tmp hash map for single contact
                        HashMap<String, String> guitar = new HashMap<>();

                        // adding each child node to HashMap key => value
                        guitar.put("marca", marca);
                        guitar.put("modelo", modelo);
                        guitar.put("cantidad", cantidad);

                        // adding contact to contact list
                        guitarList.add(guitar);
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */
            ListAdapter adapter = new SimpleAdapter(
                    MainActivity.this, guitarList,
                    R.layout.list_item, new String[]{"marca", "modelo",
                    "cantidad"}, new int[]{R.id.marca,
                    R.id.modelo, R.id.cantidad});

            lv.setAdapter(adapter);
        }

    }
}
