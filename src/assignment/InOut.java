package assignment;

import java.io.*;
import java.util.ArrayList;
import java.util.Locale;

public class InOut {
    // saves a recipient detail string to the text file
    public static void saveDetailToFile(String recipientDetail){
        PrintWriter outStream = null;
        try {
            outStream = new PrintWriter(new FileWriter("clientList.txt", true));    // client detail file name in this file
            outStream.println(recipientDetail);
        } catch (IOException e){
            System.out.println("Error occurred!");
        } finally {
            if (outStream != null)
                outStream.close();
        }
    }

    // saves notifications to the text file
    public static void saveNotificationToFile(String notification){
        PrintWriter outStream = null;
        try {
            outStream = new PrintWriter(new FileWriter("notification.txt", true));  // notification file name in this file
            outStream.println(notification);
        } catch (IOException e){
            System.out.println("Error Occurred!");
        } finally {
            if (outStream != null)
                outStream.close();
        }
    }

    // relevant string to be saved for a recipient
    public static void saveRecipient(Recipient recipient){
        String recipientDetail = "";

        if (recipient instanceof Personal) {
            Personal personal = (Personal) recipient;
            String name, nickname, email, birthday;
            name = personal.getName();
            nickname = personal.getNickName();
            email = personal.getEmail();
            birthday = personal.getBirthday().getYear() + "/" + personal.getBirthday().getMonth()
                    + "/" + personal.getBirthday().getDay();

            recipientDetail = "Personal: " + name + "," + nickname + "," + email + "," + birthday;

        } else if (recipient instanceof OfficeFriend) {
            OfficeFriend officialRecipientFriend = (OfficeFriend) recipient;
            String name, email, designation, birthday;
            name = officialRecipientFriend.getName();
            email = officialRecipientFriend.getEmail();
            designation = officialRecipientFriend.getDesignation();
            birthday = officialRecipientFriend.getBirthday().getYear() + "/"
                    + officialRecipientFriend.getBirthday().getMonth() + "/"
                    + officialRecipientFriend.getBirthday().getDay();

            recipientDetail = "OfficeFriend: " + name + "," + email + "," + designation + "," + birthday;

        } else if (recipient instanceof Official) {
            Official officialRecipient = (Official) recipient;
            String name, email, designation;
            name = officialRecipient.getName();
            email = officialRecipient.getEmail();
            designation = officialRecipient.getDesignation();

            recipientDetail = "Official: " + name + "," + email + "," + designation;

        }

        saveDetailToFile(recipientDetail);
    }

    // deserialize all email objects in the saved text file....if not found, then return an empty array list of mails
    public static ArrayList<Email> emails(){
        ArrayList<Object> objects = null;
        FileInputStream fileInputStream;
        ObjectInputStream objectInputStream;
        try {
            fileInputStream = new FileInputStream("sent.txt");   // object file name in this file
            objectInputStream = new ObjectInputStream(fileInputStream);
            objects = (ArrayList<Object>) objectInputStream.readObject();
            if (fileInputStream != null)    fileInputStream.close();
            if (objectInputStream != null)  objectInputStream.close();
        } catch (IOException e){
            System.out.println("Could not load previous Emails!");  // if the file is not available
        } catch (ClassNotFoundException e){
            objects = new ArrayList<>();
        }
        if (objects == null)    return new ArrayList<>();
        // System.out.println("Previous sent emails loaded!");
        return (ArrayList<Email>) (ArrayList<?>) objects;
    }

    // deserialize all received email objects in the saved file.....if not found, then return an empty array list of received emails
    public static ArrayList<ReceivedMail> receiveMails(){
        ArrayList<Object> objects = null;
        FileInputStream fileInputStream;
        ObjectInputStream objectInputStream;
        try {
            fileInputStream = new FileInputStream("received.txt");
            objectInputStream = new ObjectInputStream(fileInputStream);
            objects = (ArrayList<Object>) objectInputStream.readObject();
            if (fileInputStream != null)    fileInputStream.close();
            if (objectInputStream != null)  objectInputStream.close();
        } catch (IOException e){
            System.out.println("Could not load previous received email!");
        } catch (ClassNotFoundException e){
            objects = new ArrayList<>();
        }
        if (objects == null)    return new ArrayList<>();
        System.out.println("Previous received emails loaded!");
        return (ArrayList<ReceivedMail>) (ArrayList<?>) objects;
    }
    // serialize mails in the file
    public static void saveEmails(ArrayList<Email> emailArrayList){
        FileOutputStream fileOutputStream;
        ObjectOutputStream objectOutputStream;
        try {
            fileOutputStream = new FileOutputStream("sent.txt",true);
            objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(emailArrayList);
            if (fileOutputStream != null)   fileOutputStream.close();
            if (objectOutputStream != null) objectOutputStream.close();
        } catch (IOException e){
            System.out.println("error Occurred!");
        }
    }

    // serialize retrieved mails in the file
    public static void saveReceivedEmails(ArrayList<ReceivedMail> receivedMailArrayList){
        FileOutputStream fileOutputStream;
        ObjectOutputStream objectOutputStream;
        try{
            fileOutputStream = new FileOutputStream("received.txt");
            objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(receivedMailArrayList);
            if (fileOutputStream != null)   fileOutputStream.close();
            if (objectOutputStream != null) objectOutputStream.close();
        } catch (IOException e){
            System.out.println("eRrOr OcCuRrEd!");
        }
    }

    // serialize received email objects in to hard drive
    public static void saveRetrievedEmails(ReceivedMail receivedMail){
        try{
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("received.txt", true)){
                protected void writeStream(){
                    try {
                        reset();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };
            objectOutputStream.writeObject(receivedMail);
            objectOutputStream.close();
        } catch (IOException e){
            System.out.println("ERROR OCCURRED!");
        }
    }
    // read and returns all the recipient objects given in the string lines from the file
    public static ArrayList<Recipient> loadRecipients(){
        ArrayList<Recipient> recipients = new ArrayList<>();
        ArrayList<String> recipientDetail = readFromFile();

        if (recipientDetail.isEmpty()){ return recipients;
        } else {
            // System.out.println("Recipient detail loaded!");
            String[] detailParts;
            Recipient recipient;
            for (int i = 0; i < recipientDetail.size(); i++){
                detailParts = recipientDetail.get(i).split(":");
                recipient = createRecipient(detailParts);   // creating recipient according to the type
                if (recipient != null) {
                    recipients.add(recipient);
                } else {
                    continue;
                }
            }
        }
        return recipients;
    }

    // reads all recipient details from the file
    public static ArrayList<String> readFromFile() {
        ArrayList<String> recipientDetails = new ArrayList<>();
        FileReader fileIn;
        BufferedReader in;
        try {
            fileIn = new FileReader("clientList.txt");
            in = new BufferedReader(fileIn);
            String line;
            while ((line = in.readLine()) != null) {
                recipientDetails.add(line);

            }
            in.close();
            fileIn.close();

        } catch (Exception e) {
            System.out.println("Could not load Recipient Details");
        }

        return recipientDetails;

    }

    // returns relevant recipient object
    public static Recipient createRecipient(String[] detailParts){
        String[] detailArray;
        String[] birthdayArray;
        Date birthday;
        switch (detailParts[0].trim().toLowerCase(Locale.ROOT)){
            case "official":
                detailArray = detailParts[1].trim().split(",");
                return new Official(detailArray[0], detailArray[1], detailArray[2]);
            case "officefriend":
                detailArray = detailParts[1].trim().split(",");
                birthdayArray = detailArray[3].split("/");
                birthday = new Date(birthdayArray[0], birthdayArray[1], birthdayArray[2]);
                return new OfficeFriend(detailArray[0], detailArray[1], detailArray[2], birthday);
            case "personal":
                detailArray = detailParts[1].trim().split(",");
                birthdayArray = detailArray[3].split("/");
                birthday = new Date(birthdayArray[0], birthdayArray[1], birthdayArray[2]);
                return new Personal(detailArray[0], detailArray[1], detailArray[2], birthday);
            default:
                return null;
        }
    }
}
