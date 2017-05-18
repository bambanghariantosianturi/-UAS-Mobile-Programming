package com.bambang.uas_bambanghariantosianturi_41514110029;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {


    private ArrayList<location> contactList ;
    private boolean success = false;
    //private
    MyCustomAdapter dataAdapter;
    private ProgressDialog pDialog;

    //from php
    private static final String TAG_SUKSES = "sukses";
    private static final String TAG_MEMBER = "member";
    private static final String TAG_ROWS = "total_rows";
    private static final String TAG_ID = "id";
    private static final String TAG_LOCATION_NAME = "location_name";
    private static final String TAG_ADDRESS = "address";
    private static final String TAG_LONGITUDE = "longitude";
    private static final String TAG_LATITUDE = "latitude";



    // JSONArray member
    JSONArray member = null;

    JSONParser jParser = new JSONParser();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        contactList = new ArrayList<location>();

        new AmbilDataJson().execute();
        //create an ArrayAdaptar from the String Array
        dataAdapter = new MyCustomAdapter(this,
                R.layout.activity_location, contactList);
        ListView listView = (ListView) findViewById(R.id.contactlistView);
        // Assign adapter to ListView
        listView.setAdapter(dataAdapter);



    }

    private void displayContact(){
        //if the request was successful then notify the adapter to display the data
        if(success){
            dataAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    //custom array adapter to display our custom row layout for the listview
    class MyCustomAdapter extends ArrayAdapter<location> {

        public MyCustomAdapter(Context context, int textViewResourceId,
                               ArrayList<location> contactList) {
            super(context, textViewResourceId, contactList);
        }


        private class ViewHolder {
            TextView id;
            TextView location_name;
            TextView address;
            TextView longitude;
            TextView latitude;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;
            if (convertView == null) {

                LayoutInflater vi = (LayoutInflater)getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.activity_location, null);

                holder = new ViewHolder();
                holder.id = (TextView) convertView.findViewById(R.id.id);
                holder.location_name = (TextView) convertView.findViewById(R.id.location_name);
                holder.address = (TextView) convertView.findViewById(R.id.address);
                holder.longitude = (TextView) convertView.findViewById(R.id.longitude);
                holder.latitude = (TextView) convertView.findViewById(R.id.latitude);

                convertView.setTag(holder);

            }
            else {
                holder = (ViewHolder) convertView.getTag();
            }

            location contact = contactList.get(position);
            holder.id.setText(contact.getId());
            holder.location_name.setText(contact.getLocation_name());
            holder.address.setText(contact.getAddress());
            holder.longitude.setText(contact.getLongitude());
            holder.latitude.setText(contact.getLatitude());


            return convertView;

        }



    }


    class AmbilDataJson extends AsyncTask<String, String, String> {

        // inisialisasi url contact.php
        private String url="http://abdkhaliq.com/mercu/location.php";

        int total;

        /**
         * sebelum memulai background thread tampilkan Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Read data of contact...Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * mengambil semua data JSON contact dari url
         * dan memasukan ke dalam list contact
         * dilakukan secara background
         * */
        protected String doInBackground(String... args) {
            // membangun Parameter


            List<NameValuePair> params = new ArrayList<NameValuePair>();

            //params.add(new BasicNameValuePair("param1", "nilai1"));
            //params.add(new BasicNameValuePair("param2", "nilai2"));
            params.add(new BasicNameValuePair("contactid", "1"));

            // ambil JSON string dari URL
            JSONObject json = jParser.makeHttpRequest(url, "POST", params);


            // cek log cat untuk JSON reponse
            Log.d("contact: ", json.toString());




            try {
                // mengecek untuk TAG SUKSES
                int sukses = json.getInt(TAG_SUKSES);
                total = json.getInt(TAG_ROWS);

                Log.d("total Contact: ",""+total);


                if (sukses == 1) {
                    Log.d("Contact: ",TAG_SUKSES);
                    // data ditemukan
                    // mengambil  Array dari member
                    member = json.getJSONArray(TAG_MEMBER);

                    // looping data semua member/anggota
                    for (int i = 0; i < member.length(); i++) {
                        JSONObject c = member.getJSONObject(i);

                        // tempatkan setiap item json di variabel


                        String id= c.getString(TAG_ID);
                        String location_name = c.getString(TAG_LOCATION_NAME);
                        String address = c.getString(TAG_ADDRESS);
                        String longitude = c.getString(TAG_LONGITUDE);
                        String latitude = c.getString(TAG_LATITUDE);


                        contactList.add(new location(id,location_name,address,longitude,latitude));

                    }


                }
                else {
                    Toast.makeText(getApplicationContext(), "No data", Toast.LENGTH_LONG).show();

                }
            } catch (JSONException e) {
                e.printStackTrace();

            }

            return null;
        }

        /**
         * setelah menyelesaikan background task hilangkan the progress dialog
         * resfresh List view setelah data JSON diambil
         * **/
        protected void onPostExecute(String file_url) {
            // hilangkan dialog setelah mendapatkan semua data member
            pDialog.dismiss();
            // update UI dari Background Thread
            runOnUiThread(new
            () {
                public void run() {
                    /**
                     * update hasil parsing JSON ke ListView
                     * */

                    if (total>1)
                    {

                        success=true;
                        displayContact();
                        Toast.makeText(getApplicationContext(), "Successfully download JSON "+total+" Record(s)", Toast.LENGTH_LONG).show();



                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Unsuccessfully download", Toast.LENGTH_LONG).show();

                    }




                }
            });

        }

    }//endjson

}