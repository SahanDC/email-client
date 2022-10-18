package assignment;

import java.util.Calendar;

public class EmailStatPrinter implements Observable {
    @Override
    public void update(int number) {
        System.out.println("Notification: " + number + " emails received at " + Calendar.getInstance().getTime().toString());
    }
}
