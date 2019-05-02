// <summary>
// This page represents the second  test in our project.
// This is a complex test for the simulator engine.
// This test takes a Data-Driven approach - The data to be used will be loaded from an xls file.
// Notes : The test was conceived with a single insured in mind.
// </summary>

// Made by Hugo Monteiro v1.0

package testcases;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;


import base.TestBase;
import utilities.TestUtil;

public class Simulator_TC extends TestBase{

	@SuppressWarnings("deprecation")
	// This are parameters that the testNG accepts. Here we are passing the data and the priority
	@Test(dataProviderClass=TestUtil.class,dataProvider="dp",priority=2) 
	public void runSimulation(Hashtable<String,String> data) throws InterruptedException, IOException {
	
		// We'll be starting by getting all the data we want from the file and put them into variables.
		// Notes : Some of the data can be used without putting them in variables, but for a matter of simplicity and since performance is not an issue we'll be storing it in variables.
		String duration = data.get("loan_initial_duration");
	
        // Here we are using a string manipulation because the format the data comes from the excel does not match the validations that are implemented in the engine inputs.
		duration = duration.substring(0,duration.indexOf("."));
		String initial_capital = data.get("initial_capital").substring(0, data.get("initial_capital").indexOf("."));
		String currentInsurance, fleexinFixe_string,fleexinV2_string,fleexinV2Excel_string,fleexinFixeExcel_string,effectiveDate,birthDate;
		SimpleDateFormat formater = new SimpleDateFormat("dd-MM-yyyy");
		birthDate = formater.format(new Date(data.get("birth_date")));
		effectiveDate = formater.format(new Date(data.get("loan_effective_date")));

		// We are in dashboard and we'll click on the simulator icon to go to the page.
		Thread.sleep(2000);
		click("goto_simulator");
		Thread.sleep(1000);
		
		
		//Here we'll start typing the values needed in the page.
		select("project_type",data.get("project_type"));
		type("bank",data.get("bank"));
		
		//driver.findElement(By.cssSelector("loan_initial_duration")).clear();
		type("loan_initial_duration",duration);
		
		type("loan_effective_date",effectiveDate);
		select("type_of_loan",data.get("type_of_loan"));
		driver.findElement(By.cssSelector(OR.getProperty("initial_capital"))).clear();
		type("initial_capital",initial_capital);

		
		// This is a null check. This should be done for every data variable but for a matter of quick development it wasn't implemented.
		if(!data.get("type_of_loan").equals("0% rate loan")) {
			driver.findElement(By.cssSelector(OR.getProperty("initial_rate"))).clear();
			Thread.sleep(500);
			type("initial_rate",data.get("initial_rate"));
		}
		
		
		// This is the bottom part of the page.
		select("coverage",data.get("coverage"));
		type("birth_date",birthDate);
		type("cover_ratio",data.get("cover_ratio"));
		select("smoking_habit",data.get("smoking_habit"));
		select("professional_status",data.get("professional_status"));
		
		click("submit_button");
		Thread.sleep(2000);
		
		
		// This is the first verification we make. We want to make sure that the simulation was successful 
		if(!isElementPresent(By.cssSelector("[class='Feedback_Message_Success']"))) {
			Assert.fail();
		}
		
		Thread.sleep(4000);
		
		// Here we are getting the list of elements that contain the simulation results
		List<WebElement> results = driver.findElements(By.cssSelector("[class='Results_Title']"));
		//((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", results.get(0));
		
		//Here we are extracting the values and removing the spaces to get the value only so we can verify if they're equal
		fleexinV2_string = results.get(2).getText().replaceAll("\\s","");
		fleexinFixe_string = results.get(5).getText().replaceAll("\\s","");
		fleexinV2_string = fleexinV2_string.substring(1,fleexinV2_string.indexOf(","));
		fleexinFixe_string = fleexinFixe_string.substring(1,fleexinFixe_string.indexOf(","));
		
		fleexinV2Excel_string = data.get("fleexin_v2");
		fleexinV2Excel_string = fleexinV2Excel_string.substring(0, fleexinV2Excel_string.indexOf("."));
		fleexinFixeExcel_string = data.get("fleexin_fixe").substring(0, data.get("fleexin_fixe").indexOf("."));

		
		//Here is where we are actually comparing the values.
		
//		verifyEquals(data.get("current_insurance"),currentInsurance);
		verifyEquals(fleexinV2Excel_string,fleexinV2_string,"FleeXin V2 -");
		verifyEquals(fleexinFixeExcel_string,fleexinFixe_string,"FleeXin Coti Fixe - ");
		
	}

	
}
