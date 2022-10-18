package assignment;

import javax.mail.*;
import javax.mail.internet.MimeMultipart;
import javax.mail.search.FlagTerm;
import java.io.IOException;
import java.util.Properties;

public class Receive extends Thread{
    private BlockingQueue blockingQueue;
    private static final String username = "sahantestmail@gmail.com";
    private static final String password = "Sahantest333";
    private Properties properties;
    private volatile boolean isRunning;
    private Observable[] observers;

    public Receive(BlockingQueue blockingQueue, Observable observers1, Observable observers2){
        this.blockingQueue = blockingQueue;
        this.observers = new Observable[2];
        this.observers[0] = observers1;
        this.observers[1] = observers2;
        this.properties = new Properties();

        properties.put("mail.store.protocol", "imaps");
        properties.put("mail.imaps.host", "imap.gmail.com");
        properties.put("mail.imaps.port", "993");
        this.isRunning = true;
    }

    @Override
    public void run() {
        while (isRunning){
            try {
                Session session = Session.getDefaultInstance(properties, null);
                Store store = session.getStore("imaps");

                // System.out.println("Connection initiated...");
                store.connect(username, password);
                // System.out.println("Connection is ready: ");

                Folder inbox = store.getFolder("inbox");
                inbox.open(Folder.READ_WRITE);

                if (inbox.getUnreadMessageCount() > 0) {
                    int messageCount = inbox.getUnreadMessageCount();

                    //notify the observers
                    observers[0].update(messageCount);
                    observers[1].update(messageCount);
                    System.out.println("Total messages in Inbox: " + messageCount);
                    Message[] messages = inbox.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));

                    int temp = 0;
                    while (temp < messageCount){
                        Message message = messages[temp];
                        Email email = new Email(message.getFrom()[0].toString(), message.getSubject(), getTextFromMessage(message));
                        blockingQueue.enqueue(email);
                        message.setFlags(new Flags(Flags.Flag.SEEN), true);
                        temp++;
                    }
                } else {
                    // System.out.println("No new messages!");
                }
                inbox.close();
                store.close();
            } catch (MessagingException e){
                System.out.println("Error");
            }
        }
        // writing a different email to the queue to notify the save thread saying that this is the last email
        Email lastEmail = new Email("-1", "-1", "-1");
        blockingQueue.enqueue(lastEmail);
        return;
    }

    public void stopThread(){
        this.isRunning = false;
    }

    private String getTextFromMessage(Message message){
        String result = "";
        try {
            if (message.isMimeType("text/plain")){
                result = message.getContent().toString();
            } else if (message.isMimeType("multipart/")){
                MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
                result = getTextFromMimeMultipart(mimeMultipart);
            }
        } catch (MessagingException | IOException e){
            System.out.println("Error!");
        }
        return result;
    }

    //recursive method to read multipart emails
    private String getTextFromMimeMultipart(MimeMultipart mimeMultipart){
        String result = "";
        try {
            int count = mimeMultipart.getCount();
            for (int i = 0; i < count; i++){
                BodyPart bodyPart = mimeMultipart.getBodyPart(i);
                if (bodyPart.isMimeType("text/plain")){
                    result = result + "\n" + bodyPart.getContent();
                    break;
                } else if (bodyPart.getContent() instanceof MimeMultipart) {
                    result = result + getTextFromMimeMultipart((MimeMultipart) bodyPart.getContent());
                }
            }
        } catch (MessagingException | IOException e){
            System.out.println("error");
        }
        return result;
    }
}
