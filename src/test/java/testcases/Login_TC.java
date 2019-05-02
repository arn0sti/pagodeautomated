// <summary>
// This page represents the first test in our project.
// This is a simple test that will login in the 
// </summary>

// Made by Hugo Monteiro v1.0

package testcases;

import org.testng.annotations.Test;
import base.TestBase;


public class Login_TC extends TestBase {
	
	@Test(priority = 1)
	public void doLogin() throws InterruptedException {
		
		//wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(OR.getProperty("language_select"))));
		Thread.sleep(2000);
		//We just landed in the login page
		
		//first we want to change the default language to english
		click("language_select");
		Thread.sleep(500);
		click("language");
		Thread.sleep(1000);
		
		//Then we'll type the login credetials and submit
		type("username_input",OR.getProperty("username"));
		type("password_input",OR.getProperty("password"));
		click("submit_button");
		
		// We'll make the driver wait until the page is loaded. 
		//Note : You should never use Thread.sleep method here, but since this project wasn't meant to be shared I used it to speed up the proccess. Use Webdriver.wait method instead.
		Thread.sleep(1000);
		
		
		// Here we are selecting the partner we want to work on.
		
		//select("partner_select","MM");
		select("partner_select","PF");
		Thread.sleep(2000);
		
	}

}
