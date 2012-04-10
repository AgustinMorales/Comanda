package com.company.comanda.peter.server.helper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import com.company.comanda.peter.server.model.Order;

public class ServerFormatter {

    
    private static TimeZone timezone = TimeZone.getTimeZone("Europe/Madrid");
    
    public static String formatToYesterdayOrToday(Date dateTime){
        Calendar calendar = Calendar.getInstance(timezone);
        calendar.setTime(dateTime);
        Calendar today = Calendar.getInstance(timezone);
        Calendar yesterday = Calendar.getInstance(timezone);
        yesterday.add(Calendar.DATE, -1);
        DateFormat timeFormatter = new SimpleDateFormat("HH:mm");
        timeFormatter.setTimeZone(timezone);

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
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Europe/Madrid"));
        calendar.setTime(dateTime);
        Calendar today = Calendar.getInstance();
        DateFormat timeFormatter = null;
        if (calendar.get(Calendar.YEAR) == today.get(Calendar.YEAR) && calendar.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR)) {
            timeFormatter = new SimpleDateFormat("HH:mm");
        }
        else{
            timeFormatter = new SimpleDateFormat("dd/mm/yyyy - HH:mm");
        }
        timeFormatter.setTimeZone(timezone);

        
        return timeFormatter.format(dateTime);
    }
    
    public static String money(float amount){
        return String.format("%.2f €", amount);
    }
    
    public static String getExtras(Order order){
        StringBuffer extrasString = new StringBuffer();
        List<String> extras = order.getExtras();
        if(extras != null && extras.size() > 0){
            extrasString.append(" - ");
            extrasString.append(order.getExtrasName());
            extrasString.append(": ");
            for(String currentExtra:extras){
                extrasString.append(currentExtra);
                extrasString.append(", ");
            }
            extrasString.delete(extrasString.length() - 2, extrasString.length());
        }
        return extrasString.toString();
    }
}
