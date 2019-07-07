package helpers;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import java.util.Scanner;

public class MailSender1 {

    public static void sendEmailWithAttachments(String host, String port, String userName, String password, String toAddress,
                                                String subject, String message, String[] attachFiles)
            throws MessagingException {
        // задаём параметры smtp сервера
        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.user", userName);
        properties.put("mail.password", password);

        // создание новой сессии с аутентификацией
        Authenticator auth = new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(userName, password);
            }
        };
        Session session = Session.getInstance(properties, auth);

        // создание нового e-mail сообщения
        Message msg = new MimeMessage(session);

        msg.setFrom(new InternetAddress(userName));
        InternetAddress[] toAddresses = { new InternetAddress(toAddress) };
        msg.setRecipients(Message.RecipientType.TO, toAddresses);
        msg.setSubject(subject);

        msg.setSentDate(new Date());

        // создаём текстовую часть
        MimeBodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setContent(message, "text/html");

        // создаём многосоставной e-mail
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);

        // добавление вложений
        if (attachFiles != null && attachFiles.length > 0) {
            for (String filePath : attachFiles) {
                MimeBodyPart attachPart = new MimeBodyPart();

                try {
                    attachPart.attachFile(filePath);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

                multipart.addBodyPart(attachPart);
            }
        }

        // указываем multipart как e-mail контент
        msg.setContent(multipart);

        // отправка e-mail
        Transport.send(msg);

    }

    public static void mailFromfile (String myfile, String subject, String message,
                                     ArrayList<String> files) throws IOException {
        String host = "smtp.gmail.com";
        String port = "587";
        String mailFrom = "aditti030188@gmail.com";
        String password = "030188Qwerty";

        int countOfFiles = files.size();
        String[] attachFiles = new String[countOfFiles];
        for (int i=0; i<countOfFiles; i++){
            attachFiles[i]= files.get(i);
        }
        File file = new File(myfile);
        Scanner fileScanner = new Scanner(file);
        while (fileScanner.hasNextLine()) {
            try {
                String lineFromFile = fileScanner.nextLine();
                sendEmailWithAttachments(host, port, mailFrom, password, lineFromFile,
                        subject, message, attachFiles);
                System.out.printf("Email to %s sent. \n", lineFromFile);
            } catch (Exception ex) {
                System.out.println("Could not send email.");
                ex.printStackTrace();
            }
        }
    }
}