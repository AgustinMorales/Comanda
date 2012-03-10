package com.company.comanda.peter.client.helper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


//FIXME: Some methods on this class could be shared with Brian
public class Formatter {

    public static String formatToYesterdayOrToday(Date dateTime){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateTime);
        Calendar today = Calendar.getInstance();
        Calendar yesterday = Calendar.getInstance();
        yesterday.add(Calendar.DATE, -1);
        DateFormat timeFormatter = new SimpleDateFormat("HH:mm");


        if (calendar.get(Calendar.YEAR) == today.get(Calendar.YEAR) && calendar.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR)) {
            return "Hoy " + timeFormatter.format(dateTime);
        } else if (calendar.get(Calendar.YEAR) == yesterday.get(Calendar.YEAR) && calendar.get(Calendar.DAY_OF_YEAR) == yesterday.get(Calendar.DAY_OF_YEAR)) {
            return "Ayer " + timeFormatter.format(dateTime);
        } else {
            //FIXME: This is not localized
            DateFormat dateAndTimeFormatter = new SimpleDateFormat("dd/mm/yyyy - HH:mm");
            return dateAndTimeFormatter.format(dateTime);
        }
    }
    
    public static String formatToTime(Date dateTime){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateTime);
        Calendar today = Calendar.getInstance();
        DateFormat timeFormatter = null;
        if (calendar.get(Calendar.YEAR) == today.get(Calendar.YEAR) && calendar.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR)) {
            timeFormatter = new SimpleDateFormat("HH:mm");
        }
        else{
            timeFormatter = new SimpleDateFormat("dd/mm/yyyy - HH:mm");
        }
        
        
        return timeFormatter.format(dateTime);
    }

}
