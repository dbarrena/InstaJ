/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package instaj;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author Diego
 */
public class FXMLDocumentController implements Initializable {

    final Image InstagramLogo = new Image("file:img/logo.png");
    ImageView scLogoContainer = new ImageView();
    Button button123 = new Button("hola");
    
    @FXML
    private Label label;

    @FXML
    private Button button;

    @FXML
    private void handleButtonAction(ActionEvent event) throws IOException {
        scLogoContainer.setImage(InstagramLogo);
        System.out.println("You clicked me!");
        label.setText("Hello World!");
        Stage stage = null;
        Parent root = null;
        if (event.getSource() == button) {
            //get reference to the button's stage         
            stage = (Stage) button.getScene().getWindow();
            //load up OTHER FXML document
            
            root = FXMLLoader.load(getClass().getResource("LoginFXML.fxml"));
        }
        //create a new scene with root and set the stage
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

}
