package com.example.cancerdetector;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cancerdetector.DisclaimerActivitySkin;
import com.example.cancerdetector.R;
import com.example.cancerdetector.WelcomeActivitySkin;

public class MainActivity extends AppCompatActivity {

    private Button btnSkinCancer;
    private Button btnDental;

    private Button btnCredit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize buttons
        btnSkinCancer = findViewById(R.id.btn_skin_cancer);
        btnDental = findViewById(R.id.btn_dental);
        btnCredit = findViewById(R.id.btn_credit);

        // Set onClickListeners for navigation
        btnSkinCancer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start the Skin Cancer detection app's main activity
                Intent intent = new Intent(MainActivity.this, WelcomeActivitySkin.class);
                startActivity(intent);
            }
        });

        btnDental.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start the Dental detection app's main activity
                Intent intent = new Intent(MainActivity.this, WelcomeActivityDental.class);
                startActivity(intent);
            }
        });
        btnCredit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start the Skin Cancer detection app's main activity
                Intent intent = new Intent(MainActivity.this, CreditActivity.class);
                startActivity(intent);
            }
        });

    }
}
