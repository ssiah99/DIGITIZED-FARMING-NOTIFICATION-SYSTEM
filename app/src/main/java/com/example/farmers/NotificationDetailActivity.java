package com.example.farmers;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.farmers.Mail.JavaMailAPI;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class NotificationDetailActivity extends AppCompatActivity {

    private TextView tvTitle, tvMessage;
    private Spinner spinnerCounty, spinnerConstituency, spinnerWard;
    private EditText editTextFurtherDesc;
    private Button btnSend;
    private ImageView backArrow;

    private DatabaseReference databaseReference;

    private String selectedCounty, selectedConstituency, selectedWard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_detail);

        tvTitle = findViewById(R.id.tvTitle);
        tvMessage = findViewById(R.id.tvMessage);
        spinnerCounty = findViewById(R.id.spinnerCounty);
        spinnerConstituency = findViewById(R.id.spinnerConstituency);
        spinnerWard = findViewById(R.id.spinnerWard);
        editTextFurtherDesc = findViewById(R.id.editTextFurtherDesc);
        btnSend = findViewById(R.id.btnSend);
        backArrow = findViewById(R.id.back_arrow);
        databaseReference = FirebaseDatabase.getInstance().getReference("Regions");

        String title = getIntent().getStringExtra("title");
        String message = getIntent().getStringExtra("message");

        tvTitle.setText(title);
        tvMessage.setText(message);

        loadCounties();
        backArrow.setOnClickListener(v -> finish());


        btnSend.setOnClickListener(v -> sendNotification());
        spinnerCounty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCounty = parent.getItemAtPosition(position).toString();
                loadConstituencies(selectedCounty);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinnerConstituency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedConstituency = parent.getItemAtPosition(position).toString();
                loadWards(selectedConstituency);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinnerWard.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedWard = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void loadCounties() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> counties = new ArrayList<>();
                counties.add("Select County");
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    counties.add(snapshot.getKey());
                }
                ArrayAdapter<String> countyAdapter = new ArrayAdapter<>(NotificationDetailActivity.this, android.R.layout.simple_spinner_item, counties);
                countyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerCounty.setAdapter(countyAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(NotificationDetailActivity.this, "Failed to load counties", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadConstituencies(String county) {
        DatabaseReference constituenciesRef = databaseReference.child(county);
        constituenciesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> constituencies = new ArrayList<>();
                constituencies.add("Select Constituency");
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    constituencies.add(snapshot.getKey());
                }
                ArrayAdapter<String> constituencyAdapter = new ArrayAdapter<>(NotificationDetailActivity.this, android.R.layout.simple_spinner_item, constituencies);
                constituencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerConstituency.setAdapter(constituencyAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(NotificationDetailActivity.this, "Failed to load constituencies", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadWards(String constituency) {
        DatabaseReference wardsRef = databaseReference.child(selectedCounty).child(constituency);
        wardsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> wards = new ArrayList<>();
                wards.add("Select Ward");
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    wards.add(snapshot.getValue(String.class));
                }
                ArrayAdapter<String> wardAdapter = new ArrayAdapter<>(NotificationDetailActivity.this, android.R.layout.simple_spinner_item, wards);
                wardAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerWard.setAdapter(wardAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(NotificationDetailActivity.this, "Failed to load wards", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendNotification() {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");

        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> recipientEmails = new ArrayList<>();
                for (DataSnapshot wardSnapshot : dataSnapshot.getChildren()) {
                    String wardName = wardSnapshot.getKey();
                    if (wardName != null && wardName.equals(selectedWard)) {
                        for (DataSnapshot userSnapshot : wardSnapshot.getChildren()) {
                            String email = userSnapshot.getKey();


                            if (email != null) {
                                recipientEmails.add(email.replace(",","."));
                            }
                        }

                        if (recipientEmails.isEmpty()) {
                            Toast.makeText(getApplicationContext(), "No emails found for ward: " + selectedWard, Toast.LENGTH_SHORT).show();
                        }
                        break;
                    }
                }

                if (!recipientEmails.isEmpty()) {
                    String[] recipientsArray = recipientEmails.toArray(new String[0]);
                    String title = tvTitle.getText().toString();
                    String message = tvMessage.getText().toString();
                    String furtherDesc = editTextFurtherDesc.getText().toString();
                    String combinedMessage = message + "\n\nFurther Description: " + furtherDesc + "\nCounty: " + selectedCounty + "\nConstituency: " + selectedConstituency + "\nWard: " + selectedWard;

                    JavaMailAPI javaMailAPI = new JavaMailAPI(getApplicationContext(), recipientsArray, title, combinedMessage);
                    javaMailAPI.execute();

                    Toast.makeText(getApplicationContext(), "Email sent successfully!", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getApplicationContext(), NotificationDetailActivity.class));
                } else {
                    Toast.makeText(getApplicationContext(), "No recipients found for the selected ward.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Failed to fetch data: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }
}
