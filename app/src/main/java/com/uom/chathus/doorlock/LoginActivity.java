package com.uom.chathus.doorlock;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    String TAG = "logintesting";
    EditText txtUserName;
    EditText txtPassword;
    EditText txtItemNumber;
    EditText txtItemPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtUserName = findViewById(R.id.txtUserName);
        txtPassword = findViewById(R.id.txtPass);
        txtItemNumber = findViewById(R.id.txtItemNumber);
        txtItemPassword = findViewById(R.id.txtItemPassword);
    }

    public void Login(View view) {

        //initialize text fields
        final String userName = txtUserName.getText().toString();
        final String password = txtPassword.getText().toString();
        final String itemNumber = txtItemNumber.getText().toString();
        final String itemPassword = txtItemPassword.getText().toString();


        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference().child(itemNumber + "/psw");
        Log.d(TAG, "instance and ref retrieved");
        try {

            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String psw = dataSnapshot.getValue(String.class);
                    if (psw!=null) {

                        if (psw.equals(itemPassword)) {
                            final DatabaseReference myRef = database.getReference().child(itemNumber + "/userList");
                            Log.d(TAG, "instance and ref retrieved");

                            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    Iterable<DataSnapshot> users = dataSnapshot.getChildren();
                                    if(users!=null) {
                                        boolean logged = false;
                                        for (DataSnapshot user : users) {
                                            String uname = user.getKey().toString();
                                            String psw = user.getValue().toString();

                                            if (uname.equals(userName) && psw.equals(password)) {
                                                logged = true;
                                                Intent mainPage = new Intent(LoginActivity.this, MainActivity.class);
                                                mainPage.putExtra("userName", userName);
                                                mainPage.putExtra("itemNumber", itemNumber);
                                                startActivity(mainPage);
                                            }
                                        }
                                        if(!logged){
                                            // show a toast to user
                                            CharSequence text = "Wrong Credentials, Please Try again!";
                                            int duration = Toast.LENGTH_SHORT;
                                            Toast toast = Toast.makeText(getApplicationContext(), text, duration);
                                            toast.show();
                                        }
                                    }
                                    else{
                                        // show a toast to user
                                        CharSequence text = "Wrong Credentials, Please Try again!";
                                        int duration = Toast.LENGTH_SHORT;
                                        Toast toast = Toast.makeText(getApplicationContext(), text, duration);
                                        toast.show();
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError error) {
                                    // Failed to read value, log details.
                                    Log.w(TAG, "Failed to read value.", error.toException());

                                    // show a toast to user
                                    CharSequence text = "Login Error, Please Try again!";
                                    int duration = Toast.LENGTH_SHORT;
                                    Toast toast = Toast.makeText(getApplicationContext(), text, duration);
                                    toast.show();
                                }
                            });
                        }
                        else {
//                    // show a toast to user
                            CharSequence text = "Wrong Credentials, Please Try again!";
                            int duration = Toast.LENGTH_SHORT;
                            Toast toast = Toast.makeText(getApplicationContext(), text, duration);
                            toast.show();
                        }
                    } else {
//                    // show a toast to user
                        CharSequence text = "Wrong Credentials, Please Try again!";
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(getApplicationContext(), text, duration);
                        toast.show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value, log details.
                    Log.w(TAG, "Failed to read value.", error.toException());

                    // show a toast to user
                    CharSequence text = "Login Error, Please Try again!";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(getApplicationContext(), text, duration);
                    toast.show();
                }
            });
        } catch (Exception ex) {
            // show a toast to user
            CharSequence text = "Invalid credentials or connectivity fail, Please Try again!";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(getApplicationContext(), text, duration);
            toast.show();
        }
    }

}
