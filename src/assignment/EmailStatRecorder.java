package assignment;

import java.util.Calendar;

public class EmailStatRecorder implements Observable {
    @Override
    public void update(int number) {
        String notification = "Notification: " + number + " emails received at " + Calendar.getInstance().getTime().toString();
        InOut.saveNotificationToFile(notification);
    }
}
