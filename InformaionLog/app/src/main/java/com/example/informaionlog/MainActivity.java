package com.example.informaionlog;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button btn_add_new, btn_quit; // Added btn_quit variable
    ListView lv_customerList;
    DataBaseHelper dataBaseHelper;
    ArrayAdapter customerArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 1. Hook up UI
        btn_add_new = findViewById(R.id.btn_add_new);
        btn_quit = findViewById(R.id.btn_quit); // Find the new button
        lv_customerList = findViewById(R.id.lv_customerList);

        dataBaseHelper = new DataBaseHelper(MainActivity.this);
        ShowCustomersOnListView(dataBaseHelper);

        // 2. Add Logic
        btn_add_new.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddActivity.class);
            startActivity(intent);
        });

        // 3. Quit Logic (New!)
        btn_quit.setOnClickListener(v -> {
            finishAffinity(); // Closes the app completely
        });

        // CLICK LISTENER (Just a normal left-click)
        lv_customerList.setOnItemClickListener((parent, view, position, id) -> {
            // 1. Get the clicked person
            ContactModel clickedCustomer = (ContactModel) parent.getItemAtPosition(position);

            // 2. Pack their bags (data)
            Intent intent = new Intent(MainActivity.this, AddActivity.class);
            intent.putExtra("id", clickedCustomer.getId());
            intent.putExtra("name", clickedCustomer.getName());
            intent.putExtra("phone", clickedCustomer.getPhoneNumber());
            intent.putExtra("dob", clickedCustomer.getDob());
            intent.putExtra("email", clickedCustomer.getEmail());

            // 3. Send them to the Add/Edit Page
            startActivity(intent);
        });

        // CLICK LISTENER (Just a normal left-click)
        lv_customerList.setOnItemClickListener((parent, view, position, id) -> {
            // 1. Get the clicked person
            ContactModel clickedCustomer = (ContactModel) parent.getItemAtPosition(position);

            // 2. Pack their bags (data)
            Intent intent = new Intent(MainActivity.this, AddActivity.class);
            intent.putExtra("id", clickedCustomer.getId());
            intent.putExtra("name", clickedCustomer.getName());
            intent.putExtra("phone", clickedCustomer.getPhoneNumber());
            intent.putExtra("dob", clickedCustomer.getDob());
            intent.putExtra("email", clickedCustomer.getEmail());

            // 3. Send them to the Add/Edit Page
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        ShowCustomersOnListView(dataBaseHelper);
    }

    private void ShowCustomersOnListView(DataBaseHelper dataBaseHelper) {
        List<ContactModel> everyone = dataBaseHelper.getEveryone();
        customerArrayAdapter = new ArrayAdapter<ContactModel>(MainActivity.this, android.R.layout.simple_list_item_1, everyone);
        lv_customerList.setAdapter(customerArrayAdapter);
    }
}