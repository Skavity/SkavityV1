package com.example.cancerdetector;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.tensorflow.lite.Interpreter;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class MainActivitySkin extends AppCompatActivity {

    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int REQUEST_IMAGE_CAPTURE = 101;
    private static final int REQUEST_IMAGE_PICK = 102;
    private ImageView mouthPhoto;
    private Bitmap imageBitmap;
    private Interpreter tflite;
    // Corrected label order to match the model's output
    private final String[] labels = {
            "Cancer", "Normal",
    };

    private TextView resultTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_skin);

        mouthPhoto = findViewById(R.id.mouth_photo);
        resultTextView = findViewById(R.id.result_text_view);
        Button takePhotoButton = findViewById(R.id.take_photo_button);
        Button uploadPhotoButton = findViewById(R.id.upload_photo_button);
        Button analyzePhotoButton = findViewById(R.id.analyze_photo_button);

        // Load the TensorFlow Lite model
        try {
            tflite = new Interpreter(loadModelFile("model_unquant1.tflite"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Request Camera Permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
        }

        // Set Take Photo Button Click Listener
        takePhotoButton.setOnClickListener(view -> dispatchTakePictureIntent());

        // Set Upload Photo Button Click Listener
        uploadPhotoButton.setOnClickListener(view -> dispatchUploadPictureIntent());

        // Set Analyze Photo Button Click Listener
        analyzePhotoButton.setOnClickListener(view -> {
            if (imageBitmap != null) {
                analyzePhoto(imageBitmap);
            } else {
                Toast.makeText(this, "Please take a photo or upload one first", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void dispatchUploadPictureIntent() {
        Intent uploadPictureIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if (uploadPictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(uploadPictureIntent, REQUEST_IMAGE_PICK);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_IMAGE_CAPTURE:
                    Bundle extras = data.getExtras();
                    imageBitmap = (Bitmap) extras.get("data");
                    mouthPhoto.setImageBitmap(imageBitmap);
                    break;
                case REQUEST_IMAGE_PICK:
                    Uri selectedImage = data.getData();
                    try {
                        InputStream inputStream = getContentResolver().openInputStream(selectedImage);
                        imageBitmap = BitmapFactory.decodeStream(inputStream);
                        mouthPhoto.setImageBitmap(imageBitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }

    private MappedByteBuffer loadModelFile(String modelName) throws IOException {
        FileInputStream inputStream = new FileInputStream(getAssets().openFd(modelName).getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = getAssets().openFd(modelName).getStartOffset();
        long declaredLength = getAssets().openFd(modelName).getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

    private void analyzePhoto(Bitmap bitmap) {
        // Preprocess the image and run the TensorFlow Lite model
        float[][][][] inputBuffer = preprocessImage(bitmap);
        float[][] outputBuffer = new float[1][labels.length];

        // Run the model
        if (tflite != null) {
            tflite.run(inputBuffer, outputBuffer);
        }

        // Display the result
        displayAnalysisResults(outputBuffer[0]);
    }

    private float[][][][] preprocessImage(Bitmap bitmap) {
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, 224, 224, true);

        float[][][][] input = new float[1][224][224][3];

        for (int y = 0; y < 224; y++) {
            for (int x = 0; x < 224; x++) {
                int pixel = resizedBitmap.getPixel(x, y);
                input[0][y][x][0] = ((pixel >> 16) & 0xFF) / 255.0f;  // Red channel
                input[0][y][x][1] = ((pixel >> 8) & 0xFF) / 255.0f;   // Green channel
                input[0][y][x][2] = (pixel & 0xFF) / 255.0f;          // Blue channel
            }
        }
        return input;
    }

    private void displayAnalysisResults(float[] results) {
        float cancerConfidence = results[0] * 100;
        float normalConfidence = results[1] * 100;
        int maxIndex = 0;

        // Set a threshold, e.g., 70%
        float confidenceThreshold = 70.0f;

        for (int i = 1; i < results.length; i++) {
            if (results[i] > results[maxIndex]) {
                maxIndex = i;
            }
        }

        String condition = labels[maxIndex];
        String detectedCondition = labels[maxIndex];

        if (results[maxIndex] * 100 < confidenceThreshold) {
            // If confidence is low, show a warning instead of a firm diagnosis
            resultTextView.setText("Confidence too low. Please consult a doctor or try a clearer image.");
        } else {
            String resultMessage = String.format("Cancer: %.2f%%\nNormal: %.2f%%\nCondition: %s",
                    cancerConfidence, normalConfidence, condition);
            resultTextView.setText(resultMessage);
        }

        new AlertDialog.Builder(this)
                .setTitle("Analysis Result")
                .setMessage("Possible condition: " + condition + ". Consult a dermatologist for a professional diagnosis.")
                .setPositiveButton("OK", (dialog, which) -> {
                    // If the condition is cancer, show the second popup
                    if (detectedCondition.equals("Cancer")) {
                        showSecondPopup();
                    }
                })
                .show();

    }
    private void showSecondPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivitySkin.this);
        builder.setTitle("More Information")
                .setMessage("Since you have cancer, here is more information.")
                .setPositiveButton("Visit Website", (dialog, which) -> {
                    // Open a website when the user clicks "Visit Website"
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.cancer.gov/types/skin"));
                    startActivity(browserIntent);
                })
                .setNegativeButton("Close", (dialog, which) -> dialog.dismiss()) // Close the dialog
                .show();
    }
}
