package base;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.testng.annotations.*;

public class GenericMethods {

	//Global variables
	public static WebDriver driver;
	public static Properties Config = new Properties();
	public static Properties OR = new Properties();
	public static Properties ExtractedData = new Properties();
	public static FileInputStream fis;
	public static FileOutputStream fos;
	
		@BeforeSuite
		public void setUp() throws IOException {

			if (driver == null) {

				//Loading Configuration file

				FileInputStream fis = new FileInputStream(
						System.getProperty("user.dir") + "\\src\\test\\resources\\propertiesFiles\\Config.properties");

				Config.load(fis);

				//Loading Object repository file

				fis = new FileInputStream(System.getProperty("user.dir") + "\\src\\test\\resources\\propertiesFiles\\OR.properties");

				OR.load(fis);
			}
			//Browser options in switch case
			if (Config.getProperty("browser").equalsIgnoreCase("firefox")) {

				System.setProperty("webdriver.gecko.driver", System.getProperty("user.dir") + "\\Drivers\\geckodriver.exe");

				driver = new FirefoxDriver();

			} else if (Config.getProperty("browser").equalsIgnoreCase("chrome")) {

				System.setProperty("webdriver.chrome.driver",System.getProperty("user.dir") + "\\Drivers\\chromedriver.exe");

				driver = new ChromeDriver();

			} else if (Config.getProperty("browser").equalsIgnoreCase("ie")) {

				System.setProperty("webdriver.ie.driver", System.getProperty("user.dir") + "\\Drivers\\IEDriverServer.exe");

				driver = new InternetExplorerDriver();

			} else {

				System.setProperty("webdriver.chrome.driver",
						System.getProperty("user.dir") + "\\Drivers\\chromedriver.exe");

				driver = new ChromeDriver();
			}

			driver.manage().timeouts().implicitlyWait(Integer.parseInt(Config.getProperty("implicit.wait")),
					TimeUnit.SECONDS);

			driver.manage().window().maximize();

		}

		/*********************************************Generic methods********************************************/

		//Click generic method
		public void click(String locator) throws Exception {

			WebElement element = getLocator(locator);

			element.click();

		}
		//Click method when using FindElements()
		public void clicksOn(String locator, int itemIndex) throws Exception {

			List <WebElement> elements = getLocators(locator);

			highLighterMethod(driver, elements.get(itemIndex));

			elements.get(itemIndex).click();

		}
		//Get text method
		public String fetchText(String locator, int itemIndex) throws Exception {

			List <WebElement> elements = getLocators(locator);

			highLighterMethod(driver, elements.get(itemIndex));

			String fetchedText = elements.get(itemIndex).getText();

			return fetchedText;
		}

		// Highlighter generic method for WebElement
		public void highLighterMethod(WebDriver driver, WebElement element) throws Exception {

			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(false);", element);

			JavascriptExecutor js = (JavascriptExecutor) driver;

			js.executeScript("arguments[0].setAttribute('style', 'border: 5px solid yellow;');", element);

		}
		
		//Generic method for splitting OR by locator type and value
		public WebElement getLocator(String OR_locatorLabel) throws Exception {
			
			String locator = OR.getProperty(OR_locatorLabel);

			// extract the locator type and value from the object
			String locatorType = locator.split(":")[0];
			String locatorValue = locator.split(":")[1];

			if(locatorType.toLowerCase().equals("id")) {

				WebElement element = driver.findElement(By.id(locatorValue));

				highLighterMethod(driver, element);

				return element;
			}
			else if(locatorType.toLowerCase().equals("name")) {

				WebElement element = driver.findElement(By.name(locatorValue));

				highLighterMethod(driver, element);

				return element;
			}
			else if((locatorType.toLowerCase().equals("classname")) || (locatorType.toLowerCase().equals("class"))) {

				WebElement element = driver.findElement(By.className(locatorValue));

				highLighterMethod(driver, element);

				return element;
			}
			else if((locatorType.toLowerCase().equals("tagname")) || (locatorType.toLowerCase().equals("tag"))) {

				WebElement element = driver.findElement(By.tagName(locatorValue));

				highLighterMethod(driver, element);

				return element;
			}
			else if((locatorType.toLowerCase().equals("linktext")) || (locatorType.toLowerCase().equals("link"))) {

				WebElement element = driver.findElement(By.linkText(locatorValue));

				highLighterMethod(driver, element);

				return element;
			}
			else if(locatorType.toLowerCase().equals("partiallinktext")) {

				WebElement element = driver.findElement(By.partialLinkText(locatorValue));

				highLighterMethod(driver, element);

				return element;
			}
			else if((locatorType.toLowerCase().equals("cssselector")) || (locatorType.toLowerCase().equals("css"))) {

				WebElement element = driver.findElement(By.cssSelector(locatorValue));

				highLighterMethod(driver, element);

				return element;
			}
			else if(locatorType.toLowerCase().equals("xpath")) {

				WebElement element = driver.findElement(By.xpath(locatorValue));

				highLighterMethod(driver, element);

				return element;
			}
			else
				throw new Exception("Unknown locator type '" + locatorType + "'");
		}

		//get List of elements by OR
		public List <WebElement> getLocators(String OR_locatorLabel) throws Exception {

			String locator = OR.getProperty(OR_locatorLabel);

			// extract the locator type and value from the object
			String locatorType = locator.split(":")[0];
			String locatorValue = locator.split(":")[1];

			if((locatorType.toLowerCase().equals("classname")) || (locatorType.toLowerCase().equals("class"))) {

				List <WebElement> elements = driver.findElements(By.className(locatorValue));

				return elements;
			}
			else if(locatorType.toLowerCase().equals("xpath")) {

				List <WebElement> elements = driver.findElements(By.xpath(locatorValue));

				return elements;
			} //We can have more locators type here like we have in above methods
			//However using only few types here since we don't need other locator types as of now.
			else
				throw new Exception("Unknown locator type '" + locatorType + "'");
		}
		//Method to write in properties file
		public static void writeInPropertiesFile(Properties pr, String propertyName, String propertyValue) throws IOException {

			pr.setProperty(propertyName, propertyValue);

			fos = new FileOutputStream(System.getProperty("user.dir") + "\\src\\test\\resources\\propertiesFiles\\ExtractedData.properties");

			pr.store(fos, "Extracted Data");
	}


	/**************************************************** Methods End ***********************************************************************/

	@AfterSuite
	public void tearDown() {

		if (driver != null) {

			driver.quit();

			//Execution completed

		}

	}

}
