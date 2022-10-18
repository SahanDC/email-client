package assignment;

import java.util.ArrayList;

// This class will create a new EmailClient
public class EmailSystem {
    private ArrayList<Recipient> allRecipients;
    private ArrayList<IGreet> greetRecipients;
    private ArrayList<Email> previousEmail;
    private ArrayList<Email> todayMail = new ArrayList<>();
    private ArrayList<ReceivedMail> previousReceiveEmail = new ArrayList<>();
    private ArrayList<ReceivedMail> todayReceivedMail = new ArrayList<>();
    private ReceivedMail receivedMail;
    private Save save;
    private Receive receive;
    private BlockingQueue queue;

    public EmailSystem() {
        // loading all previous emails and saved recipients
        this.allRecipients = InOut.loadRecipients();
        this.greetRecipients = loadGreetRecipients();
        this.previousEmail = InOut.emails();
        // this.previousReceiveEmail = InOut.receiveMails();
        this.queue = new BlockingQueue(5);
        this.receive = new Receive(queue, new EmailStatPrinter(), new EmailStatRecorder());
        this.save = new Save(queue);
        System.out.println("\nStarting Threads");
        receive.start();
        save.start();
    }

    // recipients who have a birthday saved
    private ArrayList<IGreet> loadGreetRecipients(){
        ArrayList<IGreet> greetArrayList = new ArrayList<>();
        if (!this.allRecipients.isEmpty()) {
            for (int i = 0; i < this.allRecipients.size(); i++) {
                if (this.allRecipients.get(i) instanceof IGreet)
                    greetArrayList.add((IGreet) this.allRecipients.get(i));
            }
        }
        return greetArrayList;
    }

    // adds a user
    public void addRecipient(String recipientDetails){
        Recipient recipient = InOut.createRecipient(recipientDetails.split(":"));
        InOut.saveRecipient(recipient);
        this.allRecipients.add(recipient);
        if (recipient instanceof IGreet)
            this.greetRecipients.add((IGreet) recipient);
    }

    // send an email to a user given email
    public void sendEmail(String emailDetails) {
        String[] emailParts = emailDetails.trim().split(",");
        Email sendingEmail = new Email(emailParts[0], emailParts[1], emailParts[2]);
        System.out.println("\nSending your email...");
        SendEmail.sendEmail(sendingEmail);
        System.out.println("Your email was sent successfully");
        this.todayMail.add(sendingEmail);
    }

    // prints received mails
    public void receiveEmails(ReceiveEmail receiveEmail){
        ArrayList<ReceivedMail> receivedMailArrayList = receiveEmail.createReceivedMail();
        try {
            if (receiveEmail.getCount() != 0) {
                for (int i = 0; i < receiveEmail.getCount(); i++) {
                    this.todayReceivedMail.add(receivedMailArrayList.get(i));
                }
            } else System.out.println("no emails received today!");
        } catch (IndexOutOfBoundsException e){
            System.out.println("No emails received after the last checking!");
        }
    }
    // prints all recipients who have birthdays on a given date
    public void printBirthdayOnGivenDate(Date date){
        ArrayList<IGreet> birthdayRecipient;
        birthdayRecipient = getBirthdayRecipients(this.greetRecipients, date);
        if (birthdayRecipient.isEmpty())
            System.out.println("\nNo birthdays on " + date.getYear() + "/" + date.getMonth() + "/" + date.getDay());
        else{
            System.out.println("\nBirthdays on " + date.getYear() + "/" + date.getMonth() + "/" + date.getDay());
            for (int i = 0 ; i < birthdayRecipient.size(); i++)
                System.out.println(birthdayRecipient.get(i).getName());
        }
    }

    // return all recipients who have birthdays on a given date
    private ArrayList<IGreet> getBirthdayRecipients(ArrayList<IGreet> allBirthdayRecipients, Date date){
        ArrayList<IGreet> birthdayRecipients = new ArrayList<>();
        if (allBirthdayRecipients.isEmpty()) return birthdayRecipients;
        else {
            for (int i = 0; i < allBirthdayRecipients.size(); i++){
                if (allBirthdayRecipients.get(i).getBirthday().getDay().equals(date.getDay()) && allBirthdayRecipients.get(i).getBirthday().getMonth().equals(date.getMonth()))
                    birthdayRecipients.add(allBirthdayRecipients.get(i));
            }
        }
        return birthdayRecipients;
    }
    // print all mails sent on a given date
    public void printEmailDetailOnGivenDate(Date date){
        ArrayList<Email> emailsOnGivenDate = getEmailDetailOnGivenDate(this.previousEmail, date);
        if (emailsOnGivenDate.isEmpty()) System.out.println("\nNo emails on " + date.getYear() + "/" + date.getMonth() + "/" + date.getDay());
        else{
            System.out.println("\nEmails on "+ date.getYear() + "/" + date.getMonth() + "/" + date.getDay() +"\n");
            for (int i = 0; i < emailsOnGivenDate.size(); i++)
                System.out.println("Receiver: " + emailsOnGivenDate.get(i).getRecipientEmail() + "\nSubject: " + emailsOnGivenDate.get(i).getSubject() + "\nContent: " + emailsOnGivenDate.get(i).getContent() + "\n");
        }
    }

    // returns all mails sent on a given date
    private ArrayList<Email> getEmailDetailOnGivenDate(ArrayList<Email> emails, Date date){
        ArrayList<Email> emailsOnGivenDate = new ArrayList<>();
        if (emails.isEmpty()) {
            return emailsOnGivenDate;
        }
        else {
            for (int i = 0 ; i < emails.size(); i++){
                if (emails.get(i).getSentDate().getYear().equals(date.getYear()) && emails.get(i).getSentDate().getMonth().equals(date.getMonth()) && emails.get(i).getSentDate().getDay().equals(date.getDay()))
                    emailsOnGivenDate.add(emails.get(i));
            }
            return emailsOnGivenDate;
        }
    }

    // print the number of recipients in email client
    public void printNumberOfRecipients() {
        System.out.println("\nNumber of recipients = " + this.allRecipients.size());
    }

    // send wishes for everyone who has birthdays today. This runs every time EmailClient Runs
    public void sendWishes() {
        System.out.println("Checking for birthdays...");
        Date today = new Date();
        ArrayList<IGreet> todayBirthdayRecipients;
        todayBirthdayRecipients = getBirthdayRecipients(this.greetRecipients, today);
        if (todayBirthdayRecipients.isEmpty()) System.out.println("No Birthdays today!");
        else {
            for (int i = 0; i < todayBirthdayRecipients.size(); i++){
                if (todayBirthdayRecipients.get(i) instanceof Personal){
                    sendPersonal((Personal) todayBirthdayRecipients.get(i));
                } else if (todayBirthdayRecipients.get(i) instanceof OfficeFriend){
                    sendOfficial((OfficeFriend) todayBirthdayRecipients.get(i));
                }
            }
        }
    }

    // send wishes to a personal friend
    private void sendPersonal(Personal personal){
        Email wishEmail = new Email(personal.getEmail(), "Birthday Wish!", "Dear " + personal.getNickName() + ", Wishing you a happy birthday!");
        SendEmail.sendEmail(wishEmail);
        this.todayMail.add(wishEmail);
    }

    private void sendOfficial(OfficeFriend friend){
        Email wishEmail = new Email(friend.getEmail(), "Birthday!", "Happy Birthday " + friend.getName());
        SendEmail.sendEmail(wishEmail);
        this.todayMail.add(wishEmail);
    }

    // serialize all email Objects
    public void serializeEmail(){
        ArrayList<Email> emailArrayList = new ArrayList<>();
        emailArrayList.addAll(this.previousEmail);
        emailArrayList.addAll(this.todayMail);
        InOut.saveEmails(emailArrayList);
        System.out.println("\nSaved to Disk!");
    }

    // serialize all received email objects
    public void serializeReceivedEmail(){
        ArrayList<ReceivedMail> receivedMailArrayList = new ArrayList<>();
        receivedMailArrayList.addAll(this.previousReceiveEmail);
        receivedMailArrayList.addAll(this.todayReceivedMail);
        InOut.saveReceivedEmails(receivedMailArrayList);
        System.out.println("\nReceived emails saved to hard drive!");
    }

    // serialize all received email objects
    public void serializeRetrievedEmail(ReceiveEmail receiveEmail){
        ArrayList<ReceivedMail> receivedMailArrayList = receiveEmail.createReceivedMail();
        try {
            if (receiveEmail.getCount() != 0) {
                for (int i = 0; i < receiveEmail.getCount(); i++) {
                    this.receivedMail = new ReceivedMail(receivedMailArrayList.get(i).getSenderEmail(), receivedMailArrayList.get(i).getSubject(), receivedMailArrayList.get(i).getContent()
                                                            , receivedMailArrayList.get(i).getReceivedDate());
                    InOut.saveRetrievedEmails(this.receivedMail);
                }
            } else System.out.println("no emails received today!");
        } catch (IndexOutOfBoundsException e){
            // System.out.println("No emails received after the last checking!");
        } finally {
            System.out.println("Received emails saved to hard drive!");
        }


    }

    // Closing threads
    public void closeThread(){
        System.out.println("Closing threads...");
        this.receive.stopThread();
        try {
            this.save.join();
        } catch (InterruptedException e) {
            System.out.println("interrupted!");
        }
        System.out.println("Thread closed!");
    }
}
