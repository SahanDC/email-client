package assignment;

import java.util.Scanner;

public class EmailClient {
    public static void main(String[] args) {
        // instance of a new email client
        EmailSystem emailClient = new EmailSystem();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter option type: \n"
                + "1 - Adding a new recipient\n"
                + "2 - Sending an email\n"
                + "3 - Printing out all the recipients who have birthdays\n"
                + "4 - Printing out details of all the emails sent\n"
                + "5 - Printing out the number of recipient objects in the application");
        boolean isActive = true;
        while (isActive){
            int option = Integer.parseInt(scanner.nextLine().trim());
            switch(option) {
                case 1:
                    System.out.println("Enter the Details of the Recipient below:");
                    String recipientDetail = scanner.nextLine().trim();
                    emailClient.addRecipient(recipientDetail);
                    System.out.println("\nRecipient was added successfully!");
                    break;
                case 2:
                    System.out.println("Enter the details of the receiver email to send (email, subject, content):");
                    String email = scanner.nextLine().trim();
                    emailClient.sendEmail(email);
                    break;
                case 3:
                    System.out.println("Enter the date to display birthdays :");
                    System.out.println("Input format - yyyy/mm/dd");
                    String[] dateArray = scanner.nextLine().trim().split("/");
                    Date date = new Date(dateArray[0], dateArray[1], dateArray[2]);
                    emailClient.printBirthdayOnGivenDate(date);
                    break;
                case 4:
                    System.out.println("Enter the date to display all sent emails:");
                    System.out.println("Input format - yyyy/mm/dd");
                    String[] dateParts = scanner.nextLine().trim().split("/");
                    Date givenDate = new Date(dateParts[0], dateParts[1], dateParts[2]);
                    emailClient.printEmailDetailOnGivenDate(givenDate);
                    break;
                case 5:
                    System.out.println("Printing out the number of recipient objects in the application");
                    emailClient.printNumberOfRecipients();
                    break;
                case -1:
                    isActive = false;
                    System.out.println("System stopped!");
                    break;
                default:
                    System.out.println("Invalid Input!"+"\n");
                    break;
            }
        }
        emailClient.closeThread();
        emailClient.sendWishes();
        emailClient.serializeRetrievedEmail(new ReceiveEmail());
        emailClient.serializeEmail();
        scanner.close();

        System.out.println("Have a nice Day!");
    }
}
