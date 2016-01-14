package fi.cs.ubicomp.detector.RESTsupport;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by huber on 12/12/15.
 */
public class TimeScheduler {

    private final String TAG = TimeScheduler.class.getSimpleName();

    public static final String inputFormat = "HH:mm";

    private Date date;
    private Date dateCompareOne;
    private Date dateCompareTwo;

    private String compareStringOne = "10:00";
    private String compareStringTwo = "11:00";

    SimpleDateFormat inputParser = new SimpleDateFormat(inputFormat, Locale.US);

    public TimeScheduler(){

    }

    public boolean checkTaskExecutionSchedule(){
        String am_pm = "";

        Calendar now = Calendar.getInstance();

        int hour = now.get(Calendar.HOUR);
        int minute = now.get(Calendar.MINUTE);

        if (now.get(Calendar.AM_PM) == Calendar.AM)
            am_pm = "AM";
        else if (now.get(Calendar.AM_PM) == Calendar.PM)
            am_pm = "PM";


        Log.d(TAG, hour + ":" + minute + am_pm);

        date = parseDate(hour + ":" + minute);

        dateCompareOne = parseDate(compareStringOne);
        dateCompareTwo = parseDate(compareStringTwo);


        //night
        if (am_pm.equals("PM")){
            if ( dateCompareOne.before( date ) && dateCompareTwo.after(date)) {
                return true;
            }
        }

        //morning
        if (am_pm.equals("AM")){
            if (dateCompareOne.before(date) && dateCompareTwo.after(date)){
                return true;
            }
        }


        return false;
    }

    private Date parseDate(String date) {

        try {
            return inputParser.parse(date);
        } catch (java.text.ParseException e) {
            return new Date(0);
        }
    }
}
