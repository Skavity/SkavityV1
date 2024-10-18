package com.example.cancerdetector;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class WelcomeActivitySkin extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_skin);
        showSecondPopup();
        Button nextButton = findViewById(R.id.next_button);
        nextButton.setOnClickListener(v -> {
            Intent intent = new Intent(WelcomeActivitySkin.this, DisclaimerActivitySkin.class);
            startActivity(intent);
            finish();
        });
    }
    private void showSecondPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(WelcomeActivitySkin.this);
        builder.setTitle("Disclaimer")
                .setMessage("This feature of the app analyzes various types of skin cancers, such as Basal Cell Carcinoma, Squamous Cell Carcinoma, Melanoma, Merkel Cell Carcinoma, and Kaposi Sarcoma. The final output provides the probability of a skin cancer being present.  Please note that this app is designed for informational purposes and does not substitute for a professional medical diagnosis. If the app indicates a higher likelihood of skin cancer, or if you have concerns, consult a healthcare provider for a comprehensive evaluation.\n")
                .setPositiveButton("Ok", (dialog, which) -> {

                })
                .setNegativeButton("Close", (dialog, which) -> dialog.dismiss()) // Close the dialog
                .show();
    }
}
