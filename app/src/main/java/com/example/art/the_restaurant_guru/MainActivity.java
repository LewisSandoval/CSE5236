package com.example.art.the_restaurant_guru;

/**
 * Created by art on 4/1/2015.
 */

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class MainActivity extends FragmentActivity implements LocationListener{

    GoogleMap mGoogleMap;

    String path = "";
    double mLatitude=40.0017311;
    double mLongitude=-83.0196284;
    String key ="AIzaSyAg3ptiV1rlkHrijfSa0WVMOt2UJUnF7Ng"; // This is the browser key
    private String category;
    private int price;
    private int range;
    private String [] placeArray;

    public void back_to_home_btn(View view)
    {
        Button button = (Button) view;
        ((Button) view).setText("getting home screen....");
        Intent i = new Intent(getApplicationContext(),HomeScreen.class);
        startActivity(i);
    }
    public void randomPlace(View view)
    {
        Random random = new Random();
        if(placeArray.length > 0) {
            ((Button) view).setText(placeArray[(random.nextInt(placeArray.length))]);
        } else {
            ((Button) view).setText("No places found :(");
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            category = extras.getString("category");
            price = extras.getInt("price");
            range = extras.getInt("range");
        }

        // Getting Google Play availability status
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());

        placeArray = new String[20];

        if(status!=ConnectionResult.SUCCESS){ // Google Play Services are not available

            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
            dialog.show();

        }else { // Google Play Services are available

            // Getting reference to the SupportMapFragment
            SupportMapFragment fragment = ( SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

            // Getting Google Map
            mGoogleMap = fragment.getMap();

            // Enabling MyLocation in Google Map
            mGoogleMap.setMyLocationEnabled(true);



            // Getting LocationManager object from System Service LOCATION_SERVICE
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

            // Creating a criteria object to retrieve provider
            Criteria criteria = new Criteria();

            // Getting the name of the best provider
            String provider = locationManager.getBestProvider(criteria, true);

            // Getting Current Location From GPS
            Location location = locationManager.getLastKnownLocation(provider);

            if(location!=null){
                onLocationChanged(location);
            }

            locationManager.requestLocationUpdates(provider, 20000, 0, this);

                    StringBuilder sb = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
                    sb.append("location="+mLatitude+","+mLongitude);
                    sb.append("&radius="+range);
                    sb.append("&types=restaurant|food|bakery|cafe");
                    if(price >= 0){ sb.append("&minprice="+price); sb.append("&maxprice=" + price);}
                    sb.append("&sensor=true");
                    if(category.compareTo("Any") != 0){ sb.append("&keyword=" + category);}
                    sb.append("&key="+key);


                    // Creating a new non-ui thread task to download Google place json data
                    PlacesTask placesTask = new PlacesTask();

                    // Invokes the "doInBackground()" method of the class PlaceTask
                    placesTask.execute(sb.toString());


                }

    }


    /** A method to download json data from url */
    private String downloadUrl(String strUrl) throws IOException{
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);


            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb  = new StringBuffer();

            String line = "";
            while( ( line = br.readLine())  != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
            Log.d("Error  downloading url", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }

        return data;
    }


    /** A class, to download Google Places */
    private class PlacesTask extends AsyncTask<String, Integer, String>{

        String data = null;

        // Invoked by execute() method of this object
        @Override
        protected String doInBackground(String... url) {
            try{
                data = downloadUrl(url[0]);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }

        // Executed after the complete execution of doInBackground() method
        @Override
        protected void onPostExecute(String result){
            ParserTask parserTask = new ParserTask();

            // Start parsing the Google places in JSON format
            // Invokes the "doInBackground()" method of the class ParseTask
            parserTask.execute(result);
        }

    }

    /** A class to parse the Google Places in JSON format */
    private class ParserTask extends AsyncTask<String, Integer, List<HashMap<String,String>>>{

        JSONObject jObject;

        // Invoked by execute() method of this object
        @Override
        protected List<HashMap<String,String>> doInBackground(String... jsonData) {

            List<HashMap<String, String>> places = null;
            PlaceJSONParser placeJsonParser = new PlaceJSONParser();

            try{
                jObject = new JSONObject(jsonData[0]);

                /** Getting the parsed data as a List construct */
                places = placeJsonParser.parse(jObject);

            }catch(Exception e){
                Log.d("Exception",e.toString());
            }
            return places;
        }

        // Executed after the complete execution of doInBackground() method
        @Override
        protected void onPostExecute(List<HashMap<String,String>> list){

            // Clears all the existing markers
            mGoogleMap.clear();

            for(int i=0;i<list.size();i++){

                // Creating a marker
                MarkerOptions markerOptions = new MarkerOptions();

                // Getting a place from the places list
                HashMap<String, String> hmPlace = list.get(i);


                // Getting latitude of the place
                double lat = Double.parseDouble(hmPlace.get("lat"));

                // Getting longitude of the place
                double lng = Double.parseDouble(hmPlace.get("lng"));

                // Getting name
                String name = hmPlace.get("place_name");

                // Getting vicinity
                String vicinity = hmPlace.get("vicinity");

                // Getting the type
                String type = hmPlace.get("type");

                // Getting the price_level
                String price = hmPlace.get("price");
                int price_level = Integer.parseInt(price);
                int user_price = Integer.parseInt(price);
                LatLng latLng = new LatLng(lat, lng);

                // Setting the position for the marker
                markerOptions.position(latLng);

                // Setting the title for the marker.
                //This will be displayed on taping the marker
                markerOptions.title(name + " : " + vicinity);

                if(price_level <= user_price) {
                    // Placing a marker on the touched position
                    Marker marker = mGoogleMap.addMarker(markerOptions);
                }
                 /*   mGoogleMap.setOnMarkerClickListener(
                            new GoogleMap.OnMarkerClickListener() {
                                @Override
                                public boolean onMarkerClick(Marker marker) {

                                    String id = marker.getId();
                                    String info = marker.getTitle();
                                    LatLng location = marker.getPosition();
                                    String hil = marker.getSnippet();
                                    Intent i = new Intent(getApplicationContext(),ShowRestaurant.class);
                                    i.putExtra("id", String.valueOf(marker.getId()));
                                    i.putExtra("id", String.valueOf(marker.getTitle()));
                                    i.putExtra("id", String.valueOf(marker.getPosition()));
                                    startActivity(i);
                                    return false;
                                }
                            }
                    );
                }*/
                // dump the JSONObject to logcat
                String a = hmPlace.get("temp");
                Log.d("the jsonObject!!!!!!",a);

                placeArray[i] = name;
            }

        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
      //  getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public void onLocationChanged(Location location) {
        mLatitude = location.getLatitude();
        mLongitude = location.getLongitude();
        LatLng latLng = new LatLng(mLatitude, mLongitude);

        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(12));

    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }
    public void saveData(Marker marker) throws IOException {
        String flag = "false";
        String data = "";
        Context context = getBaseContext();
        File mydir = context.getDir("Restaurants", Context.MODE_APPEND); //Creating an internal dir;
        if(!mydir.isDirectory())
        {
            mydir.mkdirs();
        }
        if(marker != null) {

            String id = marker.getId();
            String info = marker.getTitle();
            LatLng location = marker.getPosition();
            data = "["+id+","+info+","+location+"]";
            FileOutputStream out = null;

            try {
                File output = new File(mydir,id);
                out =  openFileOutput(id,context.MODE_APPEND);
                out.write(data.getBytes());
                flag = "true";
                path = id;
                Log.d("path!!!!!!!!!", output.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                if (out != null) {
                    out.close();
                }
            }
        }
        Log.d("Data was saved:",flag);

        readFile(path);
        //   d(path);
    }
    public void d(String theFile){
        File a = new File(theFile);
        a.delete();
    }
    public void readFile(String theFile){
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(openFileInput(theFile)));
            String inputStr;
            StringBuffer stringBuffer = new StringBuffer();
            while ((inputStr = in.readLine()) != null) {

                stringBuffer.append(inputStr);

            }
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}