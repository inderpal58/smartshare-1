package com.mss.group3.smartshare.model;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.mss.group3.smartshare.R;
import com.mss.group3.smartshare.common.User;
import com.mss.group3.smartshare.interfaces.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class PostVehicle extends User implements ILocation, ISchedule, IVehicle {
    Integer vin;
    Integer plate_number;
    Double  price_km;
    Date StartDateTime;
    Date EndDateTime;
    String vehicle_type;
    String current_location;

    public String getCurrent_location() {
        return current_location;
    }

    public void setCurrent_location(String current_location) {
        this.current_location = current_location;
    }

    public Date getStartDateTime() {
        return StartDateTime;
    }

    public void setStartDateTime(String startDateTime) {
        StartDateTime = DateSetter(startDateTime);
    }

    public Date getEndDateTime() {
        return EndDateTime;
    }

    public void setEndDateTime(String endDateTime) {
        EndDateTime = DateSetter(endDateTime);
    }

    int vehicle_capacity;
    int vehicle_share_range;

    public Integer getVin() {
        return vin;
    }

    public Integer getPlate_number() {
        return plate_number;
    }

    public Double getPrice_km() {
        return price_km;
    }



    public String getVehicle_type() {
        return vehicle_type;
    }

    public int getVehicle_capacity() {
        return vehicle_capacity;
    }

    public int getVehicle_share_range() {
        return vehicle_share_range;
    }

    public void setVehicle_type(String vehicle_type) {
        this.vehicle_type = vehicle_type;
    }

    public void setVehicle_capacity(int vehicle_capacity) {
        this.vehicle_capacity = vehicle_capacity;
    }

    public void setVehicle_share_range(int vehicle_share_range) {
        this.vehicle_share_range = vehicle_share_range;
    }

    public void setVin(Integer vin) {
        this.vin = vin;
    }

    public void setPlate_number(Integer plate_number) {
        this.plate_number = plate_number;
    }

    public void setPrice_km(Double price_km) {
        this.price_km = price_km;
    }

    public Date DateSetter(String DateTime)
    {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        Date date = null;
        try
        {
             date = simpleDateFormat.parse(DateTime);
        }
        catch (ParseException ex)
        {
            System.out.println("Exception "+ex);
        }
        return  date;
    }

}