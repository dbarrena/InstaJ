/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package instaj;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 *
 * @author Diego
 */
public class GetInstagramLinks {

    Utils utils = new Utils();

    ArrayList<String> tags = new ArrayList<>();
    ArrayList<String> links = new ArrayList<>();
    int numberOfLikes;
    WebDriver driver;

    public GetInstagramLinks(WebDriver driver, TextArea tagsList, TextField numberOfLikes, TextArea logbox, TextField timeBetweenLikes) throws InterruptedException {
        tags.add(tagsList.getText());
        this.numberOfLikes = Integer.parseInt(numberOfLikes.getText());
        this.driver = driver;
        startLinksThread(logbox, timeBetweenLikes);
    }

    public void searchTag(String tag) {
        String baseURL = "https://www.instagram.com/explore/tags/";
        this.driver.get(baseURL + tag);
    }

    private void getLinks(TextArea logbox) throws InterruptedException {
        utils.addLogLine(logbox, "[+] Obteniendo links con los tags especificados...");
        int numberOfRows = (int) Math.ceil(((float) numberOfLikes / 3));
        if (numberOfLikes > 12) {
            searchTag(tags.get(0));
            int numberOfPageDowns = numberOfLikes / 12;

            WebElement loadMoreBtn = utils.fluentWait(By.xpath("//*[@id=\"react-root\"]/section/main/article/div[2]/a"), driver);
            loadMoreBtn.click();
            WebElement pageBody = utils.fluentWait(By.tagName("body"), driver);

            for (int x = 0; x < numberOfPageDowns; x++) {
                pageBody.sendKeys(Keys.PAGE_DOWN);
                pageBody.sendKeys(Keys.PAGE_DOWN);
                utils.waitGivenSeconds(3);
            }

            for (int x = 0; x < numberOfRows; x++) {
                int row = x + 1;
                WebElement firstPhoto = utils.fluentWait(By.xpath("//*[@id=\"react-root\"]/section/main/article/div[2]/div[1]/div[" + row + "]/div[1]/a"), driver);
                WebElement secondPhoto = utils.fluentWait(By.xpath("//*[@id=\"react-root\"]/section/main/article/div[2]/div[1]/div[" + row + "]/div[2]/a"), driver);
                WebElement thirdPhoto = utils.fluentWait(By.xpath("//*[@id=\"react-root\"]/section/main/article/div[2]/div[1]/div[" + row + "]/div[3]/a"), driver);

                String firstLink = firstPhoto.getAttribute("href");
                String secondLink = secondPhoto.getAttribute("href");
                String thirdLink = thirdPhoto.getAttribute("href");

                //Instagram adds "tagged=" after "?" in URL, possible bot detection method?
                String[] firstLinkStripped = firstLink.split("\\?");
                String[] secondLinkStripped = secondLink.split("\\?");
                String[] thirdLinkStripped = thirdLink.split("\\?");

                List<String> tempLinks = Arrays.asList(firstLinkStripped[0], secondLinkStripped[0], thirdLinkStripped[0]);
                links.addAll(tempLinks);
            }
        } else {
            searchTag(tags.get(0));
            for (int x = 0; x < numberOfRows; x++) {
                int row = x + 1;
                WebElement firstPhoto = utils.fluentWait(By.xpath("//*[@id=\"react-root\"]/section/main/article/div[2]/div[1]/div[" + row + "]/div[1]/a"), driver);
                WebElement secondPhoto = utils.fluentWait(By.xpath("//*[@id=\"react-root\"]/section/main/article/div[2]/div[1]/div[" + row + "]/div[2]/a"), driver);
                WebElement thirdPhoto = utils.fluentWait(By.xpath("//*[@id=\"react-root\"]/section/main/article/div[2]/div[1]/div[" + row + "]/div[3]/a"), driver);

                String firstLink = firstPhoto.getAttribute("href");
                String secondLink = secondPhoto.getAttribute("href");
                String thirdLink = thirdPhoto.getAttribute("href");

                String[] firstLinkStripped = firstLink.split("\\?");
                String[] secondLinkStripped = secondLink.split("\\?");
                String[] thirdLinkStripped = thirdLink.split("\\?");

                utils.addLogLine(logbox, firstLinkStripped[0]);
                utils.addLogLine(logbox, secondLinkStripped[0]);
                utils.addLogLine(logbox, thirdLinkStripped[0]);

                List<String> tempLinks = Arrays.asList(firstLinkStripped[0], secondLinkStripped[0], thirdLinkStripped[0]);
                links.addAll(tempLinks);
            }
        }
    }

    public Task<Void> linksTask(TextArea logbox) throws InterruptedException {
        return new Task<Void>() {

            @Override
            public Void call() throws InterruptedException, FailingHttpStatusCodeException, IOException {
                getLinks(logbox);
                return null;
            }
        };
    }

    public void startLinksThread(TextArea logbox, TextField timeBetweenLikes) throws InterruptedException {
        Task<Void> downloaderTask = linksTask(logbox);

        downloaderTask.setOnSucceeded((WorkerStateEvent t) -> {
            try {
                StartLikes startLikes = new StartLikes(links, timeBetweenLikes, logbox, driver);
            } catch (InterruptedException ex) {
                Logger.getLogger(GetInstagramLinks.class.getName()).log(Level.SEVERE, null, ex);
            }
            downloaderTask.cancel();
        });

        downloaderTask.setOnFailed((WorkerStateEvent t) -> {
            // Code to run once runFactory() **fails**
        });

        new Thread(downloaderTask).start();
    }
}
