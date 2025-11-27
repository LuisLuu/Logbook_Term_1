package com.example.informaionlog;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AddActivity extends AppCompatActivity {

    EditText etName, etPhone, etDob, etEmail;
    Button btnSave, btnDelete; // Added btnDelete
    int idToUpdate = -1; // Default -1 means "Create Mode"

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        etName = findViewById(R.id.et_name);
        etPhone = findViewById(R.id.et_phone);
        etDob = findViewById(R.id.et_dob);
        etEmail = findViewById(R.id.et_email);
        btnSave = findViewById(R.id.btn_save);
        btnDelete = findViewById(R.id.btn_delete); // Bind the new button

        // 1. CHECK INCOMING DATA (Are we Editing?)
        Intent intent = getIntent();
        if (intent.hasExtra("id")) {
            // EDIT MODE ACTIVATED
            idToUpdate = intent.getIntExtra("id", -1);

            // Fill the boxes
            etName.setText(intent.getStringExtra("name"));
            etPhone.setText(intent.getStringExtra("phone"));
            etDob.setText(intent.getStringExtra("dob"));
            etEmail.setText(intent.getStringExtra("email"));

            // UI Changes
            btnSave.setText("Update Contact");
            btnDelete.setVisibility(View.VISIBLE); // Show the Delete button!
        }

        // 2. SAVE / UPDATE LOGIC
        btnSave.setOnClickListener(v -> {
            DataBaseHelper dataBaseHelper = new DataBaseHelper(AddActivity.this);
            ContactModel model;

            try {
                model = new ContactModel(idToUpdate,
                        etName.getText().toString(),
                        etPhone.getText().toString(),
                        etDob.getText().toString(),
                        etEmail.getText().toString());
            } catch (Exception e) {
                model = new ContactModel(-1, "error", "0", "0", "0");
            }

            if (idToUpdate == -1) {
                // CREATE
                dataBaseHelper.addOne(model);
                Toast.makeText(this, "Created!", Toast.LENGTH_SHORT).show();
            } else {
                // UPDATE
                dataBaseHelper.updateOne(model);
                Toast.makeText(this, "Updated!", Toast.LENGTH_SHORT).show();
            }
            finish();
        });

        // 3. DELETE LOGIC (Simple and Button-based)
        btnDelete.setOnClickListener(v -> {
            DataBaseHelper dataBaseHelper = new DataBaseHelper(AddActivity.this);
            // We just need the ID to delete
            ContactModel modelToDelete = new ContactModel(idToUpdate, "", "", "", "");

            dataBaseHelper.deleteOne(modelToDelete);

            Toast.makeText(this, "Deleted!", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}