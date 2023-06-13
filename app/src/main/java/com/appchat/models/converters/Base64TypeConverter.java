package com.appchat.models.converters;

import android.util.Base64;
import androidx.room.TypeConverter;

public class Base64TypeConverter {
    @TypeConverter
    public static byte[] fromBase64String(String base64String) {
        return Base64.decode(base64String, Base64.DEFAULT);
    }

    @TypeConverter
    public static String toBase64String(byte[] bytes) {
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }
}

