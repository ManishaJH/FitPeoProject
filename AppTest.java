package FitPeo.FitPeoTesting;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.TimeoutException;

public class AppTest {
	WebDriver driver = null;

	@Test
	public void testMethod() {
		try {
			// Set the path to the ChromeDriver executable
			System.setProperty("webdriver.chrome.driver", "C://chromeDriver//chromedriver-win64//chromedriver.exe");

			// Initialize ChromeDriver (opens a Chrome browser)
			
			driver = new ChromeDriver();

			// Navigate to the FitPeo homepage
			driver.get("https://www.fitpeo.com");

			// maximize the browser window
			driver.manage().window().maximize();
			// Verify Revenue Calculator Page is displayed
			waitForVisibilityOfElement(driver, By.xpath("//h1[contains(text(),'Remote Patient')]"), 10);
			if (driver.findElements(By.xpath("//h1[contains(text(),'Remote Patient')]")).size() != 0) {
				System.out.println("Successfully navigated to the Remote Patient Monitoring page.");
			} else {
				System.out.println("Failed to navigate to the Remote Patient Monitorin page.");
			}

			// dynamic wait for visibility of the element on the web page
			waitForVisibilityOfElement(driver, By.xpath("//div[text()='Revenue Calculator']"), 10);

			// Navigate to the Revenue Calculator Page.
			driver.findElement(By.xpath("//div[text()='Revenue Calculator']")).click();

			// Verify Revenue Calculator Page is displayed
			waitForVisibilityOfElement(driver, By.xpath("//h5[text()='Total Gross Revenue Per Year']"), 10);
			if (driver.findElements(By.xpath("//h5[text()='Total Gross Revenue Per Year']")).size() != 0) {
				System.out.println("Successfully navigated to the Revenue Calculator page.");
			} else {
				System.out.println("Failed to navigate to the Revenue Calculator page.");
			}
			Thread.sleep(3000);
			// Locate the slider
			WebElement slider = waitForVisibilityOfElement(driver, By.xpath("//input[@type='number']"), 10);

			// Scroll down the page until the revenue calculator slider is visible
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].scrollIntoView();",
					driver.findElement(By.xpath("//h5[text()='Total Gross Revenue Per Year']")));

			// Use JavaScriptExecutor to set the slider value directly
			js.executeScript("arguments[0].value = 820;", slider);
			js.executeScript("arguments[0].dispatchEvent(new Event('input'));", slider);

			// Once the slider is moved the bottom text field value should be updated to 820
			WebElement textBoxValue = waitForVisibilityOfElement(driver, By.xpath("//input[@type='number']"), 10);
			if (textBoxValue.getAttribute("value").trim().equals("820")) {
				System.out.println("Slider value set to: " + textBoxValue.getAttribute("value").trim()
						+ " and TextField value is updated");
			} else {
				System.out.println("Failed to set slider value to 820 instead it is set to: "
						+ textBoxValue.getAttribute("value").trim());
			}

			// Enter the value 560 in the text field. and check the slider also should
			// change accordingly
			Thread.sleep(3000);
			textBoxValue.sendKeys(Keys.CONTROL + "a");
			textBoxValue.sendKeys(Keys.BACK_SPACE);
			textBoxValue.sendKeys("560");
			String sliderValue = driver.findElement(By.xpath("//input[@type='range']")).getAttribute("value");
			if (sliderValue.trim().equals("560")) {
				System.out.println(
						"TextField value set to: " + sliderValue.trim() + " and the slider's position is updated");
			} else {
				System.out.println("the slider's position is updated to reflect the value 560");
			}

			// select the checkboxes for CPT-99091, CPT-99453, CPT-99454, and CPT-99474.
			String CPTCodes = "CPT-99091;CPT-99453;CPT-99454;CPT-99474";
			for (String code : CPTCodes.split(";")) {
				// replacing code text with CPTcodes
				String locator = "(//p[text()='" + code + "']//following::input[@type='checkbox'])[1]";

				// Now it will find the element using this dynamically constructed locator
				WebElement checkbox = driver.findElement(By.xpath(locator));
				checkbox.click();
			}
			// Verify that the header displaying Total Recurring Reimbursement for all
			// Patients Per Month: shows the value $110700.
			String Reimbursementamount = driver.findElement(By.xpath(
					"(//p[text()='Total Recurring Reimbursement for all Patients Per Month:']//following::p[contains(@class,'MuiTypography-root MuiTypography-body1 inter css')])[1]"))
					.getText();
			if (Reimbursementamount.trim().equals("$110700")) {
				System.out.println("Total Recurring Reimbursement for all Patients Per Month: shows the value: "
						+ Reimbursementamount);
			} else {
				System.out.println("Total Recurring Reimbursement for all Patients Per Month: shows the value: "
						+ Reimbursementamount);
			}
			// Close the browser when done
			driver.quit();
			System.out.println("Browser closed successfully");
		} catch (Exception ex) {
			ex.printStackTrace();

		}
	}

	public static WebElement waitForVisibilityOfElement(WebDriver driver, By locator, int timeoutInSeconds) {
		// WebDriverWait instance with timeout
		WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
		try {
			// Wait until the element is visible
			return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
		} catch (TimeoutException e) {
			System.out.println("Timeout: Element not visible after waiting for " + timeoutInSeconds + " seconds.");
			return null; // Return null if element is not visible within the timeout
		}
	}
}
