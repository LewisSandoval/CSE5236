package com.example.art.the_restaurant_guru;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by art on 4/2/2015.
 */
public class ShowRestaurant extends Activity {
    String path = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_restaurant);
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
