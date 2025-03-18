/*This is where the farmers details are displayed. It where the farmer is required to inputs his details and the size of his/her farm
* the data the farmer fills out is also validated and saved in the database */
package com.example.farmers;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.farmers.Model.Farmer;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Spinner countySpinner, constituencySpinner, wardSpinner;
    private EditText nameField,emailField, phoneField, ageField, farmSizeField,idNumberField;
    private Button registerButton;
    private DatabaseReference databaseReference,reference,userReference;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        countySpinner = findViewById(R.id.spinner_county);
        constituencySpinner = findViewById(R.id.spinner_constituency);
        wardSpinner = findViewById(R.id.spinner_ward);

        nameField = findViewById(R.id.edit_name);
        phoneField = findViewById(R.id.edit_phone);
        ageField = findViewById(R.id.edit_age);
        emailField = findViewById(R.id.edit_email);
        farmSizeField = findViewById(R.id.edit_farm_size);
        idNumberField = findViewById(R.id.edit_id);
        registerButton = findViewById(R.id.button_register);
        databaseReference = FirebaseDatabase.getInstance().getReference("Regions");


            userReference = FirebaseDatabase.getInstance().getReference("users");

        reference = FirebaseDatabase.getInstance().getReference();

        loadCounties();
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameField.getText().toString();
                String phone = phoneField.getText().toString();
                String age = ageField.getText().toString();
                String email = emailField.getText().toString();
                String idNumber=idNumberField.getText().toString();
                String farmSize = farmSizeField.getText().toString();
                String county =countySpinner.getSelectedItem().toString();
                String constituency = constituencySpinner.getSelectedItem().toString();
                String ward = wardSpinner.getSelectedItem().toString();
                registerFarmer(name,phone,age,farmSize,county,constituency,ward,idNumber,email);
            }
        });
    }

    private void loadCounties() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> counties = new ArrayList<>();
                counties.add("Select County");
                for (DataSnapshot countySnapshot : dataSnapshot.getChildren()) {

                    counties.add(countySnapshot.getKey());
                }

                ArrayAdapter<String> countyAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_item, counties);
                countyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                countySpinner.setAdapter(countyAdapter);
                countySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String selectedCounty = countySpinner.getSelectedItem().toString();
                        loadConstituencies(selectedCounty);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void loadConstituencies(String county) {
        databaseReference.child(county).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> constituencies = new ArrayList<>();
                constituencies.add("Select Constituency");
                for (DataSnapshot constituencySnapshot : dataSnapshot.getChildren()) {

                    constituencies.add(constituencySnapshot.getKey());
                }

                ArrayAdapter<String> constituencyAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_item, constituencies);
                constituencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                constituencySpinner.setAdapter(constituencyAdapter);
                constituencySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String selectedConstituency = constituencySpinner.getSelectedItem().toString();
                        loadWards(county, selectedConstituency);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void loadWards(String county, String constituency) {
        databaseReference.child(county).child(constituency).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> wards = new ArrayList<>();
                wards.add("Select Ward");
                for (DataSnapshot wardSnapshot : dataSnapshot.getChildren()) {

                    wards.add(wardSnapshot.getValue(String.class));
                }

                ArrayAdapter<String> wardAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_item, wards);
                wardAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                wardSpinner.setAdapter(wardAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void registerFarmer(String name, String phone, String age, String farmSize, String county, String constituency, String ward,String idNumber,String emails) {
        if (emails.isEmpty()) {
            Toast.makeText(MainActivity.this, "Email Address is required", Toast.LENGTH_SHORT).show();
            return;
        }
        if (idNumber.isEmpty()) {
            Toast.makeText(MainActivity.this, "Id number is required", Toast.LENGTH_SHORT).show();
            return;
        }
        if (phone.isEmpty()) {
            Toast.makeText(MainActivity.this, "Phone number is required", Toast.LENGTH_SHORT).show();
            return;
        }
        if (name.isEmpty()) {
            Toast.makeText(MainActivity.this, "Name is required", Toast.LENGTH_SHORT).show();
            return;
        }
        if (age.isEmpty()) {
            Toast.makeText(MainActivity.this, "Age is required", Toast.LENGTH_SHORT).show();
            return;
        }
        if (farmSize.isEmpty()) {
            Toast.makeText(MainActivity.this, "Farm Size is required", Toast.LENGTH_SHORT).show();
            return;
        }


        Farmer farmer=new Farmer(name,phone,age,farmSize,county,constituency,ward,idNumber,emails);
        Farmer farmer1=new Farmer(name,ward,idNumber,emails);

        reference.child("Farmers").child(ward).child(idNumber).setValue(farmer)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String sanitizedEmail = emails.replace(".", ",");
                       userReference.child(ward).child(sanitizedEmail).setValue(farmer1);
                        Toast.makeText(MainActivity.this, "Farmer registered successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this, NotificationActivityFarmer.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(MainActivity.this, "Failed to register farmer", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
