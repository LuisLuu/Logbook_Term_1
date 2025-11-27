package com.example.gallery_view;

import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.io.File;

public class ViewerActivity extends AppCompatActivity {

    ImageView imageView;
    Button btnPrev, btnNext, btnHome, btnDelete; // Added btnDelete
    int currentIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewer);

        imageView = findViewById(R.id.imageViewDisplay);
        btnPrev = findViewById(R.id.btnPrev);
        btnNext = findViewById(R.id.btnNext);
        btnHome = findViewById(R.id.btnHome);
        btnDelete = findViewById(R.id.btnDelete); // Bind the new button

        // Load the first image
        updateImage();

        // --- NEW BUTTON LOGIC ---
        btnDelete.setOnClickListener(v -> {
            // Check if there is anything to delete first
            if (!ImageRepository.imageList.isEmpty()) {
                showDeleteDialog();
            }
        });

        // Navigation Logic
        btnNext.setOnClickListener(v -> {
            if (!ImageRepository.imageList.isEmpty()) {
                currentIndex++;
                if (currentIndex >= ImageRepository.imageList.size()) currentIndex = 0;
                updateImage();
            }
        });

        btnPrev.setOnClickListener(v -> {
            if (!ImageRepository.imageList.isEmpty()) {
                currentIndex--;
                if (currentIndex < 0) currentIndex = ImageRepository.imageList.size() - 1;
                updateImage();
            }
        });

        btnHome.setOnClickListener(v -> finish());
    }

    private void showDeleteDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Delete Image")
                .setMessage("Are you sure? This cannot be undone.")
                .setPositiveButton("Yes, Delete", (dialog, which) -> deleteCurrentImage())
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void deleteCurrentImage() {
        if (ImageRepository.imageList.isEmpty()) return;

        // 1. Delete the physical file to save space
        Uri uriToDelete = ImageRepository.imageList.get(currentIndex);
        if (uriToDelete.getPath() != null) {
            File file = new File(uriToDelete.getPath());
            if (file.exists()) {
                file.delete();
            }
        }

        // 2. Remove from list and Save
        ImageRepository.imageList.remove(currentIndex);
        ImageRepository.saveImages(this);

        Toast.makeText(this, "Deleted!", Toast.LENGTH_SHORT).show();

        // 3. Handle Empty List or Index Shift
        if (ImageRepository.imageList.isEmpty()) {
            finish(); // Close viewer if empty
        } else {
            // If we deleted the last item, step back one index
            if (currentIndex >= ImageRepository.imageList.size()) {
                currentIndex = 0;
            }
            updateImage();
        }
    }

    private void updateImage() {
        if (!ImageRepository.imageList.isEmpty()) {
            Uri currentUri = ImageRepository.imageList.get(currentIndex);
            try {
                imageView.setImageURI(currentUri);
            } catch (Exception e) {
                imageView.setImageResource(android.R.drawable.ic_menu_report_image);
            }
        }
    }
}