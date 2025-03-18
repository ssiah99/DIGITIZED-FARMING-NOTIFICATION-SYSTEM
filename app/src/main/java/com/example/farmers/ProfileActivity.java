package com.example.farmers;
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.MenuItem;
//import android.widget.ImageView;
//import android.widget.TextView;
//import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
//import com.google.android.material.bottomNavigation.BottomNavigationView;
//import com.google.firebase.auth.FirebaseAuth;
//
public class ProfileActivity extends AppCompatActivity {
//
//    private TextView emailTextView;
//    private ImageView logoImageView;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_profile);
//
//        emailTextView = findViewById(R.id.emailTextView);
//        logoImageView = findViewById(R.id.logoImageView);
//
//        // Get the current user email
//        String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
//        emailTextView.setText(userEmail);
//
//        // Set up the Bottom Navigation
//        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
//        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                switch (item.getItemId()) {
//                    case R.id.navigation_home:
//                        startActivity(new Intent(ProfileActivity.this, HomeActivity.class));
//                        return true;
//                    case R.id.navigation_profile:
//                        // Current Activity
//                        return true;
//                    case R.id.navigation_settings:
//                        startActivity(new Intent(ProfileActivity.this, ProfileActivity.class));
//                        return true;
//                }
//                return false;
//            }
//        });
//    }
}
