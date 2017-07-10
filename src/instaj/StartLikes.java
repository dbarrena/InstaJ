/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package instaj;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import java.io.IOException;
import java.util.ArrayList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 *
 * @author Diego
 */
public class StartLikes {
    
    WebDriver driver;
    Utils utils = new Utils();
    int timeBetweenLikes;
    
    public StartLikes(ArrayList<String> links, TextField timeBetweenLikes, TextArea logbox, WebDriver driver) throws InterruptedException {
        this.timeBetweenLikes = Integer.parseInt(timeBetweenLikes.getText());
        this.driver = driver;
        startLikesThread(logbox, links);
    }
    
    public void goToPage(String link) {
        this.driver.get(link);
    }
    
    public void likePosts(TextArea logbox, ArrayList<String> links) {
        for (int x = 0; x < links.size(); x++) {
            goToPage(links.get(x));
            if (isAvailable()) {
                WebElement fav = utils.fluentWait(By.xpath("//*[@id=\"react-root\"]/section/main/div/div/article/div[2]/section[1]/a[1]"), driver);
                fav.click();
                utils.addLogLine(logbox, "[+] like a: " + links.get(x));
                utils.waitGivenSeconds(timeBetweenLikes);
            } else {
                utils.addLogLine(logbox, "[+]PAGINA NO DISPONIBLE! CONTINUANDO CON LA SIGUIENTE");
            }
        }
    }
    
    public Task<Void> likesTask(TextArea logbox, ArrayList<String> links) throws InterruptedException {
        return new Task<Void>() {
            
            @Override
            public Void call() throws InterruptedException, FailingHttpStatusCodeException, IOException {
                utils.addLogLine(logbox, "[+] Links cargados! Empezando likes...");
                likePosts(logbox, links);
                return null;
            }
        };
    }
    
    public void startLikesThread(TextArea logbox, ArrayList<String> links) throws InterruptedException {
        Task<Void> downloaderTask = likesTask(logbox, links);
        
        downloaderTask.setOnSucceeded((WorkerStateEvent t) -> {
            utils.addLogLine(logbox, "[+] Likes terminados!");
            downloaderTask.cancel();
        });
        
        downloaderTask.setOnFailed((WorkerStateEvent t) -> {
            // Code to run once runFactory() **fails**
        });
        
        new Thread(downloaderTask).start();
    }
    
    public boolean isAvailable() {
        boolean isAvailable = true;
        WebElement pageBody = utils.fluentWait(By.tagName("body"), driver);
        String bodyClass = pageBody.getAttribute("class");
        if (bodyClass.equals(" p-error dialog-404")) {
            isAvailable = false;
        }
        return isAvailable;
    }
    
}
