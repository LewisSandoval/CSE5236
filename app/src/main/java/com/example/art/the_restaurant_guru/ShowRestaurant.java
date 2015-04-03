package com.example.art.the_restaurant_guru;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

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
    private String path = "ratings";
    File mydir;
    private String id = "";
    private String name = "";
    private String address = "";
    private String location = "";
    private float rating = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_restaurant);
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            id = extras.getString("id");
            name = extras.getString("name");
            address = extras.getString("address");
            location = extras.getString("location");
        }
        TextView restName = (TextView) findViewById(R.id.restaurant_name);
        TextView restAdd = (TextView) findViewById(R.id.restaurant_address);
        restName.setText(name);
        restAdd.setText(address);
    }

    public void save_btn(View view){
        RatingBar mBar = (RatingBar) findViewById(R.id.show_rating_bar);
        rating = mBar.getRating();
        try {
            saveData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void saveData() throws IOException {
        String flag = "false";
        String data = "";
        Context context = getBaseContext();
        mydir = context.getDir("Restaurants", Context.MODE_APPEND); //Creating an internal dir;
        if(!mydir.isDirectory())
        {
            mydir.mkdirs();
        }

            data = "["+id+","+name+","+address+","+location+","+rating+"]";
            FileOutputStream out = null;
            try {
                File output = new File(mydir,path);
                Log.d("path output",output.toString());
                out =  openFileOutput(path,context.MODE_APPEND);
                out.write(data.getBytes());
                flag = "true";
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                if (out != null) {
                    out.close();
                }
            }
        Log.d("Data was saved:",flag);
        Log.d("here is the data!!!!!!",data);

        Log.d("path out",out.toString());
    }
    public void deleteFile(View view){
       Context context =getBaseContext();
        boolean flag = context.deleteFile(path);

        if(flag) {
            Toast.makeText(ShowRestaurant.this, "Saved Data was Deleted!", Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(ShowRestaurant.this, "Saved Data was Not Deleted!", Toast.LENGTH_LONG).show();
        }
    }
    public void showSavedData(View view){
        readFile();
    }

    public void readFile(){
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(openFileInput(path)));
            String inputStr;
            StringBuffer stringBuffer = new StringBuffer();
            while ((inputStr = in.readLine()) != null) {
                stringBuffer.append(inputStr);
            }
            in.close();
            TextView show_data = (TextView) findViewById(R.id.showSaveData);
            show_data.setText(stringBuffer);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
