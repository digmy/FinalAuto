import Automation.Page.BasePage;
import Automation.TestConfig.DriverTestBase;
import Automation.Page.BookingPageDOM;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static java.lang.System.out;

public class BookingTest extends DriverTestBase {
    BookingPageDOM bookingPageDOMPage;

    @BeforeClass
    public void setup() {
        bookingPageDOMPage = new BookingPageDOM(getDriver());
    }

    @Test
    public void testBookingSearch() {
        BasePage.openSite("https://www.msccrociere.it");
        out.println("Starting booking test...");
    }

    @Test(dependsOnMethods = {"testBookingSearch"})
    public void homePage(){
        out.println("Inside the search");
        bookingPageDOMPage.testFYI();
    }

    @Test(dependsOnMethods = {"homePage"})
        public void searchCruiseInHomePage(){
        out.println("Search curise started");
        bookingPageDOMPage.selectCruiseCard();
    }

    @Test(dependsOnMethods = {"searchCruiseInHomePage"})
    public void offertStepOfBF() {
        out.println("Step 0 complete");
        bookingPageDOMPage.selectOffer();
    }

    @Test(dependsOnMethods = {"offertStepOfBF"})
    public void selectCabinStepOfBF() {
        out.println("Step 1 complete");
        bookingPageDOMPage.selectCabin();

    }

    @Test(dependsOnMethods = {"selectCabinStepOfBF"})
    public void experienceStepOfBF() {
        out.println("Step 2 complete");
        bookingPageDOMPage.selectExperience();

    }

    @Test(dependsOnMethods = {"experienceStepOfBF"})
    public void cabinPositionStepOfBF() {

        out.println("Step 3 complete");
        bookingPageDOMPage.selectCabinPosition();

    }

    @Test(dependsOnMethods = {"cabinPositionStepOfBF"})
    public void cabinSelectStepOfBF() {

        out.println("Step 4 complete");
        bookingPageDOMPage.selectCabinNumber();

    }

    @Test(dependsOnMethods = {"cabinSelectStepOfBF"})
    public void passengerInfoStepOfBF() {

        out.println("Step 5 complete");
        bookingPageDOMPage.passengerRegister();
    }

    @Test(dependsOnMethods = {"passengerInfoStepOfBF"})
    public void specialOffStepOfBF() {

        out.println("Step 6 complete");
        bookingPageDOMPage.selectSpecialOffers();
    }

    @Test(dependsOnMethods = {"specialOffStepOfBF"})
    public void checkoutStepOfBF() {

        out.println("Step 7 complete");
        bookingPageDOMPage.proceedToCheckout();
    }

    @Test(dependsOnMethods = {"checkoutStepOfBF"})
    public void validateResults() {

        out.println("Validation of booking result");
        Assert.assertTrue(driver.getTitle().contains("Results"), "Search was not successful.");
    }

}

