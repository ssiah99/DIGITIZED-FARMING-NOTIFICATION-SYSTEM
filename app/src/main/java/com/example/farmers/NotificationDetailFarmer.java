/*This code displays notification massage to the farmer based on the details the farmer had filled up */
package com.example.farmers;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.farmers.Mail.JavaMailAPI;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class NotificationDetailFarmer extends AppCompatActivity {
    private TextView tvTitle, tvMessage,tvDesc;
    private Spinner spinnerCounty, spinnerConstituency, spinnerWard;
    private EditText editTextFurtherDesc;
    private Button btnSend;
    private ImageView backArrow;

    private DatabaseReference databaseReference;

    private String selectedCounty, selectedConstituency, selectedWard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_detail_farmer);

        tvTitle = findViewById(R.id.tvTitle);
        tvMessage = findViewById(R.id.tvMessage);
        tvDesc=findViewById(R.id.tvFullDescription);
        backArrow = findViewById(R.id.back_arrow);
        databaseReference = FirebaseDatabase.getInstance().getReference("Regions");

        String title = getIntent().getStringExtra("title");
        String message = getIntent().getStringExtra("message");
        String desc = getIntent().getStringExtra("description");

        tvTitle.setText(title);
        tvMessage.setText(message);
        tvDesc.setText(desc);

        backArrow.setOnClickListener(v -> finish());

    }
}