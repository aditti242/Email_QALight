package test;

import helpers.MailSender;
import helpers.MailSender1;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;

import static java.lang.Thread.sleep;

public class WritingPricesToFile extends BaseTest {
    @Test
    public void writingPricesToFile() throws IOException, InterruptedException {
        sleep(3000);

        mainPage.clickOnButton();
        mainPage.putPricesToFile();
        ArrayList<String> file = new ArrayList<>();
        file.add("output.txt");

        MailSender.sendMailFromFile(
                "emails.txt","FirstEmailTest","This is mail text body", file);



    }
}
