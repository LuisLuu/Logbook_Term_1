package com.example.informaionlog;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {

    // Constant variables for column names (Prevents typos later!)
    public static final String CUSTOMER_TABLE = "CUSTOMER_TABLE";
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_CUSTOMER_NAME = "CUSTOMER_NAME";
    public static final String COLUMN_CUSTOMER_PHONE = "CUSTOMER_PHONE";
    public static final String COLUMN_CUSTOMER_DOB = "CUSTOMER_DOB";

    public static final String COLUMN_CUSTOMER_EMAIL = "CUSTOMER_EMAIL";


    // 1. THE CONSTRUCTOR
    // Creates a file named "phonebook.db" on the phone
    public DataBaseHelper(@Nullable Context context) {
        super(context, "phonebook.db", null, 1);
    }

    // 2. ON CREATE
    // This runs strictly ONCE: the very first time the app is installed.
    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL Command:
        // CREATE TABLE CUSTOMER_TABLE (ID INTEGER PRIMARY KEY AUTOINCREMENT, CUSTOMER_NAME TEXT, CUSTOMER_PHONE TEXT, CUSTOMER_DOB TEXT)

        String createTableStatement = "CREATE TABLE " + CUSTOMER_TABLE + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_CUSTOMER_NAME + " TEXT, " +
                COLUMN_CUSTOMER_PHONE + " TEXT, " +
                COLUMN_CUSTOMER_DOB + " TEXT, " +
                COLUMN_CUSTOMER_EMAIL + " TEXT)";


        db.execSQL(createTableStatement);
    }

    // 3. ON UPGRADE
    // Runs if you change the version number in the constructor (e.g., from 1 to 2)
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Nuke the old table and build a new one
        db.execSQL("DROP TABLE IF EXISTS " + CUSTOMER_TABLE);
        onCreate(db);
    }

    // 4. ADD ONE METHOD (We need this to insert data!)
    public boolean addOne(ContactModel contactModel) {
        SQLiteDatabase db = this.getWritableDatabase(); // Unlock the drawer
        ContentValues cv = new ContentValues(); // A special map for DB data

        // We don't put ID because it's Auto-Increment
        cv.put(COLUMN_CUSTOMER_NAME, contactModel.getName());
        cv.put(COLUMN_CUSTOMER_PHONE, contactModel.getPhoneNumber());
        cv.put(COLUMN_CUSTOMER_DOB, contactModel.getDob());
        cv.put(COLUMN_CUSTOMER_EMAIL, contactModel.getEmail());


        // Insert returns -1 if it failed, or the row ID if success
        long insert = db.insert(CUSTOMER_TABLE, null, cv);

        db.close(); // Always close the drawer!

        return insert != -1;
    }

    // 5. GET EVERYONE (Read from DB)
    public List<ContactModel> getEveryone() {
        List<ContactModel> returnList = new ArrayList<>();

        // The Query: "SELECT * FROM CUSTOMER_TABLE"
        String queryString = "SELECT * FROM " + CUSTOMER_TABLE;

        SQLiteDatabase db = this.getReadableDatabase(); // "Readable" this time, not Writable

        // The Cursor is the result set
        Cursor cursor = db.rawQuery(queryString, null);

        // Check if we got results
        if (cursor.moveToFirst()) {
            // Loop through the results (Move finger down one row at a time)
            do {
                int customerID = cursor.getInt(0); // Column 0 is ID
                String customerName = cursor.getString(1); // Column 1 is Name
                String customerPhone = cursor.getString(2);
                String customerDob = cursor.getString(3);
                String customerEmail = cursor.getString(4);


                // Create the object
                ContactModel newCustomer = new ContactModel(customerID, customerName, customerPhone, customerDob, customerEmail);

                // Add to list
                returnList.add(newCustomer);

            } while (cursor.moveToNext()); // Keep going while there are next rows
        }


        // Always close the cursor and db when done!
        cursor.close();
        db.close();

        return returnList;
    }

    // 6. DELETE ONE ROW
    public boolean deleteOne(ContactModel contactModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = COLUMN_ID + " = " + contactModel.getId();
        long deleteCount = db.delete(CUSTOMER_TABLE, whereClause, null);
        db.close();
        return deleteCount > 0;
    }

    // 7. UPDATE ONE ROW (This is the one causing your error!)
    public boolean updateOne(ContactModel contactModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_CUSTOMER_NAME, contactModel.getName());
        cv.put(COLUMN_CUSTOMER_PHONE, contactModel.getPhoneNumber());
        cv.put(COLUMN_CUSTOMER_DOB, contactModel.getDob());
        cv.put(COLUMN_CUSTOMER_EMAIL, contactModel.getEmail());

        String whereClause = COLUMN_ID + " = " + contactModel.getId();
        long updateCount = db.update(CUSTOMER_TABLE, cv, whereClause, null);
        db.close();
        return updateCount > 0;
    }
}