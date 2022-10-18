package assignment;

import java.io.Serializable;

public class ReceivedMail implements Serializable {
    private String senderEmail, subject, content, receivedDate;

    public ReceivedMail(String senderEmail, String subject, String content, String receivedDate) {
        this.senderEmail = senderEmail;
        this.subject = subject;
        this.content = content;
        this.receivedDate = receivedDate;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public String getSubject() {
        return subject;
    }

    public String getContent() {
        return content;
    }

    public String getReceivedDate() {
        return receivedDate;
    }
}
