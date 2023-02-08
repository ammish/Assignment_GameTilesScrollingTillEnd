package assignment;


import org.testng.Assert;
import org.testng.annotations.*;

import base.GenericMethods;

public class Assignment_TestCase extends GenericMethods {
	
	/*************
	 * Note: Please see "Assignment\src\test\resources\propertiesFiles\ExtractedData.properties" file before and after execution,
	 * 		 This file will contain outputs after execution. 
	 */

	@Test(priority=0)
	public void clickOnGameTile() throws Exception {

		int gameTileIndex = 27;

		//Launching URL: https://game.tv
		driver.get(Config.getProperty("url"));

		//Validation - To get assured that our URL is launched successfully
		Assert.assertTrue((getLocator("homepage.logo")).isDisplayed(), "FAIL: URL is not launched successfully");

		//Getting text of game tile on which we will click
		String gameName_Text = fetchText("homepage.gameItems", gameTileIndex);

		//Writing gameName text value in 'src\test\resources\propertiesFiles\ExtractedData.properties' file to store output
		writeInPropertiesFile(ExtractedData, "gameName", gameName_Text);

		//Clicking on 26th element(Game) from WebElement list
		clicksOn("homepage.gameItems", gameTileIndex);

		//Validation - To get assured that we are on game details page
		Assert.assertEquals(gameName_Text, getLocator("gameDetailPage.gameName").getText());

	}

	@Test(priority=1)
	public void getTournamentCount() throws Exception {

		//Getting text of game details of game
		String gameDetails_Text = getLocator("gameDetailPage.gameDetails").getText();

		//Writing gameDetails text value in 'ExtractedData.properties' file to store output
		writeInPropertiesFile(ExtractedData, "gameDetails", gameDetails_Text);

		//while loop to click on 'More View' button only if the button is visible
		while(getLocators("gameDetailPage.viewMore_Btn_Hidden").size() == 0) {

			//Click on 'View More' button
			getLocator("gameDetailPage.viewMore_Btn_Visible").click();

		}

		//Getting total count of tournaments of game
		int totalTournamentCount = getLocators("gameDetailPage.tournaments").size();

		//Writing totalTournamentCount value in 'ExtractedData.properties' file to store output
		writeInPropertiesFile(ExtractedData, "totalTournamentCount", Integer.toString(totalTournamentCount));

		//Getting total tournament count text visible in top
		String availableTournamentsCount = getLocator("gameDetailPage.availableTournamentsCount").getText();

		//Matching counts of tournament with count mentioned in top
		boolean countMatches = ExtractedData.getProperty("totalTournamentCount").equals(availableTournamentsCount);

		if(countMatches){

			//If count matches it will write 'true' in Extracted Data file
			writeInPropertiesFile(ExtractedData, "CountMatches(true/false)", Boolean.toString(countMatches));
		}else {

			//If count matches it will write 'true' in Extracted Data file
			writeInPropertiesFile(ExtractedData, "CountMatches(true/false)", Boolean.toString(countMatches));
		}

	}
}