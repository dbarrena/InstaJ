/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package instaj;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * FXML Controller class
 *
 * @author Diego
 */
public class MainScreenFXMLController {

    Utils utils = new Utils();

    WebDriver driver;

    @FXML
    ImageView profilePicture;

    @FXML
    Label username, currentFollowers, currentFollowing;

    @FXML
    TextArea logbox, tagsList;

    @FXML
    TextField numberOfLikes, timeBetweenLikes;

    /**
     * Initializes the controller class.
     *
     * @param driver
     */
    public void setDriver(WebDriver driver) {
        this.driver = driver;
    }

    public void startBtnBehavior() throws InterruptedException {
        GetInstagramLinks getLinks = new GetInstagramLinks(driver, tagsList, numberOfLikes, logbox, timeBetweenLikes);
    }

    public void initGUI() {
        WebElement userLink = utils.fluentWait(By.xpath("//*[@id=\"react-root\"]/section/nav/div[2]/div/div/div[3]/div/div[3]/a"), driver);
        userLink.click();

        WebElement webUsername = utils.fluentWait(By.xpath("//*[@id=\"react-root\"]/section/main/article/header/div[2]/div[1]/h1"), driver);
        WebElement webFollowers = utils.fluentWait(By.xpath("//*[@id=\"react-root\"]/section/main/article/header/div[2]/ul/li[2]/a/span"), driver);
        WebElement webFollowing = utils.fluentWait(By.xpath("//*[@id=\"react-root\"]/section/main/article/header/div[2]/ul/li[3]/a/span"), driver);
        this.username.setText(webUsername.getText());
        this.currentFollowers.setText(webFollowers.getText());
        this.currentFollowing.setText(webFollowing.getText());

        WebElement profilePictureElement = utils.fluentWait(By.cssSelector("#react-root > section > main > article > header > div._o0ohn > div > button > img"), driver);
        String profilePicLink = profilePictureElement.getAttribute("src");
        Image img = new Image(profilePicLink);
        profilePicture.setImage(img);
        logbox.setText("*** InstaJ Bot by Diego Barrena ***");
        autoScrollLogbox(logbox);
        numberOfLikes.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                timeBetweenLikes.setDisable(false);
                timeBetweenLikes.setPromptText("");
            } else {
                timeBetweenLikes.setDisable(true);
                timeBetweenLikes.setPromptText("minutos");
            }
        });
    }

    public void autoScrollLogbox(TextArea logbox) {
        logbox.textProperty().addListener(new ChangeListener<Object>() {
            @Override
            public void changed(ObservableValue<?> observable, Object oldValue,
                    Object newValue) {
                logbox.setScrollTop(Double.MAX_VALUE); //this will scroll to the bottom
                //use Double.MIN_VALUE to scroll to the top
            }
        });
    }

}
