package com.mss.group3.smartshare.controller;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.mss.group3.smartshare.R;
import com.mss.group3.smartshare.model.FindVehicle;
import com.mss.group3.smartshare.model.FindVehicleList;
import com.mss.group3.smartshare.model.FindVehiclelistSingleton;
import com.mss.group3.smartshare.utility.LocationServices;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by inder on 2016-02-20.
 * This controller will trigger the find vehicle layout
 * The purpose is to validate information and find matching vehicle as per schedule
 */
public class FindVehicleController extends Activity {

    private Calendar calendar = Calendar.getInstance();
    private FindVehicle findVehicle;
    private String EnddateString;
    private int month1;
    private int day1;
    private int year1;
    private int hour1;
    private int min1;
    private Geocoder geoCoder;
    private ArrayAdapter<CharSequence> adaptorVehicleCapacity;
    private Spinner spinnerVehicleCapacity;
    private int vehicle_capacity;
    private FindVehiclelistSingleton objVehicleSingleton = FindVehiclelistSingleton.getInstance();
    final long ONE_MINUTE_IN_MILLIS = 60000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.findvehicle);
        findVehicle = new FindVehicle();
        ((TextView) findViewById(R.id.wrongAddressMessage)).setVisibility(View.INVISIBLE);
        ((Button) findViewById(R.id.findVehicleProceedButton)).setVisibility(View.INVISIBLE);
        geoCoder = new Geocoder(this, Locale.getDefault());
        EditText arrivalAddressEvent = (EditText) findViewById(R.id.arrivalAddressPostalCodeText);

        spinnerVehicleCapacity = (Spinner) findViewById(R.id.filtervehiclecapacity);
        adaptorVehicleCapacity = ArrayAdapter.createFromResource(this, R.array.vehicle_capacity, android.R.layout.simple_spinner_item);
        adaptorVehicleCapacity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerVehicleCapacity.setAdapter(adaptorVehicleCapacity);
        spinnerVehicleCapacity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                vehicle_capacity = Integer.parseInt((String) parent.getItemAtPosition(pos));
                objVehicleSingleton.capacity = vehicle_capacity;
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        arrivalAddressEvent.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    findVehicle.setDepartureAddressLineOneText(((EditText) findViewById(R.id.departureAddressLineOneText)).getText().toString());
                    findVehicle.setDepartureAddressCityNameText(((EditText) findViewById(R.id.departureAddressCityNameText)).getText().toString());
                    findVehicle.setDepartureAddressCountryNameText(((EditText) findViewById(R.id.departureAddressCountryNameText)).getText().toString());
                    findVehicle.setDepartureAddressPostalCodeText(((EditText) findViewById(R.id.departureAddressPostalCodeText)).getText().toString());

                    findVehicle.setArrivalAddressLineOneText(((EditText) findViewById(R.id.arrivalAddressLineOneText)).getText().toString());
                    findVehicle.setArrivalAddressCityNameText(((EditText) findViewById(R.id.arrivalAddressCityNameText)).getText().toString());
                    findVehicle.setArrivalAddressCountryNameText(((EditText) findViewById(R.id.arrivalAddressCountryNameText)).getText().toString());
                    findVehicle.setArrivalAddressPostalCodeText(((EditText) findViewById(R.id.arrivalAddressPostalCodeText)).getText().toString());
                    findVehicle.findDistanceAndDuration(geoCoder);
                    ((TextView) findViewById(R.id.travellingDistance)).setText("Travelling Distance  " + String.format("%1.2f",(findVehicle.getDistnaceInMeters()/1000)) +" Km");
                    ((TextView) findViewById(R.id.travellingTime)).setText("Travelling Time  " + String.format("%1.2f",(findVehicle.getTimeInMinutes()/60)) + " Hours");
                    if (findVehicle.getDistnaceInMeters() <= 0 || findVehicle.getTimeInMinutes() <= 0) {
                        ((Button) findViewById(R.id.findVehicleProceedButton)).setVisibility(View.INVISIBLE);
                        ((TextView) findViewById(R.id.wrongAddressMessage)).setVisibility(View.VISIBLE);
                    } else {
                        ((Button) findViewById(R.id.findVehicleProceedButton)).setVisibility(View.VISIBLE);
                        ((TextView) findViewById(R.id.wrongAddressMessage)).setVisibility(View.INVISIBLE);
                    }
                }
            }
        });
    }

    //set date and time for to and from button clicks
    public void setDateTime(View v) {

        calendar = Calendar.getInstance();
        switch (v.getId()) {
            /*case R.id.getToDateButton: {
                new DatePickerDialog(this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view,
                                                  int year, int month, int day) {
                                EnddateString = (month + 1) + "/" + day + "/" + year;
                                month1 = month;
                                day1 = day;
                                year1 = year;
                                new TimePickerDialog(FindVehicleController.this,
                                        new TimePickerDialog.OnTimeSetListener() {
                                            @Override
                                            public void onTimeSet(TimePicker view,
                                                                  int hour, int min) {
                                                EnddateString += " " + hour + ":" + min;
                                                ((TextView) findViewById(R.id.getToDate)).setText(EnddateString);    // Text View
                                                Calendar C = new GregorianCalendar();
                                                C.set(year1, month1, day1, hour, min, 0);
                                                findVehicle.setArrivalDate(C);
                                            }
                                        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),
                                        android.text.format.DateFormat.is24HourFormat(FindVehicleController.this)).show();
                            }
                        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
                break;
            }
            */case R.id.getFromDateButton: {

                new DatePickerDialog(this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view,
                                                  int year, int month, int day) {
                                EnddateString = (month + 1) + "/" + day + "/" + year;
                                month1 = month;
                                day1 = day;
                                year1 = year;
                                new TimePickerDialog(FindVehicleController.this,
                                        new TimePickerDialog.OnTimeSetListener() {
                                            @Override
                                            public void onTimeSet(TimePicker view,
                                                                  int hour, int min) {
                                                EnddateString += " " + hour + ":" + min;
                                                hour1 = hour;
                                                min1 = min;
                                                ((TextView) findViewById(R.id.getFromDate)).setText(EnddateString);    // Text View
                                                Calendar C = new GregorianCalendar();
                                                C.set(year1, month1, day1, hour, min, 0);
                                                findVehicle.setDepartureDate(C);

                                                String pp = C.getTime().toString();

                                                //set to date time
                                                double time = findVehicle.getTimeInMinutes();

                                                Calendar C1 = new GregorianCalendar();
                                                C1.set(year1, month1, day1, hour, min, 0);
                                               C1.add(Calendar.MINUTE,(int)time);

                                                ((TextView) findViewById(R.id.getToDate)).setText(C1.getTime().toString());
                                                findVehicle.setArrivalDate(C1);

                                                String pp1 = C1.getTime().toString();



                                            }
                                        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),
                                        android.text.format.DateFormat.is24HourFormat(FindVehicleController.this)).show();






                            }
                        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
                break;
            }
        }
    }

    //refresh current location
    public void getCurrentLocationButtonClick(View v) {
        String currentAddress = null;
        getLocation(currentAddress);
    }

    //find list of vehicles
    public void processFindVehicleButtonClick(View v) {

        objVehicleSingleton.arrivalDate = findVehicle.getArrivalDate();
        objVehicleSingleton.departureDate = findVehicle.getDepartureDate();
        objVehicleSingleton.departureAddressPostalCodeText = findVehicle.getDepartureAddressCityNameText() +","+
                findVehicle.getDepartureAddressCountryNameText() +","+
                findVehicle.getDepartureAddressPostalCodeText();
        objVehicleSingleton.arrivalAddressDepartureCode = findVehicle.getArrivalAddressCountryNameText() +","+
                findVehicle.getArrivalAddressCityNameText() +","+
                findVehicle.getArrivalAddressPostalCodeText();

        //Move To Next Screen
        Intent nextScreen = new Intent(FindVehicleController.this, FindVehicleList.class);
        startActivity(nextScreen);
    }

    //get current location
    private void getLocation(String address) {
        LocationServices mLocationServices = new LocationServices(this);
        mLocationServices.getLocation();
        if (mLocationServices.isLocationAvailable == false) {
            //try again
            Toast.makeText(getApplicationContext(), "Your location is not available, " +
                    "Enter address manually or Press refresh after enabling location.", Toast.LENGTH_SHORT).show();
            return;
        } else {
            // Getting location co-ordinates
            double latitude = mLocationServices.getLatitude();
            double longitude = mLocationServices.getLongitude();
            address = mLocationServices.getLocationAddress();
            try {
                ((EditText) findViewById(R.id.departureAddressLineOneText)).setText(mLocationServices.getLineOneAddress());
                ((EditText) findViewById(R.id.departureAddressCityNameText)).setText(mLocationServices.getLocationCity());
                ((EditText) findViewById(R.id.departureAddressCountryNameText)).setText(mLocationServices.getLocationCountry());
                ((EditText) findViewById(R.id.departureAddressPostalCodeText)).setText(mLocationServices.getPostalCode());

            } catch (Exception e) {

            }
        }
        //close the gps
        mLocationServices.closeGPS();
    }
}
