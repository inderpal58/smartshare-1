package com.mss.group3.smartshare.controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.mss.group3.smartshare.R;
import com.mss.group3.smartshare.model.UserSingleton;
import com.mss.group3.smartshare.model.VehicleAdaptor;
import com.mss.group3.smartshare.model.VehicleDataStore;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OwnerController extends Activity {

    public static final String VEHICLE_ID = "VehicleId";
    private ListView lvProduct;
    private VehicleAdaptor adapter;
    private List<VehicleDataStore> mProductList;
    ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("VehicleTable");
    static int counter=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.owner);
        lvProduct = (ListView)findViewById(R.id.listView);
        mProductList = new ArrayList<>();

        if(counter==0)  getData();
    }

    public void getData() {
        query = new ParseQuery<ParseObject>("VehicleTable");
        mProductList.clear();
        UserSingleton userSingleton = UserSingleton.getInstance();
        query.whereEqualTo("Owner_email", userSingleton.emailAddress);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                for (ParseObject p : list) {
                    mProductList.add(new VehicleDataStore(
                            p.getObjectId(),
                            p.getString("Vehicle_type"),
                            p.getInt("Capacity"),
                            p.getDate("FromDate"),
                            p.getDate("ToDate"),
                            p.getDouble("Price_km") ));
                }

                adapter = new VehicleAdaptor(getApplicationContext(), mProductList,1);
                lvProduct.setAdapter(adapter);

                lvProduct.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent myIntent = new Intent(OwnerController.this, UpdateVehicleController.class);
                        String VID = (String) view.getTag();
                        myIntent.putExtra(VEHICLE_ID, VID);
                        startActivity(myIntent);
                    }
                });
                query = null;
            }
        });
    }

    @Override
    protected void onResume() {
        counter++;
        super.onResume();
        if(counter>1) {
            getData();
        }
    }

    public void postVehicle(View view) {
        Intent screenfour = new Intent(OwnerController.this,PostVehicleController.class);
        startActivity(screenfour);
    }
}
