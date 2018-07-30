package com.uom.chathus.doorlock;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import utill.StableArrayAdapter;
import utill.User;

public class Settings extends AppCompatActivity {

    ListView lstPeople;
    Button btnAdd;
    Button btnRemove;
    Button btnOK;
    ArrayList<String> values;
    String TAG = "fillingfilelist";

    int selectedIndex;
    String userName;
    String itemNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setTitle("Add or remove people");

        lstPeople = findViewById(R.id.lstPeople);
        lstPeople.setSelector(android.R.color.holo_blue_dark);
        btnRemove = findViewById(R.id.btnRemove);
        btnAdd = findViewById(R.id.btnAddPerson);
        userName = getIntent().getStringExtra("userName");
        itemNumber = getIntent().getStringExtra("itemNumber");

        values = new ArrayList<>();
        refreshFileList();

        btnRemove.setEnabled(false);


        //ListView item select listener.
        lstPeople.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                btnRemove.setEnabled(true);
                //Toast.makeText(ScheculeLullabyActivity.this, values.get(position), Toast.LENGTH_SHORT).show();
                selectedIndex = position;
            }
        });
    }

    private void fillListView(final ArrayList values) {
        final StableArrayAdapter emptyAdapter = new StableArrayAdapter(this,
                android.R.layout.simple_list_item_1, new ArrayList<String>());
        final StableArrayAdapter adapter = new StableArrayAdapter(this,
                android.R.layout.simple_list_item_1, values);
        //lstPeople.removeAllViews();
        lstPeople.setAdapter(null);

//        lstPeople.setAdapter(emptyAdapter);
        lstPeople.setAdapter(adapter);
    }

    private void refreshFileList() {
        values.clear();

        // get database instance and slot
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference().child(itemNumber + "/userList");
        Log.d(TAG, "instance and ref retrieved");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> users = dataSnapshot.getChildren();
                for (DataSnapshot user : users) {
                    Log.d(TAG, user.getKey());
                    values.add(user.getKey());
                    fillListView(values);
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


        //Log.d(TAG, values.get(0));
    }

    public void addPeople(View view) {
        try {
            // get database instance and slot
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            final DatabaseReference myRef = database.getReference().child(itemNumber + "/userList");
            Log.d(TAG, "instance and ref retrieved");


            //show dialog box to get iputs
            final EditText txtName = new EditText(this);
            EditText txtPasst = new EditText(this);

            txtPasst.setInputType(InputType.TYPE_NUMBER_VARIATION_PASSWORD);

            final EditText txtPass = txtPasst;

            final TextView txtView1 = new TextView(this);
            final TextView txtView2 = new TextView(this);
            txtView1.setText("User Name");
            txtView2.setText("Password");

            LinearLayout layout = new LinearLayout(this);
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.addView(txtView1);
            layout.addView(txtName);
            layout.addView(txtView2);
            layout.addView(txtPass);


            new AlertDialog.Builder(this)
                    .setTitle("Input New User Credentials")
                    .setView(layout)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            String name = txtName.getText().toString();
                            String pass = txtPass.getText().toString();
                            myRef.child(name).setValue(pass);

                            Toast.makeText(Settings.this, "New User Add Success...\nOpening Main Control Page...", Toast.LENGTH_SHORT).show();
                            finish();
//                            refreshFileList();
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            Toast.makeText(Settings.this, "New User Add cancelled", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .show();
        }
        catch (Exception ee){
            //Toast.makeText(Settings.this, "Sorry, an error occured!", Toast.LENGTH_SHORT).show();
        }
    }

    public void removeUser(View view) {
        try {
            String removingUser = values.get(selectedIndex);
            // get database instance and slot
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            final DatabaseReference myRef = database.getReference().child(itemNumber + "/userList/" + removingUser);
            Log.d(TAG, "instance and ref retrieved");
            myRef.removeValue();
            Toast.makeText(Settings.this, "User Remove Success...\nOpening Main Control Page...", Toast.LENGTH_SHORT).show();
            finish();
//            refreshFileList();
        }
        catch (Exception ex){
//            Toast.makeText(Settings.this, "Sorry, an error occured!", Toast.LENGTH_SHORT).show();
        }
    }

    public void help(View view) {
        new AlertDialog.Builder(this).setTitle("Quick help...").setMessage("The list contains the users who has permission to operate this device.\n" +
                "\nYou can give permission to this device by clicking on 'Add' button and input" +
                " a username and a password and then click OK.\n\n" +
                "Share the credentials with them to give access. More help on the User's manual." +
                "If you have the user already created, the password will change.\n\n" +
                "You can remove permission of a particular user by clicking on the User's name on the name list and clicking 'Remove' button.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //close and do nothing
                    }}).show();
    }
}
