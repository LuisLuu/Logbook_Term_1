package com.example.informaionlog;

public class ContactModel {

    // 1. THE FIELDS (The actual data)
    // We use 'private' so other files can't mess with them directly.
    private int id;
    private String name;
    private String phoneNumber;
    private String dob;

    private String email;

    public ContactModel(int id, String name, String phoneNumber, String dob, String email) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.dob = dob;
        this.email = email;
    }

    // 3. THE "TO STRING" (Optional but very helpful)
    // This tells Java what to print if you just try to print the object.
    // Without this, it prints gibberish like "ContactModel@4f35a".
    // REPLACE THE OLD toString WITH THIS:
    @Override
    public String toString() {
        // This will display Name on top, and Phone number below it
        return "Name: " + name + "\nPhone: " + phoneNumber + "\nDOB: " + dob + "\nEmail: " + email;
    }

    // 4. GETTERS AND SETTERS
    // These are the doors to access the private data.

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getDob() { return dob; }
    public void setDob(String dob) { this.dob = dob; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }



}