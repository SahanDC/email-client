package assignment;

import java.io.Serializable;
import java.util.Calendar;

public class Date implements Serializable {
    private String year, month, day;

    // This creates a given date
    public Date(String year, String month, String day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    // This creates today's date
    public Date(){
        Calendar calendar = Calendar.getInstance();
        this.year = String.valueOf(calendar.get(Calendar.YEAR));
        this.month = String.format("%02d", (calendar.get(Calendar.MONTH) + 1));
        this.day = String.format("%02d", (calendar.get(Calendar.DATE)));
    }

    public String getYear() {
        return year;
    }

    public String getMonth() {
        return month;
    }

    public String getDay() {
        return day;
    }
}
