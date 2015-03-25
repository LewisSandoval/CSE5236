package com.example.art.the_restaurant_guru;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

public class HomeScreen extends Activity {
    private Spinner first_spinner;
    private Button submit_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        first_spinner = (Spinner) findViewById(R.id.first_spinner);
        List<String> list = new ArrayList<String>();
        list.add("Android");
        list.add("Java");
        list.add("Spinner Data");
        list.add("Spinner Adapter");
        list.add("Spinner Example");
        for(int i=0;i<10;i++)
        {
            list.add(i+"");
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item,list);

        dataAdapter.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);

        first_spinner.setAdapter(dataAdapter);

        // Spinner item selection Listener
        addListenerOnSpinnerItemSelection();

        // Button click Listener
        addListenerOnButton();

    }

    // Add spinner data

    public void addListenerOnSpinnerItemSelection(){

        first_spinner.setOnItemSelectedListener(new CustomOnItemSelectedListener());

    }

    //get the selected dropdown list value

    public void addListenerOnButton() {

        first_spinner = (Spinner) findViewById(R.id.first_spinner);

        submit_btn = (Button) findViewById(R.id.submit_btn);

        submit_btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                Toast.makeText(HomeScreen.this,
                        "On Button Click : " +
                                "\n" + String.valueOf(first_spinner.getSelectedItem()) ,
                        Toast.LENGTH_LONG).show();
            }

        });

    }

    public void show_map_btn(View view)
    {
        Button button = (Button) view;
        ((Button) view).setText("getting map....");
        Intent i = new Intent(getApplicationContext(),MapsActivity.class);
        startActivity(i);
    }

}