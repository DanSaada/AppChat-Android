package com.appchat.entities.converters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import androidx.room.TypeConverter;

import java.io.ByteArrayOutputStream;

public class Base64TypeConverter {
    @TypeConverter
    public static byte[] fromBase64String(String base64String) {
        return Base64.decode(base64String, Base64.DEFAULT);
    }

    @TypeConverter
    public static String toBase64String(byte[] bytes) {
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    @TypeConverter
    public static String convertBitmapToBase64String(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    @TypeConverter
    public static Bitmap convertBase64ToBitmap(String base64String) {
        if (base64String == null) {
            return null;
        }
        byte[] decodedString = Base64.decode(base64String, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }

}

