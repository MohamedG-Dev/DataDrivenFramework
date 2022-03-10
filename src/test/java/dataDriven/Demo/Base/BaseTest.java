package dataDriven.Demo.Base;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Date;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.testng.Assert;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

import io.github.bonigarcia.wdm.WebDriverManager;

public class BaseTest {
	public Properties prop = null;
	public WebDriver driver;
	public ExtentReports eReport;
	public ExtentTest eTest;

	public void initialize() {
		if (prop == null) {
			prop = new Properties();
			File projectConfigFile = new File("./src/test/resources/projectconfig.properties");
			FileInputStream fis = null;
			try {
				fis = new FileInputStream(projectConfigFile);
				prop.load(fis);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void launchBrowser(String browserType) {
		eTest.log(Status.INFO, "Launching the " + browserType + " browser");
		switch (browserType.toUpperCase()) {
		case "CHROME":
			WebDriverManager.chromedriver().setup();
			driver = new ChromeDriver();
			break;
		case "EDGE":
			WebDriverManager.edgedriver().setup();
			driver = new EdgeDriver();
			break;
		case "FIREFOX":
			WebDriverManager.firefoxdriver().setup();
			driver = new FirefoxDriver();
			break;
		case "IE":
			WebDriverManager.iedriver().setup();
			driver = new InternetExplorerDriver();
			break;
		default:
			eTest.warning(
					"Please check the browserName and select the browser as CHROME or EDGE or FIREFOX or INTERNET EXPLORER");

		}
		eTest.log(Status.INFO, "Browser " + browserType + " got launched");
		driver.manage().window().maximize();
		eTest.info("Browser is maximized");
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
	}

	public void navigateApp(String URL) {
		driver.get(URL);
		eTest.log(Status.INFO, "Navigated to " + URL);
	}

	public boolean doLogin(String userName, String password) {
		click("LoginLink_classname");
		typeText("EmailTextBox_id", userName);
		click("NextButton_id");
		typeText("PasswordTextBox_id", password);
		if (isElementAvailable("MailOption_cssSelector")) {
			return true;
		} else {
			return false;
		}
	}

	public void click(String locatorKey) {
		WebElement element = getElement(locatorKey);
		eTest.info("Clicking on " + locatorKey);
		element.click();
		eTest.log(Status.INFO, "Successfully clicked on " + locatorKey);
	}

	public void typeText(String locatorKey, String text) {
		WebElement element = getElement(locatorKey);
		element.sendKeys(text);
		eTest.log(Status.INFO, text + " got typed into the " + locatorKey);
	}

	public boolean isElementAvailable(String locatorKey) {
		WebElement element = getElement(locatorKey);
		boolean elementDisplayed = element.isDisplayed();
		return elementDisplayed;
	}

	public WebElement getElement(String locatorKey) {
		String locatorValue = prop.getProperty(locatorKey);
		WebElement element = null;
		try {
			if (locatorKey.endsWith("_id")) {
				element = driver.findElement(By.id(locatorValue));
			} else if (locatorKey.endsWith("_name")) {
				element = driver.findElement(By.name(locatorValue));
			} else if (locatorKey.endsWith("_classname")) {
				element = driver.findElement(By.className(locatorValue));
			} else if (locatorKey.endsWith("_linkText")) {
				element = driver.findElement(By.linkText(locatorValue));
			} else if (locatorKey.endsWith("_cssSelector")) {
				element = driver.findElement(By.cssSelector(locatorValue));
			} else if (locatorKey.endsWith("_xpath")) {
				element = driver.findElement(By.xpath(locatorValue));
			}
		} catch (Exception e) {
			reportFail(locatorKey + " holding the " + locatorValue + " is unavailable in the application");
			// e.printStackTrace();
		}
		return element;
	}

	public void reportPass(String message) {
		eTest.log(Status.PASS, message + " got passed.");
	}

	public void reportFail(String message) {
		eTest.fail(message);
		takeScreenshot();
		Assert.fail(message);
	}

	public void takeScreenshot() {
		Date date = new Date();
		String filePath = date.toString().replace(":", "_").replace(" ", "_") + ".png";
		File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		try {
			FileUtils.copyFileToDirectory(srcFile, new File("screenshots/" + filePath));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		eTest.log(Status.INFO, "Screenshot ->" + eTest.addScreenCaptureFromPath(System.getProperty("user.dir")+"/screenshots/" + filePath));
	}
}
