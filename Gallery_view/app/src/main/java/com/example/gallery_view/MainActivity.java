package com.example.gallery_view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    // 1. We switch back to "GetMultipleContents" (The System File Browser)
    // This gives you the side menu with "Drive", "Downloads", etc.
    private final ActivityResultLauncher<String> pickImageLauncher = registerForActivityResult(
            new ActivityResultContracts.GetMultipleContents(),
            uris -> {
                if (uris != null && !uris.isEmpty()) {
                    int count = 0;

                    for (Uri sourceUri : uris) {
                        // USE THE COPY FUNCTION
                        Uri localUri = copyToInternalStorage(sourceUri);

                        if (localUri != null) {
                            ImageRepository.imageList.add(localUri);
                            count++;
                        }
                    }

                    ImageRepository.saveImages(this);
                    Toast.makeText(this, "Saved " + count + " images permanently!", Toast.LENGTH_SHORT).show();
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Load saved images when app starts
        ImageRepository.loadImages(this);

        Button btnAdd = findViewById(R.id.btnAdd);
        Button btnView = findViewById(R.id.btnView);
        Button btnExit = findViewById(R.id.btnExit);

        // 3. The launch command is simple again
        btnAdd.setOnClickListener(v -> {
            // "image/*" tells the System Picker to show all image types
            pickImageLauncher.launch("image/*");
        });

        btnView.setOnClickListener(v -> {
            if (ImageRepository.imageList.isEmpty()) {
                Toast.makeText(this, "No images to view!", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(MainActivity.this, ViewerActivity.class);
                startActivity(intent);
            }
        });

        btnExit.setOnClickListener(v -> finishAffinity());
    }

    // This function takes a temporary cloud URI and saves a real copy to your phone
    private Uri copyToInternalStorage(Uri sourceUri) {
        try {
            // 1. Create a unique filename
            String filename = "img_" + System.currentTimeMillis() + ".jpg";

            // 2. Open the source (Google Drive/Downloads)
            InputStream inputStream = getContentResolver().openInputStream(sourceUri);

            // 3. Open the destination (Your App's Private Storage)
            FileOutputStream outputStream = openFileOutput(filename, MODE_PRIVATE);

            // 4. Copy the data byte-by-byte
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            inputStream.close();
            outputStream.close();

            // 5. Return the address of the NEW local file
            File file = new File(getFilesDir(), filename);
            return Uri.fromFile(file);

        } catch (Exception e) {
            e.printStackTrace();
            return null; // Copy failed
        }
    }
}