package com.example.geocalculatorapp;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import com.example.geocalculatorapp.dummy.HistoryContent;
import org.joda.time.DateTime;


public class MainActivity extends AppCompatActivity {
    //identifier when the picker selected back
    public static final int UNITS_SELECTION = 1;
    public static final int HISTORY_RESULT = 2;

    //unit variables: initialize
    private String disUnit = "Miles";
    private String bearingUnit = "degrees";

    //Unit Conversion: 1 kilometer = 0.621371 miles. 1 degree = 17.777777777778 mil.
    final Double kmToMiles = 0.621371;
    final Double degreeToMils = 17.777777777778;

    //reference to the UI use the findViewById
    private EditText lat1 = null;
    private EditText long1 = null;
    private EditText lat2 = null;
    private EditText long2 = null;
    private TextView calDistance = null;
    private TextView calBearing = null;

    //on create Android Life Cycle -main method when launching the application
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity); //where to get UI from -in this case, it's under layout folder on main_activity.xml
        //toolbar
        Toolbar myToolBar = findViewById(R.id.mainToolbar);
        setSupportActionBar(myToolBar);


        Button calBtn = findViewById(R.id.calculate);
        Button clearBtn = findViewById(R.id.clearBtn);

        lat1 = (EditText) findViewById(R.id.lat1);
        lat2 = (EditText) findViewById(R.id.lat2);
        long1 = (EditText) findViewById(R.id.long1);
        long2 = (EditText) findViewById(R.id.long2);
        calDistance = (TextView) findViewById(R.id.calDistance);
        calBearing = (TextView) findViewById(R.id.calBearing);

        //Press Clear button the all values are clear
        clearBtn.setOnClickListener(v -> {
            clearValues();
        });

        //Press Calculate button
        //listener set on click of your calculation button
        calBtn.setOnClickListener(v -> {
            HistoryContent.HistoryItem item = new
                    HistoryContent.HistoryItem(lat1.getText().toString(),
                    long1.getText().toString(), lat2.getText().toString(), long2.getText().toString(), DateTime.now());
            HistoryContent.addItem(item);
            computeValues();
            // remember the calculation.
        });
    }


    //clear button
    private  void clearValues() {
        lat1.setText("");
        long1.setText("");
        lat2.setText("");
        long2.setText("");
        calDistance.setText("");
        calBearing.setText("");

    }

    //compute the values
    private void computeValues() {
        try {
            Double lat1value = Double.parseDouble(lat1.getText().toString());
            Double long1value = Double.parseDouble(long1.getText().toString());
            Double lat2value = Double.parseDouble(lat2.getText().toString());
            Double long2value = Double.parseDouble(long2.getText().toString());

            Location startPoint = new Location("a");
            Location endPoint = new Location("b");

            startPoint.setLatitude(lat1value);
            startPoint.setLongitude(long1value);
            endPoint.setLatitude(lat2value);
            endPoint.setLongitude(long2value);

            double distance = (startPoint.distanceTo(endPoint))/1000;
            double bearing = (startPoint.bearingTo(endPoint));

            if (disUnit.equals("Miles")) {
                distance *= kmToMiles;
            }
            if (bearingUnit.equals("Mils")) {
                bearing *= degreeToMils;
            }

            String distanceString = String.format("%.2f "+ disUnit, distance);
            String bearingString = String.format("%.2f "+ bearingUnit, bearing);
            calDistance.setText(distanceString);
            calBearing.setText(bearingString);
            hideKeyboard();

        } catch (Exception e) {
            return;
        }
    }
    private void hideKeyboard()
    {
        // Check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            //this.getSystemService(Context.INPUT_METHOD_SERVICE);
            InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    //setup the setting menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        //calling the setting menu
        MenuItem settingMenuItem = menu.findItem(R.id.actionSetting);
        MenuItem historyMenuItem = menu.findItem(R.id.actionHistory);

        //when click on setting menu
        settingMenuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                //navigate to setting page
                Intent intent = new Intent(MainActivity.this, unitSettingActivity.class);
                //override with onActivityResult for the result code
                startActivityForResult(intent,UNITS_SELECTION);
                return true;
            }
        });

        //when click on History menu
        historyMenuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
                //override with onActivityResult for the result code
                startActivityForResult(intent, HISTORY_RESULT );
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //passing on the selection result from the unitSettingActivity page to MainActivity page
        if(resultCode == UNITS_SELECTION) {
            String[] units = data.getStringArrayExtra("units");
            disUnit = units[0];
            bearingUnit = units[1];
            computeValues();
        }
        //passing on the history result from the HistoryActivity to MainPage
        else if(requestCode == HISTORY_RESULT){
            String[] vals = data.getStringArrayExtra("item");
            this.lat1.setText(vals[0]);
            this.long1.setText(vals[1]);
            this.lat2.setText(vals[2]);
            this.long2.setText(vals[3]);
            this.computeValues();
        }
    }


}
