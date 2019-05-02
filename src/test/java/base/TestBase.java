// <summary>
// This page represent the base for all other test pages.
// Here we can define methods for general actions such as typing and locating elements.
// </summary>

// Made by Hugo Monteiro v1.0


package base;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import utilities.TestUtil;


public class TestBase {

	//Define required properties, variables and classes
	public static WebDriver driver;
	public static Properties config = new Properties();
	public static Properties OR = new Properties();
	public static FileInputStream fis;
	public static Logger log = Logger.getLogger("devpinoyLogger");
	public static utilities.ExcelReader excel = new utilities.ExcelReader(System.getProperty("user.dir") + "\\src\\test\\resources\\excel\\testdata.xlsx");
	public static WebDriverWait wait;
	public ExtentReports rep = utilities.ExtentManager.getInstance();
	public static ExtentTest test;
	public static String browser;

	
	//First method to be initiated after test starts.
	//Loads init config and launches the browser.
	@BeforeSuite
	public void setUp() {

		
		// driver is not initialized yet
		if (driver == null) {

			// get the config files
			try {
				
				fis = new FileInputStream(
						System.getProperty("user.dir") + "\\src\\test\\resources\\properties\\Config.properties");
				
			} catch (FileNotFoundException e) {
				
				// TODO Auto-generated catch block
				e.printStackTrace();
				
			}try {
				
				config.load(fis);
				log.debug("Config file loaded !!!");
				
			} catch (IOException e) {
				
				// TODO Auto-generated catch block
				e.printStackTrace();
				
			}try {
				
				// get the property files
				fis = new FileInputStream(System.getProperty("user.dir") + "\\src\\test\\resources\\properties\\OR.properties");
				
			} catch (FileNotFoundException e) {
				
				// TODO Auto-generated catch block
				e.printStackTrace();
				
			}
			try {
				
				OR.load(fis);
				log.debug("OR file loaded !!!");
				
			} catch (IOException e) {
				
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (System.getenv("browser") != null && !System.getenv("browser").isEmpty()) {

				browser = System.getenv("browser");
				
			} else {

				browser = config.getProperty("browser");

			}

			//Set browser properties and executable path
			config.setProperty("browser", browser);
			System.setProperty("webdriver.chrome.driver",System.getProperty("user.dir") + "\\src\\test\\resources\\executables\\chromedriver.exe");
			driver = new ChromeDriver();
			log.debug("Chrome Launched !!!");

			// Go the url and set the window properties
			driver.get(config.getProperty("testsiteurl"));
			log.debug("Navigated to : " + config.getProperty("testsiteurl"));
			driver.manage().window().maximize();
			driver.manage().timeouts().implicitlyWait(Integer.parseInt(config.getProperty("implicit.wait")),TimeUnit.SECONDS);
			wait = new WebDriverWait(driver, 5);
		}

	}

	
	// click on a component
	public void click(String locator) {

		driver.findElement(By.cssSelector(OR.getProperty(locator))).click();
		test.log(LogStatus.INFO, "Clicking on : " + locator);
		
	}

	
	// insert text in a component
	public void type(String locator, String value) {

		driver.findElement(By.cssSelector(OR.getProperty(locator))).sendKeys(value);
		test.log(LogStatus.INFO, "Typing in : " + locator + " entered value as " + value);

	}

	// Handle a combo-box
	static WebElement dropdown;
	public void select(String locator, String value) {

		dropdown = driver.findElement(By.cssSelector(OR.getProperty(locator)));
		Select select = new Select(dropdown);
		select.selectByVisibleText(value);

		test.log(LogStatus.INFO, "Selecting from dropdown : " + locator + " value as " + value);

	}

	//Check if an element is present in the current page
	public boolean isElementPresent(By by) {

		try {

			driver.findElement(by);
			return true;

		} catch (NoSuchElementException e) {

			return false;

		}

	}

	//Validate if two values match
		public static boolean verifyBetween(Double fleexinV2, Double  fleexinFixeExcel,String field) throws IOException {
			try {

			//	Assert.assertTrue(actual <= expected && actual >= expected);
				
				if(fleexinV2 != fleexinFixeExcel) {
					if(fleexinV2 - fleexinFixeExcel > 1 || fleexinV2 - fleexinFixeExcel < -1 ) {
						Assert.fail();
						
					}else {
						Assert.fail();
					}	
				}
				return true;
			} catch (Throwable t) {
				
				
				// This will make the error report, take screenshots and so on.
				TestUtil.captureScreenshot();
				// ReportNG
				Reporter.log("<br>" + "Verification failure : "+field+" Expected : " + fleexinV2 +"  found :  "+fleexinFixeExcel + "<br>");
				Reporter.log("<a target=\"_blank\" href=" + TestUtil.screenshotName + "><img src=" + TestUtil.screenshotName+ " height=200 width=200></img></a>");
				Reporter.log("<br>");
				Reporter.log("<br>");
				
				// Extent Reports
				test.log(LogStatus.FAIL, " Verification failed with exception : " + t.getMessage());
				test.log(LogStatus.FAIL, test.addScreenCapture(TestUtil.screenshotName));
				return false;
			}

		}
		
		
		//Validate if two values match
				public static boolean verifyEquals(String expected, String actual,String field) throws IOException {
					try {

						Assert.assertEquals(actual,expected);
						return true;
					} catch (Throwable t) {
						
						TestUtil.captureScreenshot();
						// ReportNG
						Reporter.log("<br>" + "Verification failure : "+field+" Expected : " + expected+"  found :  "+actual + "<br>");
						Reporter.log("<a target=\"_blank\" href=" + TestUtil.screenshotName + "><img src=" + TestUtil.screenshotName+ " height=200 width=200></img></a>");
						Reporter.log("<br>");
						Reporter.log("<br>");
						// Extent Reports
						test.log(LogStatus.FAIL, " Verification failed with exception : " + t.getMessage());
						test.log(LogStatus.FAIL, test.addScreenCapture(TestUtil.screenshotName));
						return false;
					}
			   }

				
	//After test is done, close the browser window
	@AfterSuite
	public void tearDown() {

		if (driver != null) {
			driver.quit();
		}

		log.debug("test execution completed !!!");
	}
}
