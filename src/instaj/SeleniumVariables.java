/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package instaj;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

/**
 *
 * @author Diego
 */
public class SeleniumVariables {

    WebDriver driver;

    public void setChromeDriver() {
        String exePath = "C:\\Users\\Diego\\Desktop\\chromedriver.exe";
        System.setProperty("webdriver.chrome.driver", exePath);
        this.driver = new ChromeDriver();
        System.out.println("Chrome Driver loaded, using new profile.");
    }

    public WebDriver getDriver() {
        return driver;
    }

    public void setChromeProfile() {
        String exePath = "C:\\Users\\Diego\\Desktop\\chromedriver.exe";
        System.setProperty("webdriver.chrome.driver", exePath);
        ChromeOptions options = new ChromeOptions();
        options.addArguments("user-data-dir=C:\\Users\\Diego\\AppData\\Local\\Google\\Chrome\\User Data");
        this.driver = new ChromeDriver(options);
        System.out.println("Chrome Driver loaded, using local profile.");
    }
}
