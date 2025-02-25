package school.redrover;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;
import school.redrover.runner.BaseTest;

public class FreestyleProject6Test extends BaseTest {

    private final String PROJECT_NAME = "Project 2";
    private final String EXPECTED_H2 = "This folder is empty";
    private final String NEW_NAME = "Correct name";

    private void createProject() {
        WebElement newItemMenu = getWait2().until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@href = '/view/all/newJob']")));
        newItemMenu.click();

        WebElement nameInputField = getDriver().findElement(By.id("name"));
        nameInputField.sendKeys(PROJECT_NAME);

        WebElement freestyleProjectButton = getDriver().findElement(By.xpath("//li//span[contains(text(), 'Freestyle')]"));
        freestyleProjectButton.click();

        WebElement okButton = getDriver().findElement(By.id("ok-button"));
        okButton.click();

        WebElement saveButton = getDriver().findElement(By.name("Submit"));
        saveButton.click();
    }

    @Test
    public void testAddingProject() {
        final String expectedH1 = "Project Project 2";

        WebElement newItemMenu = getWait2().until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@href = '/view/all/newJob']")));
        newItemMenu.click();

        WebElement nameInputField = getDriver().findElement(By.id("name"));
        nameInputField.sendKeys(PROJECT_NAME);

        WebElement freestyleProjectButton = getDriver().findElement(By.xpath("//li//span[contains(text(), 'Freestyle')]"));
        freestyleProjectButton.click();

        WebElement okButton = getDriver().findElement(By.id("ok-button"));
        okButton.click();

        WebElement saveButton = getDriver().findElement(By.name("Submit"));
        saveButton.click();

        WebElement myViewsMenuItem = getWait2().until(ExpectedConditions.presenceOfElementLocated(By.xpath("//h1")));

        Assert.assertEquals(myViewsMenuItem.getText(), expectedH1);
    }

    @Test
    public void testDeleteProject() {
        createProject();

        WebElement dashboardBreadCrumb = getDriver().findElement(By.xpath("//li/a[contains(text(),'Dashboard')]"));
        dashboardBreadCrumb.click();

        WebElement project = getDriver().findElement(By.xpath("//span[contains(text(), '" + PROJECT_NAME + "')]"));
        project.click();

        WebElement deleteProjectLink = getDriver().findElement(By.xpath("//span[contains(text(), 'Delete Project')]"));
        deleteProjectLink.click();
        getDriver().switchTo().alert().accept();

        WebElement myViews = getDriver().findElement(By.xpath("//a[@href = '/me/my-views']"));
        myViews.click();

        Assert.assertEquals(getDriver().findElement(By.xpath("//h2")).getText(), EXPECTED_H2);
    }

    @Test
    public void testDeleteProjectFromDropdown() {
        createProject();

        WebElement dashboardBreadCrumb = getDriver().findElement(By.xpath("//li/a[contains(text(),'Dashboard')]"));
        dashboardBreadCrumb.click();

        Actions act = new Actions(getDriver());
        WebElement projectName = getDriver().findElement(By.xpath("//span[contains(text(), '" + PROJECT_NAME + "')]"));
        act.moveToElement(projectName, 23, 7).perform();


        Actions act2 = new Actions(getDriver());
        WebElement dropDownButton = getDriver().findElement(By.xpath("//td/a/button[@class = 'jenkins-menu-dropdown-chevron']"));
        act2.moveToElement(dropDownButton).perform();
        dropDownButton.sendKeys(Keys.RETURN);

        getWait2().until(ExpectedConditions.visibilityOfElementLocated(By.id("breadcrumb-menu")));
        getDriver().findElement(By.xpath("//div//li//span[contains(text(),'Delete Project')]")).click();
        getDriver().switchTo().alert().accept();

        getDriver().findElement(By.xpath("//a[@href = '/me/my-views']")).click();

        Assert.assertEquals(getDriver().findElement(By.xpath("//h2")).getText(), EXPECTED_H2);
    }

    @Test
    public void testAddDescription() {
        final String descriptionText = "In publishing and graphic design, Lorem ipsum is a placeholder " +
                "text commonly used to demonstrate the visual form of a document or a typeface without relying .";

        createProject();

        WebElement dashboardBreadCrumb = getDriver().findElement(By.xpath("//li/a[contains(text(),'Dashboard')]"));
        dashboardBreadCrumb.click();

        Actions act = new Actions(getDriver());
        WebElement projectName = getDriver().findElement(By.xpath("//span[contains(text(), '" + PROJECT_NAME + "')]"));
        act.moveToElement(projectName, 23, 7).perform();

        Actions act2 = new Actions(getDriver());
        WebElement dropDownButton = getDriver().findElement(By.xpath("//td/a/button[@class = 'jenkins-menu-dropdown-chevron']"));
        act2.moveToElement(dropDownButton).perform();
        dropDownButton.sendKeys(Keys.RETURN);

        getWait2().until(ExpectedConditions.visibilityOfElementLocated(By.id("breadcrumb-menu")));
        getDriver().findElement(By.xpath("//div//li//span[contains(text(),'Configure')]")).click();

        getDriver().findElement(By.name("description")).sendKeys(descriptionText);
        getDriver().findElement(By.xpath("//div/a[@previewendpoint='/markupFormatter/previewDescription']")).click();

        WebElement previewTextArea = getDriver().findElement(By.xpath("//div[@class = 'textarea-preview']"));

        Assert.assertEquals(previewTextArea.getText(), descriptionText);

        getDriver().findElement(By.name("Submit")).click();

        Assert.assertEquals(getDriver().findElement(By.xpath("//div[@id='description']/div[1]")).getText(), descriptionText);
    }

    @Test
    public void testRenameProjectFromDashboardPage() {
        createProject();

        WebElement dashboardBreadCrumb = getDriver().findElement(By.xpath("//li/a[contains(text(),'Dashboard')]"));
        dashboardBreadCrumb.click();

        Actions act = new Actions(getDriver());
        WebElement projectName = getDriver().findElement(By.xpath("//span[contains(text(), '" + PROJECT_NAME + "')]"));
        act.moveToElement(projectName, 23, 7).perform();

        Actions act2 = new Actions(getDriver());
        WebElement dropDownButton = getDriver().findElement(By.xpath("//td/a/button[@class = 'jenkins-menu-dropdown-chevron']"));
        act2.moveToElement(dropDownButton).perform();
        dropDownButton.sendKeys(Keys.RETURN);

        getWait2().until(ExpectedConditions.visibilityOfElementLocated(By.id("breadcrumb-menu")));
        getDriver().findElement(By.xpath("//div//li//span[contains(text(),'Rename')]")).click();

        WebElement newNameInputField = getDriver().findElement(By.name("newName"));
        newNameInputField.clear();
        newNameInputField.sendKeys(NEW_NAME);

        getDriver().findElement(By.name("Submit")).click();

        Assert.assertEquals(getDriver().findElement(By.xpath("//h1")).getText(), "Project " + NEW_NAME);
    }

    @Test
    public void testRenameProjectFromProjectPage() {
        createProject();

        WebElement dashboardBreadCrumb = getDriver().findElement(By.xpath("//li/a[contains(text(),'Dashboard')]"));
        dashboardBreadCrumb.click();

        WebElement project = getDriver().findElement(By.xpath("//span[contains(text(), '" + PROJECT_NAME + "')]"));
        project.click();

        WebElement renameProjectLink = getDriver().findElement(By.xpath("//a[contains(@href, 'rename')]"));
        renameProjectLink.click();

        WebElement newNameInputField = getDriver().findElement(By.name("newName"));
        newNameInputField.clear();
        newNameInputField.sendKeys(NEW_NAME);

        getDriver().findElement(By.name("Submit")).click();

        Assert.assertEquals(getDriver().findElement(By.xpath("//h1")).getText(), "Project " + NEW_NAME);
    }
}
