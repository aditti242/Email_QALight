package helpers;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import java.util.Scanner;
import javax.activation.CommandMap;
import javax.activation.MailcapCommandMap;





public class MailSender {
    public static void sendMailSettings(String host, String port, String user, String password, String toAddress,
                                        String subject, String message, String[] attachFiles) throws MessagingException {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.user", user);
        properties.put("mail.password", password);

        Authenticator auth = new Authenticator() {

            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user, password);
            }
        };
        Session session = Session.getInstance(properties, auth);
        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(user));
        InternetAddress[] toAddresses = {new InternetAddress(toAddress)};
        msg.setRecipients(Message.RecipientType.TO, toAddresses);
        msg.setSubject(subject);
        msg.setSentDate(new Date());
        MimeBodyPart bodyPart = new MimeBodyPart();
        bodyPart.setContent(message, "text/html");
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(bodyPart);
        if (attachFiles != null && attachFiles.length > 0) {
            for (String filePath : attachFiles) {
                MimeBodyPart attachPart = new MimeBodyPart();
                try {
                    attachPart.attachFile(filePath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                multipart.addBodyPart(attachPart);
            }
            msg.setContent(multipart);
            Transport.send(msg);
        }
    }

    public static void sendMailFromFile(String myFile, String subject, String message,
                                        ArrayList<String> files) throws IOException {
        String host = "smtp.gmail.com";
        String port = "587";
        String mailFrom = "aditti030188@gmail.com";
        String password = "030188Qwerty";

        int countOfFiles = files.size();
        String[] attachFiles = new String[countOfFiles];
        for (int i = 0; i < countOfFiles; i++) {
            attachFiles[i] = files.get(i);
        }

        File file = new File(myFile);
        Scanner fileScanner = new Scanner(file);
        while (fileScanner.hasNextLine()) {
            try {
                String lineFromFile = fileScanner.nextLine();
                sendMailSettings( host, port, mailFrom, password, lineFromFile,
                        subject, message, attachFiles);
                System.out.printf("Email to %s sent. \n", lineFromFile);
            } catch (MessagingException e) {
                System.out.println("Could not sent message");
                e.printStackTrace();

            }
        }
    }
}
