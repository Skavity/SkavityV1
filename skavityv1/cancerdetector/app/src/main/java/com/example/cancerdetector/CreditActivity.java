package com.example.cancerdetector;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class CreditActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.credit_activity);

        // Setting the citations
        TextView citationsTextView = findViewById(R.id.citations_text);
        String citations = "1. Roboflow Cancer Classification Dataset:\n" +
                "   https://universe.roboflow.com/wictronix-xs7mo/cancer-classification-ysnra\n\n" +
                "2. Roboflow Normal Skin Dataset:\n" +
                "   https://universe.roboflow.com/janitha-prathapa/normalskin\n\n" +
                "3. Melanoma Detection Dataset:\n" +
                "   https://universe.roboflow.com/melanomadetection-x01mf/melanoma-detection-svbl4\n";
        citationsTextView.setText(citations);
    }
}
