package com.example.cancerdetector;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class DisclaimerActivitySkin extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disclaimer_skin);

        Button nextButton = findViewById(R.id.next_button);
        nextButton.setOnClickListener(v -> {
            Intent intent = new Intent(DisclaimerActivitySkin.this, MainActivitySkin.class);
            startActivity(intent);
            finish();
        });
    }
}
