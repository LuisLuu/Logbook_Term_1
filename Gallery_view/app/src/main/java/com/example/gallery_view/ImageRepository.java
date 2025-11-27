package com.example.gallery_view;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import java.util.ArrayList;
import java.util.List;

public class ImageRepository {

    // The list is still here
    public static List<Uri> imageList = new ArrayList<>();

    private static final String PREF_NAME = "gallery_prefs";

    // 1. SAVE FUNCTION
    public static void saveImages(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        // Save the number of images we have
        editor.putInt("list_size", imageList.size());

        // Save each URI as a string: "image_0", "image_1", etc.
        for (int i = 0; i < imageList.size(); i++) {
            editor.putString("image_" + i, imageList.get(i).toString());
        }
        editor.apply(); // Write to disk
    }

    // 2. LOAD FUNCTION
    public static void loadImages(Context context) {
        imageList.clear(); // Clear current list to avoid duplicates
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        int size = prefs.getInt("list_size", 0);

        for (int i = 0; i < size; i++) {
            String uriString = prefs.getString("image_" + i, null);
            if (uriString != null) {
                imageList.add(Uri.parse(uriString));
            }
        }
    }
}