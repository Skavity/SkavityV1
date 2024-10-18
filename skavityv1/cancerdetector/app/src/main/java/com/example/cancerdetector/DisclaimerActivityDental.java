package com.example.cancerdetector;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class DisclaimerActivityDental extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disclaimer_dental);

        Button nextButton = findViewById(R.id.next_button);
        nextButton.setOnClickListener(v -> {
            Intent intent = new Intent(DisclaimerActivityDental.this, MainActivityDental.class);
            startActivity(intent);
            finish();
        });
    }
}
