package assignment;

import java.io.Serializable;

public class Email implements Serializable {
    private String recipientEmail, subject, content;
    private Date sentDate;

    public Email(String recipientEmail, String subject, String content) {
        this.recipientEmail = recipientEmail;
        this.subject = subject;
        this.content = content;
        this.sentDate = new Date();
    }

    public String getRecipientEmail() {
        return recipientEmail;
    }

    public String getSubject() {
        return subject;
    }

    public String getContent() {
        return content;
    }

    public Date getSentDate() {
        return sentDate;
    }
}
