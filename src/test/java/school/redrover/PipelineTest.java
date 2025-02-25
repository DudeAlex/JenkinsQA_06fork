package school.redrover;

import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;
import school.redrover.model.MainPage;
import school.redrover.runner.BaseTest;
import school.redrover.runner.TestUtils;

import java.time.Duration;

public class PipelineTest extends BaseTest {

    private static final String PIPELINE_NAME = RandomStringUtils.randomAlphanumeric(10);

    private final By newItem = By.linkText("New Item");
    private final By name = By.id("name");
    private final By pipelineItem = By.xpath("//span[text() = 'Pipeline']");
    private final By pipelineJob = By.xpath("//span[text() = '" + PIPELINE_NAME + "']");
    private final By okButton = By.id("ok-button");
    private final By saveButton = By.xpath("//button[contains(@class,'jenkins-button jenkins-button--primary')]");
    private final By jenkinsIconHeader = By.id("jenkins-name-icon");
    private final By textAreaDescription = By.xpath("//textarea[@name='description']");
    private final By pipelineDescription = By.xpath("//div[@id = 'description']/div[1]");
    private final By editDescription = By.xpath("//a[@id='description-link']");
    private final By pipelineTrySampleDropDownMenu = By.xpath("//option[text() = 'try sample Pipeline...']");
    private final By buildNowButton = By.xpath("//div[@id = 'tasks']/div[3]//a");
    private final By dashboard = By.xpath("//a[text()='Dashboard']");
    private final By buttonSaveOnConfigurePage = By.xpath("//button[@name='Submit']");

    private WebDriverWait getWait(int seconds) {
        return new WebDriverWait(getDriver(), Duration.ofSeconds(seconds));
    }

    public WebDriverWait webDriverWait10;

    public void scrollByElement(By by) throws InterruptedException {
        WebElement scroll = getDriver().findElement(by);
        new Actions(getDriver())
                .scrollToElement(scroll)
                .perform();
    }

    public final WebDriverWait getWait10() {
        if (webDriverWait10 == null) {
            webDriverWait10 = new WebDriverWait(getDriver(), Duration.ofSeconds(10));
        }
        return webDriverWait10;
    }

    @Ignore
    @Test
    public void testCreatePipeline() throws InterruptedException {

        WebElement jobName = getDriver().findElement(By.cssSelector(".content-block__link>span"));
        jobName.click();

        WebElement field = getDriver().findElement(By.cssSelector("#name"));
        field.sendKeys("Pipe");

        WebElement pipeline = getDriver().findElement(By.xpath("//span[text()='Pipeline']"));
        pipeline.click();

        WebElement buttonOk = getDriver().findElement(By.cssSelector("#ok-button"));
        buttonOk.click();

        getWait10();

        scrollByElement(By.cssSelector(".page-footer__links.page-footer__links--white.jenkins_ver>a"));
        WebElement save = getDriver().findElement(By.cssSelector(".jenkins-button.jenkins-button--primary"));
        save.click();

        getWait10();

        WebElement pipelinePipe = getDriver().findElement(By.cssSelector("#main-panel>h1"));

        Assert.assertEquals(pipelinePipe.getText(), "Pipeline Pipe");
    }

    @Test
    public void testCreatedPipelineIsDisplayedOnDashboard() {
        getDriver().findElement(newItem).click();
        getWait(1).until(ExpectedConditions.elementToBeClickable(name)).sendKeys(PIPELINE_NAME);
        getDriver().findElement(pipelineItem).click();
        getDriver().findElement(okButton).click();
        getWait(2).until(ExpectedConditions.elementToBeClickable(saveButton)).click();
        getDriver().findElement(jenkinsIconHeader).click();

        String actualResult = getDriver().findElement(By.xpath("//tr[@id = 'job_" + PIPELINE_NAME + "']//a[@href='job/" + PIPELINE_NAME + "/']")).getText();

        Assert.assertEquals(actualResult, PIPELINE_NAME);
    }

    @Test
    public void testCreatePipelineWithDescription() {
        String pipelineDescriptionText = "description text";
        getDriver().findElement(newItem).click();
        getWait(1).until(ExpectedConditions.elementToBeClickable(name)).sendKeys(PIPELINE_NAME);
        getDriver().findElement(pipelineItem).click();
        getDriver().findElement(okButton).click();
        getWait(2).until(ExpectedConditions.elementToBeClickable(textAreaDescription)).click();
        getDriver().findElement(textAreaDescription).sendKeys(pipelineDescriptionText);
        getDriver().findElement(saveButton).click();

        Assert.assertEquals(getDriver().findElement(pipelineDescription).getText(), pipelineDescriptionText);
    }

    @Test
    public void testEditPipelineDescription() {
        String pipelineDescriptionText = "description text";
        String pipelineDescriptionTextEdited = "Edited description text";
        getDriver().findElement(newItem).click();
        getWait(1).until(ExpectedConditions.elementToBeClickable(name)).sendKeys(PIPELINE_NAME);
        getDriver().findElement(pipelineItem).click();
        getDriver().findElement(okButton).click();
        getWait(2).until(ExpectedConditions.elementToBeClickable(textAreaDescription)).click();
        getDriver().findElement(textAreaDescription).sendKeys(pipelineDescriptionText);
        getDriver().findElement(saveButton).click();
        getDriver().findElement(jenkinsIconHeader).click();
        getDriver().findElement(pipelineJob).click();
        getDriver().findElement(editDescription).click();
        getWait(2).until(ExpectedConditions.elementToBeClickable(textAreaDescription)).click();
        getDriver().findElement(textAreaDescription).clear();
        getDriver().findElement(textAreaDescription).sendKeys(pipelineDescriptionTextEdited);
        getDriver().findElement(saveButton).click();

        Assert.assertEquals(getDriver().findElement(pipelineDescription).getText(), pipelineDescriptionTextEdited);
    }

    @Test
    public void testPipelineBuildNow() {
        getDriver().findElement(newItem).click();
        getWait(2).until(ExpectedConditions.elementToBeClickable(name)).sendKeys(PIPELINE_NAME);
        getDriver().findElement(pipelineItem).click();
        getDriver().findElement(okButton).click();
        getWait(2).until(ExpectedConditions.elementToBeClickable(pipelineTrySampleDropDownMenu)).click();
        getDriver().findElement(By.cssSelector("option[value='hello']")).click();
        getDriver().findElement(saveButton).click();
        getWait(2).until(ExpectedConditions.elementToBeClickable(buildNowButton)).click();
        getWait(10).until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".table-viewPort")));

        Assert.assertEquals(getDriver().findElement(By.cssSelector(".stage-header-name-0")).getText(), "Hello");
    }

    @Test
    public void testPipelineConsoleOutputSuccess() {
        getDriver().findElement(newItem).click();
        getWait(2).until(ExpectedConditions.elementToBeClickable(name)).sendKeys(PIPELINE_NAME);
        getDriver().findElement(pipelineItem).click();
        getDriver().findElement(okButton).click();
        getWait(2).until(ExpectedConditions.elementToBeClickable(pipelineTrySampleDropDownMenu)).click();
        getDriver().findElement(By.cssSelector("option[value='hello']")).click();
        getDriver().findElement(saveButton).click();
        getWait(2).until(ExpectedConditions.elementToBeClickable(buildNowButton)).click();
        getWait(10).until(ExpectedConditions
                .visibilityOfElementLocated(By.cssSelector(".build-icon"))).click();
        getWait(2).until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#main-panel")));

        Assert.assertTrue(getDriver().findElement(By.cssSelector(".console-output")).getText().contains("Finished: SUCCESS"));
    }

    @Test
    public void testCreatePipelineProject() {
        WebElement projectName = new MainPage(getDriver())
                .clickNewItem()
                .enterItemName(PIPELINE_NAME)
                .selectPipelineAndClickOK()
                .clickSaveButton()
                .clickDashboard().getProjectName();

        Assert.assertEquals(projectName.getText(), PIPELINE_NAME);
    }


    @Test
    public void testAddingDescriptionToPipeline() {
        getDriver().findElement(By.xpath("//a[normalize-space()='New Item']")).click();
        getWait(1);

        getDriver().findElement(By.id("name")).sendKeys(PIPELINE_NAME);
        getDriver().findElement(By.xpath("//span[normalize-space()='Pipeline']")).click();
        getDriver().findElement(By.id("ok-button")).click();

        getDriver().findElement(By.xpath("//a[normalize-space()='Dashboard']")).click();
        getWait(1);

        getDriver().findElement(By
                .xpath("//a[@class='jenkins-table__link model-link inside']")).click();
        getWait(1);

        getDriver().findElement(By.xpath("(//div[@id='side-panel']/div/div)[4]")).click();
        getWait(1);

        String pipelineDescription = "This is a basic Pipeline project.";

        getDriver().findElement(By.name("description")).sendKeys(pipelineDescription);
        getDriver().findElement(By.name("Submit")).click();
        getWait(1);

        WebElement projectDescription =
                getDriver().findElement(By.xpath("(//div[@id='description']/div)[1]"));

        Assert.assertEquals(projectDescription.getText(), pipelineDescription);
    }

    @Test(dependsOnMethods = "testCreatePipelineProject")
    public void testRenamePipeline() {
        final String newPipelineName = PIPELINE_NAME + "new";

        WebElement projectName = new MainPage(getDriver())
                .clickPipelineProject(PIPELINE_NAME)
                .clickRename()
                .clearNameField()
                .enterNewName(newPipelineName)
                .clickRenameButton()
                .clickDashboard().getProjectName();

        Assert.assertEquals(projectName.getText(), newPipelineName);
    }

    @Test(dependsOnMethods = {"testCreatePipelineProject", "testRenamePipeline"})
    public void testDeletePipeline() {
        new MainPage(getDriver())
                .clickPipelineProject(PIPELINE_NAME + "new")
                .clickDeletePipeline()
                .acceptAlert();

        Assert.assertFalse(getDriver().findElement(By.id("main-panel")).getText().contains(PIPELINE_NAME + "new"));
    }

    @Test
    public void testCreatingBasicPipelineProjectThroughJenkinsUI() {

        getDriver().findElement(By.xpath("//a[normalize-space()='New Item']")).click();

        getDriver().findElement(By.id("name")).sendKeys("Pipeline01");
        getDriver().findElement(By.xpath("//span[normalize-space()='Pipeline']")).click();
        getDriver().findElement(By.id("ok-button")).click();

        getDriver().findElement(By.xpath("//button[@data-section-id='pipeline']")).click();
        WebElement optionInDefinitionField = getDriver()
                .findElement(By.xpath("((//div[@class='jenkins-form-item'])[2]//select//option)[1]"));

        Assert.assertEquals(optionInDefinitionField.getText(), "Pipeline script");
    }

    @Test
    public void testOpenCreatedPipeline() {
        TestUtils.createPipeline(this, PIPELINE_NAME, true);

        getWait5().until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("a[href='job/" + PIPELINE_NAME + "/']"))).click();

        Assert.assertTrue(getDriver().findElement(By.cssSelector("#main-panel > h1")).getText().contains(PIPELINE_NAME));
    }

    @Test
    public void testDeletePipelineProject() {
        TestUtils.createPipeline(this, PIPELINE_NAME, false);

        getWait5().until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("a[data-url='/job/" + PIPELINE_NAME + "/doDelete']"))).click();
        getDriver().switchTo().alert().dismiss();
        getDriver().findElement(By.id("jenkins-home-link")).click();

        WebElement chevron = getWait5().until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//a[contains(@href,'job/" + PIPELINE_NAME + "/')]/button[@class='jenkins-menu-dropdown-chevron']")));
        chevron.sendKeys(Keys.RETURN);
        getWait5().until(ExpectedConditions.presenceOfElementLocated(By.xpath("//ul[@class='first-of-type']/li[4]"))).click();
        getDriver().switchTo().alert().accept();
        getDriver().findElement(By.id("jenkins-home-link")).click();

        Assert.assertFalse(getDriver().findElement(By.id("main-panel")).getText().contains(PIPELINE_NAME));
    }

    @Test(dependsOnMethods = "testCreatingBasicPipelineProjectThroughJenkinsUI")
    public void testPipelineBuildingAfterChangesInCode(){

        getWait2().until(ExpectedConditions.presenceOfElementLocated(dashboard)).click();
        getWait2().until(ExpectedConditions.presenceOfElementLocated(By
                .xpath("//a[@href='job/Pipeline01/']"))).click();

        getWait2().until(ExpectedConditions.presenceOfElementLocated(By
                .xpath("//a[@href='/job/Pipeline01/configure']"))).click();
        getWait10().until(ExpectedConditions.presenceOfElementLocated(By
                .xpath("//button[@data-section-id='pipeline']"))).click();

        WebElement trySamplePipelineField = getWait2().until(ExpectedConditions
                .presenceOfElementLocated(By.xpath("//div[@class='samples']//select")));

        Select samplePipelineCode = new Select(trySamplePipelineField);
        samplePipelineCode.selectByIndex(0);
        getDriver().findElement(buttonSaveOnConfigurePage).click();

        getWait5().until(ExpectedConditions.presenceOfElementLocated(buildNowButton)).click();
        WebElement buildNumber=getWait10().until(ExpectedConditions.presenceOfElementLocated(By
                .xpath("(//a[@update-parent-class='.build-row'])[1]")));

        new Actions(getDriver())
                .moveToElement(buildNumber)
                .pause(Duration.ofSeconds(1))
                .click()
                .perform();

        getWait5().until(ExpectedConditions.presenceOfElementLocated(By
                .xpath("//span[@class='build-status-icon__outer']//*[local-name()='svg']")));

        WebElement buildStatusIcon =
                getDriver().findElement(By.xpath("//span[@class='build-status-icon__outer']//*[local-name()='svg']"));
        WebElement buildStatusText=
                getDriver().findElement(By.xpath("//h1[@class='build-caption page-headline']"));

        Assert.assertTrue(buildStatusText.getText().contains("Build #1"));
        Assert.assertTrue(buildStatusIcon.isDisplayed());
    }
}
