package assignment;

import java.io.*;
import java.util.*;
import java.util.Date;
import javax.mail.*;
import javax.mail.Flags.Flag;
import javax.mail.search.FlagTerm;

public class ReceiveEmail {
    private ArrayList<ReceivedMail> receivedMailArrayList = new ArrayList<>();
    private ArrayList<String> senders = new ArrayList<>();
    private ArrayList<String> subjects = new ArrayList<>();
    private ArrayList<String> contents  = new ArrayList<>();
    private ArrayList<String> dateArrayList = new ArrayList<>();
    private ReceivedMail receivedMail;
    private int count = 0;
    Folder inbox;
    //Constructor of the class.
    public ReceiveEmail()
    {
        /*  Set the mail properties  */
        Properties props = System.getProperties();
        props.setProperty("mail.store.protocol", "imaps");
        try
        {
            /*  Create the session and get the store for read the mail. */
            Session session = Session.getDefaultInstance(props, null);
            Store store = session.getStore("imaps");
            store.connect("imap.gmail.com","sahantestmail@gmail.com", "Sahantest333");

            /*  Mention the folder name which you want to read. */
            inbox = store.getFolder("Inbox");
            System.out.println("No of Unread Messages : " + inbox.getUnreadMessageCount());

            /*Open the inbox using store.*/
            inbox.open(Folder.READ_ONLY);

            /*  Get the messages which is unread in the Inbox*/
            Message messages[] = inbox.search(new FlagTerm(new Flags(Flag.SEEN), false));

            /* Use a suitable FetchProfile    */
            FetchProfile fp = new FetchProfile();
            fp.add(FetchProfile.Item.ENVELOPE);
            fp.add(FetchProfile.Item.CONTENT_INFO);
            inbox.fetch(messages, fp);


            try
            {
                printAllMessages(messages);
                inbox.close(true);
                store.close();
            }
            catch (Exception ex)
            {
                System.out.println("Exception arise at the time of read mail");
                ex.printStackTrace();
            }
        }
        catch (NoSuchProviderException e)
        {
            e.printStackTrace();
            System.exit(1);
        }
        catch (MessagingException e)
        {
            e.printStackTrace();
            System.exit(2);
        }
        count++;
    }


    public void printAllMessages(Message[] messages) throws Exception
    {
        for (int i = 0; i < messages.length; i++)
        {
            /*System.out.println("MESSAGE #" + (i + 1) + ":");*/
            printEnvelope(messages[i]);
        }
    }

    /*  Print the envelope(FromAddress,ReceivedDate,Subject)  */
    public void printEnvelope(Message message) throws Exception
    {
        Address[] a;
        // FROM
        if ((a = message.getFrom()) != null)
        {
            for (int j = 0; j < a.length; j++)
            {
                /*System.out.println("FROM: " + a[j].toString());*/
                // senders.add(a[j].toString());   // adding the sender email for the senders array
            }
        }
        senders.add(a[0].toString());   // adding the sender email for the senders array
        // senderEmails.add(senders);
        // TO
        if ((a = message.getRecipients(Message.RecipientType.TO)) != null)
        {
            for (int j = 0; j < a.length; j++)
            {
                /*System.out.println("TO: " + a[j].toString());*/
            }
        }
        String subject = message.getSubject();
        Date receivedDate = message.getReceivedDate();
        String content = message.getContent().toString();
        /*System.out.println("Subject : " + subject);*/
        subjects.add(subject);  // adding the subject to the subjects array

        /*System.out.println("Received Date : " + receivedDate.toString());*/
        dateArrayList.add(receivedDate.toString()); // adding the received date to the array

        /*System.out.println("Content : " + content);*/
        contents.add(content);
        // getContent(message);


    }

    public void getContent(Message msg)
    {
        try
        {
            String contentType = msg.getContentType();
            // System.out.println("Content Type : " + contentType);
            Multipart mp = (Multipart) msg.getContent();
            int count = mp.getCount();
            for (int i = 0; i < count; i++)
            {
                dumpPart(mp.getBodyPart(i));
            }
        }
        catch (Exception ex)
        {
            System.out.println("Exception arise at get Content");
            ex.printStackTrace();
        }
    }

    public void dumpPart(Part p) throws Exception
    {
        // Dump input stream ..
        InputStream is = p.getInputStream();
        // If "is" is not already buffered, wrap a BufferedInputStream
        // around it.
        if (!(is instanceof BufferedInputStream))
        {
            is = new BufferedInputStream(is);
        }
        int c;
        System.out.println("Message : ");
        while ((c = is.read()) != -1)
        {
            System.out.write(c);
        }
    }

    public ArrayList<ReceivedMail> createReceivedMail(){
        for (int i = 0; i < senders.size(); i++){
            this.receivedMail = new ReceivedMail(senders.get(i), subjects.get(i), contents.get(i), dateArrayList.get(i));
            this.receivedMailArrayList.add(receivedMail);
        }
        return receivedMailArrayList;
    }

    public int getCount() {
        return count;
    }
}

// need to add the email content to the array list