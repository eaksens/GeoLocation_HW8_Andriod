package com.example.geocalculatorapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.parceler.Parcels;

import java.util.Arrays;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private TextView origLocationField;
    private TextView dstLocationField;
    private TextView datePickerField;
    private GeoCalcLocation origLocation;
    private GeoCalcLocation dstLocation;
    public static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    private int SET_LOCATION = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent();
            Parcelable[] locations = {Parcels.wrap(origLocation), Parcels.wrap(dstLocation)} ;
            intent.putExtra("locations", locations);
            setResult(MainActivity.PLACES_RESULT, intent);
            finish();
        });


        origLocationField = findViewById(R.id.originPlace);
        dstLocationField = findViewById(R.id.dstPlace);
        datePickerField = findViewById(R.id.datePicker);

        origLocationField.setOnClickListener(v -> {
            SET_LOCATION = 1;
            locationsPressed();

        });
        dstLocationField.setOnClickListener(v -> {
            SET_LOCATION = 2;
            locationsPressed();
        });

        DateTime now = DateTime.now();
        DateTimeFormatter fmt = DateTimeFormat.forPattern("MMMM dd, YYYY");
        String time = fmt.print(now);
        datePickerField.setText(time);
        origLocation = new GeoCalcLocation();
        dstLocation = new GeoCalcLocation();
    }

    private void locationsPressed() {
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.LAT_LNG, Place.Field.NAME);
        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields).build(this);
        startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if(resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                TextView view;
                GeoCalcLocation location;
                if(SET_LOCATION == 1) {
                    view = origLocationField;
                    location = origLocation;
                } else {
                    location = dstLocation;
                    view = dstLocationField;
                }
                view.setText(place.getName());
                location.name = place.getName();
                location.lat = place.getLatLng().latitude;
                location.lng = place.getLatLng().longitude;
                location.placeId = place.getId();
            }
        }
    }
}
