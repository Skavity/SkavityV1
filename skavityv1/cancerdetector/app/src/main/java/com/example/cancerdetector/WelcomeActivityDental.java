package com.example.cancerdetector;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cancerdetector.DisclaimerActivityDental;
import com.example.cancerdetector.R;

public class WelcomeActivityDental extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_dental);
        showSecondPopup();
        Button nextButton = findViewById(R.id.next_button);
        nextButton.setOnClickListener(v -> {
            Intent intent = new Intent(WelcomeActivityDental.this, DisclaimerActivityDental.class);
            startActivity(intent);
            finish();

        });
    }
    private void showSecondPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(WelcomeActivityDental.this);
        builder.setTitle("Disclaimer")

                .setMessage("This feature of the app detects specific dental and oral health conditions, including tooth decay, gum disease, wisdom teeth issues, misaligned teeth, oral cancer, cavities, or whether your oral health is in a normal and healthy state. Please note that while the app provides a direct assessment of these conditions, it does not substitute for a professional dental examination. If any condition is detected, or if you have concerns about your oral health, consult a licensed dentist or healthcare provider for a comprehensive evaluation and treatment.\n")
                .setPositiveButton("Ok", (dialog, which) -> {

                })
                .setNegativeButton("Close", (dialog, which) -> dialog.dismiss()) // Close the dialog
                .show();
    }

}
