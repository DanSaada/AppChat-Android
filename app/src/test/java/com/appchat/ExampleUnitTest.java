package com.appchat;

import org.junit.Assert;
import org.junit.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void testTimeStamp() {
        String timestamp = "2023-06-28T11:02:03.427Z";
        DateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        inputDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        DateFormat outputDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        outputDateFormat.setTimeZone(TimeZone.getDefault());

        try {
            Date date = inputDateFormat.parse(timestamp);
            String convertedTimestamp = outputDateFormat.format(date);
            System.out.println(convertedTimestamp);
            Assert.assertEquals("2023-06-28 14:02:03", convertedTimestamp);
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
}