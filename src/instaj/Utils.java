/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package instaj;

import com.google.common.base.Function;
import java.util.concurrent.TimeUnit;
import javafx.beans.property.BooleanProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

/**
 *
 * @author Diego
 */
public class Utils {
    public WebElement fluentWait(final By locator, WebDriver driver) {
        Wait<WebDriver> wait = new FluentWait<>(driver)
                .withTimeout(120, TimeUnit.SECONDS)
                .pollingEvery(5, TimeUnit.SECONDS)
                .ignoring(NoSuchElementException.class);

        WebElement foo = wait.until((Function<WebDriver, WebElement>) (WebDriver driver1) -> driver1.findElement(locator));

        return foo;
    }

    public String generateRandomString() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        String pwd = RandomStringUtils.random(15, characters);
        return pwd;
    }
    
    public void waitGivenSeconds(int seconds){
        int milli = seconds*1000;
        try{
            Thread.sleep(milli);
        }catch (InterruptedException e) { 
             // Restore the interrupted status
             Thread.currentThread().interrupt();
         }
    }
    
    public void removeInitialFocus(TextField tf, VBox vBox, BooleanProperty firstTime) {
        tf.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue && firstTime.get()) {
                vBox.requestFocus(); // Delegate the focus to container
                firstTime.setValue(false); // Variable value changed for future references
            }
        });
    }
    
    public void addLogLine(TextArea logbox, String newLine){
        logbox.setText(logbox.getText()+"\n"+newLine);
        logbox.appendText("");
    }
    
}

