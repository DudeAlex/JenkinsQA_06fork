package school.redrover;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;
import school.redrover.runner.BaseTest;

public class PipelineProject6Test extends BaseTest {

    private final String projectName = "New Project";

    public void createNewProject() {
        WebElement newItemButton = getDriver().findElement(By.xpath("//a[@href = '/view/all/newJob']"));
        newItemButton.click();

        WebElement inputField = getDriver().findElement(By.id("name"));
        inputField.sendKeys(projectName);

        WebElement jobType = getDriver()
                .findElement(By.xpath("//*[@id='j-add-item-type-standalone-projects']/ul/li[2]"));
        jobType.click();

        WebElement okButton = getDriver().findElement(By.id("ok-button"));
        okButton.click();

        WebElement saveButton = getDriver().findElement(By.xpath("//*[@name = 'Submit']"));
        saveButton.click();
    }

    @Test
    public void testVerifyProjectIsCreated() {
        createNewProject();

        Assert.assertEquals(getDriver().findElement(By.xpath("//*[@id = 'main-panel']/h1"))
                .getText(), "Pipeline " + projectName);
    }

    @Test
    public void testVerifyProjectOnDashboard() {
        createNewProject();

        WebElement dashboardButton = getDriver().findElement(By.xpath("//*[@id='breadcrumbs']/li[1]/a"));
        dashboardButton.click();

        Assert.assertEquals(getDriver().findElement(By.xpath("//*[@id='job_New Project']/td[3]/a/span"))
                .getText(), projectName);
    }
}
