/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package instaj;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.openqa.selenium.WebDriver;

/**
 *
 * @author Diego
 */
public class LoginController {

    final BooleanProperty firstTime = new SimpleBooleanProperty(true);
    WebDriver driver;

    ProgressIndicator pi = new ProgressIndicator();
    @FXML
    Button loginBtn;

    @FXML
    VBox vbox;

    @FXML
    TextField user;

    @FXML
    PasswordField pass;

    @FXML
    Label hint1, hint2, hint3;

    @FXML
    Separator separator1, separator2;

    public void loginBtnBehavior() {
        System.out.println("LOCAL ACC BTN PRESSED");
        pi.setStyle(" -fx-progress-color: #9a3bab;");
        pi.setMinWidth(40);
        pi.setMinHeight(40);
        vbox.getChildren().removeAll(loginBtn, hint1, hint2, hint3, separator1, separator2);
        vbox.getChildren().add(pi);
        vbox.requestFocus();
        try {
            startLoginThread(vbox, 1);
        } catch (InterruptedException ex) {

        }
    }

    public Task<Void> useLocalInstagramProfile() throws InterruptedException {
        return new Task<Void>() {

            @Override
            public Void call() throws InterruptedException, FailingHttpStatusCodeException, IOException {
                SeleniumVariables selenium = new SeleniumVariables();
                selenium.setChromeProfile();
                Utils utils = new Utils();
                selenium.getDriver().get("https://www.instagram.com/");
                driver = selenium.getDriver();
                return null;
            }
        };
    }

    public void startLoginThread(VBox vbox, int selector) throws InterruptedException {
        Task<Void> downloaderTask = null;

        downloaderTask = useLocalInstagramProfile();

        downloaderTask.setOnSucceeded((WorkerStateEvent t) -> {
            Stage stage = null;
            //get reference to the button's stage         
            stage = (Stage) pi.getScene().getWindow();
            try {
                System.out.println("DRIVER: " + driver);
                showMainView(driver, stage);
            } catch (IOException ex) {
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        downloaderTask.setOnFailed((WorkerStateEvent t) -> {
            // Code to run once runFactory() **fails**
        });

        new Thread(downloaderTask).start();
    }

    public void showMainView(WebDriver driver, Stage stage) throws IOException { // Taking the client-object as an argument from LoginViewController
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(LoginController.class.getResource("MainScreenFXML.fxml"));
        Parent mainLayout = loader.load();
        MainScreenFXMLController cvc = loader.getController(); // This did the "trick"
        cvc.setDriver(driver); // Passing the client-object to the ClientViewController
        try {
            startGUIThread(cvc, mainLayout, stage);
        } catch (InterruptedException ex) {

        }
    }

    public Task<Void> guiTask(MainScreenFXMLController cvc) throws InterruptedException {
        return new Task<Void>() {

            @Override
            public Void call() throws InterruptedException, FailingHttpStatusCodeException, IOException {
                cvc.initGUI();
                return null;
            }
        };
    }

    public void startGUIThread(MainScreenFXMLController cvc, Parent mainLayout, Stage stage) throws InterruptedException {
        Task<Void> downloaderTask = null;

        downloaderTask = guiTask(cvc);

        downloaderTask.setOnSucceeded((WorkerStateEvent t) -> {
            Scene scene = new Scene(mainLayout, 700, 610);
            stage.setScene(scene);
            stage.show();
        });

        downloaderTask.setOnFailed((WorkerStateEvent t) -> {
            // Code to run once runFactory() **fails**
        });

        new Thread(downloaderTask).start();
    }
}
