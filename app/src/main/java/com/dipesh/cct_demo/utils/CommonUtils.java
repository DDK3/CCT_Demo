package com.dipesh.cct_demo.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CommonUtils {
    public static String getDayMonthYearFromMiliseconds(String Miliseconds){
        if(Miliseconds == null){
            return "";
        }

        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        long milliSeconds= Long.parseLong(Miliseconds);
//        System.out.println(milliSeconds);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
//        System.out.println(formatter.format(calendar.getTime()));

        return  formatter.format(calendar.getTime());
    }
}
