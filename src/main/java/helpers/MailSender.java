package helpers;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import java.util.Scanner;

public class MailSender {
    public static void sendMailSettings(String user, String password, String port, String host, String toAddress, String subject,
                                        String[] attachFiles) throws MessagingException, IOException {
        Properties properties = new Properties();
        properties.put("mail.user", user);
        properties.put("mail.password", password);
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        Authenticator auth = new Authenticator() {

            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user, password);
            }
        };
        Session session = Session.getInstance(properties, auth);
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(user));
        InternetAddress[] toAddresses = {new InternetAddress(toAddress)};
        message.setRecipients(Message.RecipientType.TO, toAddresses);
        message.setSubject(subject);
        message.setSentDate(new Date());
        MimeBodyPart bodyPart = new MimeBodyPart();
        bodyPart.setContent(message, "txt/html");
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(bodyPart);
        if (attachFiles != null && attachFiles.length > 0) {
            for (String filePath : attachFiles) {
                MimeBodyPart attachPart = new MimeBodyPart();
                try {
                    attachPart.attachFile(filePath);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
                multipart.addBodyPart(attachPart);
            }
            message.setContent(multipart);
            Transport.send(message);
        }
    }

    public static void sendMailFromFile(String myFile, String subject, String message,
                                        ArrayList<String> files) throws FileNotFoundException {
        String user = "sanitarskiytest@gmail.com";
        String password = "123QWEqwe";
        String host = "smtp.gmail.com";
        String port = "587";

        int countOfFiles = files.size();
        String[] attachFiles = new String[countOfFiles];
        for (int i = 0; i < countOfFiles; i++) {
            attachFiles[i] = files.get(i);
        }

        File file = new File(myFile);
        Scanner fileScanner = new Scanner(file);
        while (fileScanner.hasNextLine()) {
            try {
                String linefromFile = fileScanner.nextLine();
                sendMailSettings(user, password, port, host, linefromFile, subject, attachFiles);
                System.out.printf("Mail for %s sent\n", linefromFile);
            } catch (MessagingException e) {
                System.out.println("Could not sent message");
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
