package com.mss.group3.smartshare.controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.mss.group3.smartshare.R;
import com.mss.group3.smartshare.common.SaveSharedPreference;
import com.mss.group3.smartshare.common.User;
import com.mss.group3.smartshare.model.Login;
import com.mss.group3.smartshare.model.VehicleAdaptor;
import com.mss.group3.smartshare.model.VehicleDataStore;
import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

import static com.mss.group3.smartshare.common.InputValidation.loginInputValidation;

/**
 * Created on 2016-02-20.
 * The login controller handle request from login, signup and forgot password view
 */
public class LoginController extends Activity {

    public Login login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //the first screen to be initialized is login
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        if(SaveSharedPreference.getUserName(LoginController.this).length() != 0)
        {

            ((EditText) findViewById(R.id.userName)).setText(SaveSharedPreference.getUserName(LoginController.this));
            ((EditText) findViewById(R.id.userPassword)).setText(SaveSharedPreference.getPassword(LoginController.this));

            //login screen initialization
            Intent myIntent = new Intent(LoginController.this, UserTypeController.class);
            startActivity(myIntent);
        }


        //initialize login
        login = new Login();
    }

    public void loginButtonClick(View view ) {

        String userName=((EditText) findViewById(R.id.userName)).getText().toString().toLowerCase();
        login.setUserName(userName);
        String userPassword= ((EditText) findViewById(R.id.userPassword)).getText().toString();
        login.setUserPassword(userPassword);

        if(loginInputValidation( userName,  userPassword, this)){
            return;
        }

        ParseUser.logInInBackground(login.getUserName(), login.getUserPassword(), new LogInCallback() {
            public void done(ParseUser user, ParseException e) {
                if (user != null) {
                    //login screen initialization
                    if (user.getBoolean("emailVerified") == true) {

                        SaveSharedPreference.setUserName(LoginController.this, login.getUserName());
                    SaveSharedPreference.setPassword(LoginController.this, login.getUserPassword());
                    Toast.makeText(getApplicationContext(), "Wecome back !", Toast.LENGTH_SHORT).show();

                    User.vehicle_list.clear();

                    ParseQuery<ParseObject> query_shares = new ParseQuery<ParseObject>("VehicleTable");
                    query_shares.whereEqualTo("Owner_email", login.getUserName());
                    query_shares.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> list, ParseException e) {
                            for (ParseObject p : list) {
                                User.addVehicle(p.getString("Plate_number"));
                            }
                        }
                    });

                    if(!user.getBoolean("Credit_created")) {

                        ParseObject credit_object = new ParseObject("UserCredit");
                        credit_object.put("user_email", login.getUserName());
                        credit_object.put("user_credit", 1000);
                        try {
                            credit_object.save();
                        } catch (ParseException eux) {
                            eux.printStackTrace();
                        }

                        user.put("Credit_created",true);
                        try {
                            user.save();
                        } catch (ParseException e1) {
                            e1.printStackTrace();
                        }


                    }

                    Intent myIntent = new Intent(LoginController.this, UserTypeController.class);

                    startActivity(myIntent);

                    }

                    else if(user.getBoolean("emailVerified") == false){

                        Toast.makeText(getApplicationContext(), "Please verify your email!", Toast.LENGTH_SHORT).show();

                    }
                } else {
                    SaveSharedPreference.setUserName(LoginController.this, "");
                    SaveSharedPreference.setPassword(LoginController.this, "");
                    Toast.makeText(getApplicationContext(), "Please enter correct information", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    @Override
    public void onBackPressed() {
        moveTaskToBack(false);
      //  System.exit(0);
    }

    public void signUpButtonClick(View view ) {

        //login screen initialization
        Intent myIntent = new Intent(LoginController.this, UserRegistrationController.class);
        startActivity(myIntent);
    }

    public void recoveryButtonClick(View view ) {
        //recovery screen initialization
        Intent myIntent = new Intent(LoginController.this, RecoveryController.class);
        startActivity(myIntent);

    }

}
