import Automation.TestConfig.DriverTestBase;
import Automation.Page.BasePage;
import Automation.Page.BookingPageDOM;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

public class BookingTest extends DriverTestBase {
    BookingPageDOM bookingPageDOMPage;
    Logger logger = LogManager.getLogger(BookingTest.class);

    @Test
    public void homePage(){

        BasePage.acceptCookies();

        logger.info("Starting booking test...");
        logger.info("Inside the search");
        bookingPageDOMPage.testFYI();

        logger.info("Search curise started");
        bookingPageDOMPage.selectCruiseCard();
    }

    @Test(dependsOnMethods = {"homePage"})
    public void firstStepOfBF() {
        logger.info("Step 0 complete");
        bookingPageDOMPage.selectOffer();
    }

    @Test(dependsOnMethods = {"firstStepOfBF"})
    public void secondStepOfBF() {
        logger.info("Step 1 complete");
        bookingPageDOMPage.selectCabin();

    }

    @Test(dependsOnMethods = {"secondStepOfBF"})
    public void thirdStepOfBF() {
        logger.info("Step 2 complete");
        bookingPageDOMPage.selectExperience();

    }

    @Test(dependsOnMethods = {"thirdStepOfBF"})
    public void fourthStepOfBF() {

        logger.info("Step 3 complete");
        bookingPageDOMPage.selectCabinPosition();

    }

    @Test(dependsOnMethods = {"fourthStepOfBF"})
    public void fifthStepOfBF() {

        logger.info("Step 4 complete");
        bookingPageDOMPage.selectCabinNumber();

    }

    @Test(dependsOnMethods = {"fifthStepOfBF"})
    public void sixthStepOfBF() {

        logger.info("Step 5 complete");
        bookingPageDOMPage.passengerRegister();
    }

    @Test(dependsOnMethods = {"sixthStepOfBF"})
    public void seventhStepOfBF() {

        logger.info("Step 6 complete");
        bookingPageDOMPage.selectSpecialOffers();
    }

    @Test(dependsOnMethods = {"seventhStepOfBF"})
    public void eihgthStepOfBF() {

        logger.info("Step 7 complete");
        bookingPageDOMPage.proceedToCheckout();
    }

    @Test(dependsOnMethods = {"eighthStepOfBF"})
    public void validateResults() {

        logger.info("Validation of booking result");
        Assert.assertTrue(driver.getTitle().contains("Results"), "Search was not successful.");
    }

}

