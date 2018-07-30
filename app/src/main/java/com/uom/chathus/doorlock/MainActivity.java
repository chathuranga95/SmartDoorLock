package com.uom.chathus.doorlock;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.TooltipCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import utill.DoorLock;

public class MainActivity extends AppCompatActivity {

    DoorLock doorLock;
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    private String TAG = "mainacttest";
    ImageButton btnUnlock;
    ImageView imgStatus;
    ImageButton addPeople;

    String userName;
    String itemNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Smart Door Lock");

        btnUnlock = findViewById(R.id.btnUnlock);
        imgStatus = findViewById(R.id.imgStatus);
        addPeople = findViewById(R.id.btnAdd);
        TooltipCompat.setTooltipText(addPeople,"Add or remove people");
        userName = getIntent().getStringExtra("userName");
        itemNumber = getIntent().getStringExtra("itemNumber");
        setStatus();
    }

//    public void turnOn(View view) {
//        myRef.setValue("1");
//    }
//
//    public void turnOff(View view) {
//        myRef.setValue("0");
//    }

    public void unlock(View view) {

        final String TAG = "isuserhasaccesscheck";

        //write the relevant data slot
        DatabaseReference motorRef = FirebaseDatabase.getInstance().getReference().child(itemNumber+"/motor");
        motorRef.setValue(true);

        Log.d(TAG,"checkpoint 1");
        btnUnlock.setEnabled(false);
        imgStatus.setImageDrawable(getResources().getDrawable(R.drawable.unlocked_open_it));

/*


        // get database instance and slot
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference().child("Item001/userList");
        Log.d(TAG, "instance and ref retrieved");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> users = dataSnapshot.getChildren();
                for (DataSnapshot user : users) {
                    if(user.getKey().equals(userName)){
                        //then do the shit

                        //write the relevant data slot
                        DatabaseReference motorRef = FirebaseDatabase.getInstance().getReference().child("Item001/motor");
                        motorRef.setValue(true);

                        Log.d(TAG,"here 1");
                        btnUnlock.setEnabled(false);
                        imgStatus.setImageDrawable(getResources().getDrawable(R.drawable.unlocked_open_it));
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value, log details.
                Log.w(TAG, "Failed to read value.", error.toException());

                // show a toast to user
                CharSequence text = "Login Error, Please Try again!";
                int duration = Toast.LENGTH_SHORT;
            }
        });

*/

    }

    private void setStatus(){
        Log.d(TAG,itemNumber);
        Log.d(TAG,userName);
        DatabaseReference sensorRef = database.getReference().child(itemNumber+"/sensor");
        ValueEventListener sensorListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                Boolean status = dataSnapshot.getValue(Boolean.class);
                if(!status){
                    //if the sensor says door is closed.
                    btnUnlock.setEnabled(true);
                    imgStatus.setImageDrawable(getResources().getDrawable(R.drawable.closed_locked));
                }
                else{
                    //if the sensor says door is open,
                    btnUnlock.setEnabled(false);
                    imgStatus.setImageDrawable(getResources().getDrawable(R.drawable.opened));
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());

            }
        };
        sensorRef.addValueEventListener(sensorListener);
    }

    public void addPeople(View view) {
        Intent settingsIntent = new Intent(MainActivity.this, Settings.class);
        settingsIntent.putExtra("userName", userName);
        settingsIntent.putExtra("itemNumber",itemNumber);
        startActivity(settingsIntent);
    }

    public void initiate(View view) {
        DatabaseReference ref = database.getReference("Item001");
        ref.setValue(new DoorLock());
    }
}
