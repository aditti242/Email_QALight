package test;

import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import pages.MainPage;

import java.util.concurrent.TimeUnit;

public class BaseTest {
    public WebDriver webDriver;
    public MainPage mainPage;



    @Before
    public void setup() {
        setDriver();
        mainPage = new MainPage(webDriver);
        webDriver.get("https://qalight.com.ua/");
    }

    public void setDriver() {
        String browser = "chrome";
        switch (browser) {
            case "chrome":
                System.setProperty("webdriver.chrome.driver", "c:\\chromedriver.exe" +
                        "");
                webDriver = new ChromeDriver();
                break;

            default:
                throw new WebDriverException();
        }

        webDriver.manage().window().maximize();
        webDriver.manage().timeouts().implicitlyWait(18, TimeUnit.SECONDS);
    }

    @After
    public void closeAll() {
        webDriver.quit();
    }
}
