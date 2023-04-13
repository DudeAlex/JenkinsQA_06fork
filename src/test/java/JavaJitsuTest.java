import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

public class JavaJitsuTest {

    @Test
    public void eightComponents() throws InterruptedException {

       // ChromeOptions chromeOptions = new ChromeOptions();
      //  chromeOptions.addArguments("--headless","--window-size=1920,1080");

        WebDriver driver = new ChromeDriver();
        driver.get("https://www.selenium.dev/selenium/web/web-form.html");

        String title = driver.getTitle();
        Assert.assertEquals("Web form", title);

      //  driver.manage().timeouts().implicitlyWait(Duration.ofMillis(500));
        Thread.sleep(5000);

        WebElement textBox = driver.findElement(By.name("my-text"));
        WebElement submitButton = driver.findElement(By.cssSelector("button"));

        textBox.sendKeys("Selenium");
        submitButton.click();

        WebElement message = driver.findElement(By.id("message"));
        String value = message.getText();
    Assert.assertEquals("Received!", value);

        driver.quit();
    }
    }

