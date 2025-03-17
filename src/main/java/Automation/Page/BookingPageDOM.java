package Automation.Page;

import com.github.javafaker.Faker;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import Automation.Page.BasePage;


import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static java.lang.System.out;

public class BookingPageDOM {

    protected WebDriver driver;
    protected Actions actions;
    protected WebDriverWait wait;
    protected Random random = new Random();
    protected Faker faker;

    /*****SELECTORS*****/

    private By advancedFiltersDropdownLocator = By.cssSelector("div #search-advanced-filters-button");
    private By popupSelectorX = By.cssSelector("div #fpw_text_container");
    private By destinationDropdown = By.cssSelector("div[automation-id='search-destination']");
    private By calendarDropdown = By.cssSelector("div .vdp-datepicker__calendar--columns-months");
    private By dateDropdown = By.cssSelector("div[automation-id='search-dates']");
    private By departureDropdown = By.cssSelector("div[automation-id='search-departures']");
    private By dropdownContainer = By.cssSelector("div .dropdown__container");
    private By searchButton = By.cssSelector("div[automation-id='search-button']");
    private By cruiseListContainer = By.cssSelector("div .search_results__container");
    private By promoRibbons = By.cssSelector("div .promo-ribbon");
    private By firstButtonLocator = By.cssSelector("button[id^='SelectItinerary_']");
    private By itineraryDetailsSection = By.cssSelector("div .itinerary-options__buttons-wrapper");
    private By secondButtonLocator = By.cssSelector("button[id^='SelectItinerarySummary_']");
    private By listContainer = By.cssSelector("div #pricing");
    private By selectedCabin = By.cssSelector("button[automation-id^='cabin-type-selection-button-']");


    public BookingPageDOM(WebDriver driver) {
        this.driver = driver;
        this.actions = new Actions(driver);
        this.wait = BasePage.getWait();
        this.faker = BasePage.getFaker();
    }

    /*****METHODS*****/

    //FYI
    public void testFYI () {
        try {
            out.println("I'm inside testFYI");
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".crm-sticky-banner__tooltip")));
            selectDestination();
            selectDate();
            selectDeparture();
            clickSearchButton();
        } catch (Exception e) {
            out.println("Error: Not element found for the 'testFYI' " + e.getMessage());
        }
    }

    // Method to select the destination
    public void selectDestination() {
        try {
            out.println("Selecting destination");
            out.println("The destination is: " + destinationDropdown);
            WebElement destinationDropdownElement = BasePage.waitForElementToBeClickableLocator(driver, destinationDropdown);
            BasePage.scrollToElementSmoothly(driver, destinationDropdownElement);
            BasePage.clickWithActions(driver, destinationDropdownElement);
            List<WebElement> destinationList = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector("div.checkbox-label.checklist-item__label.checkbox-label--enabled")));

            if (destinationList.isEmpty()) {
                throw new AssertionError("Error: No destinations available");
            }

            Random rand = new Random();
            int randomIndex = rand.nextInt(destinationList.size());
            WebElement randomDestination = destinationList.get(randomIndex);
            out.println("Random destination selected: " + randomDestination.getText());

            BasePage.scrollToElementSmoothly(driver, randomDestination);
            BasePage.clickWithActions(driver, randomDestination);

        } catch (Exception e) {
            out.println("Error to select the destination: " + e.getMessage());
        }
    }

    // Method to select the date
    public void selectDate() {
        try {
            out.println("Selecting date");
            WebElement dateDropdownElement = BasePage.waitForElementToBeClickableLocator(driver, dateDropdown);
            BasePage.scrollToElementSmoothly(driver, dateDropdownElement);
            BasePage.clickWithActions(driver, dateDropdownElement);
            WebElement calendarContainer = wait.until(ExpectedConditions.visibilityOfElementLocated(calendarDropdown));

            List<WebElement> allDateOptions = calendarContainer.findElements(By.cssSelector(".cell.month"));
            List<WebElement> enabledDateOptions = allDateOptions.stream()
                    .filter(option -> !option.getAttribute("class").contains("disable"))
                    .collect(Collectors.toList());

            if (enabledDateOptions.isEmpty()) {
                throw new AssertionError("Error: No available dates to select");
            }

            int indexToSelect = new Random().nextInt(enabledDateOptions.size());
            WebElement selectedDate = enabledDateOptions.get(indexToSelect);
            out.println("Selected date: " + selectedDate.getText());

            BasePage.scrollToElementSmoothly(driver, selectedDate);
            BasePage.clickWithActions(driver, selectedDate);

            if (!selectedDate.isSelected()) {
                BasePage.clickWithJS(driver, selectedDate);
                out.println("Date clicked via JavaScript.");
            }

        } catch (Exception e) {
            out.println("Error selecting the date: " + e.getMessage());
        }
    }

    // Method to select the departures
    public void selectDeparture() {
        try {
            out.println("Selecting departure");
            out.println("Selector for departures: " + departureDropdown);
            WebElement departureDropdownElement = BasePage.waitForElementToBeClickableLocator(driver, departureDropdown);
            BasePage.scrollToElementSmoothly(driver, departureDropdownElement);
            BasePage.clickWithActions(driver, departureDropdownElement);

            List<WebElement> departureList = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector("div.checkbox-label.checklist-item__label.checkbox-label--enabled")));

            if (departureList.isEmpty()) {
                throw new AssertionError("Error: No departures available");
            }

            Random rand = new Random();
            int randomIndex = rand.nextInt(departureList.size());
            WebElement randomDeparture = departureList.get(randomIndex);
            out.println("Random departure selected: " + randomDeparture.getText());

            BasePage.scrollToElementSmoothly(driver, randomDeparture);
            BasePage.clickWithActions(driver, randomDeparture);

        } catch (Exception e) {
            out.println("Error to select the departure: " + e.getMessage());
        }
    }


    // Method to select the search button
    public void clickSearchButton() {
        out.println("Clicking the search button");
        WebElement searBut = driver.findElement(searchButton);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", searBut);
        actions.moveToElement(searBut).click().perform();
    }

    // Method to select the cruise card
    public void selectCruiseCard () {
        try {
            out.println("I'm inside selectCruiseCard");
            BasePage.waitForElementsPresentAndVisible(driver, cruiseListContainer);
            List<WebElement> cruiseList = BasePage.waitForElementsPresentAndVisible(driver, By.cssSelector(".search_results__container .itinerary-card"));
            out.println("There are " + cruiseList.size() + " cruise cards");
            Assert.assertFalse(cruiseList.isEmpty(), "Error: No options are present");
            WebElement selectedCruiseCard = cruiseList.getFirst();
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", selectedCruiseCard);
            List<WebElement> promoRibs = selectedCruiseCard.findElements(promoRibbons);
            for (WebElement ribbon : promoRibs) {
                if (ribbon.getText().trim().equalsIgnoreCase("VOLI INCLUSI")) {
                    out.println("Error: Selected cruise has 'VOLI INCLUSI', test aborted");
                    Assert.fail("Error: Selected cruise has 'VOLI INCLUSI', test aborted");
                    return;
                }
            }
            closePopupIfExists(By.cssSelector("#outerContainer > div.ui-dialog.insightera-dialog"), By.cssSelector("#Layer_1"), "Popup #outerContainer");
            WebElement firstButton = BasePage.findElement(driver, firstButtonLocator);
            assert firstButton != null;
            out.println("The first button is visible: " + firstButton.isDisplayed());
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center', inline: 'center'});", firstButton);
            try {
                BasePage.clickWithActions(driver, firstButton);
                out.println("First button clicked");
            } catch (Exception e) {
                out.println("First button not clicked, trying JavaScript");
                BasePage.clickWithJS(driver, firstButton);
            }
            WebElement itineraryDetails = BasePage.findElement(driver, itineraryDetailsSection);
            out.println("Itinerary details section is visible");
            WebElement secondButton = null;
            int scrollTries = 0;
            while (scrollTries < 5) {
                try {
                    secondButton = BasePage.waitForElementToBeClickableLocator(driver, secondButtonLocator);
                    if (secondButton.isDisplayed() && secondButton.isEnabled()) {
                        break;
                    }
                } catch (TimeoutException e) {
                    out.println("Waiting for the second button");
                }
                BasePage.scrollToElementSmoothly(driver, itineraryDetails);
                scrollTries++;
            }
            if (secondButton == null || !secondButton.isDisplayed() || !secondButton.isEnabled()) {
                out.println("Error: Second button is not visible or enabled");
                Assert.fail("Error: Second button is not visible");
                return;
            }
            out.println("Second button clicked");
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center', inline: 'center'});", secondButton);
            try {
                BasePage.waitForElementToBeClickable(driver, secondButton);
                BasePage.clickWithActions(driver, secondButton);
                out.println("Second button clicked successfully");
            } catch (Exception e) {
                out.println("Click failed, trying JavaScript");
                BasePage.clickWithJS(driver, secondButton);
            }
        } catch (Exception e) {
            out.println("Error: No element found " + e.getMessage());
            Assert.fail("Test failed");
        }
    }

    // Method to select the offer
    public void selectOffer () {
        try {
            out.println("I'm inside selectOffer");
            BasePage.waitForElementsPresentAndVisible(driver, listContainer);
            List<WebElement> offerList = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector("button[automation-id^='price-type-selection-button-']")));
            if (offerList.isEmpty()) {
                out.println("No offers found, waiting for elements...");
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("button[automation-id^='price-type-selection-button-']")));
                offerList = driver.findElements(By.cssSelector("button[automation-id^='price-type-selection-button-']"));
            }
            out.println("There are " + offerList.size() + " offers");
            Assert.assertFalse(offerList.isEmpty(), "Error: No options are present");
            if (offerList.size() > 1) {
                out.println("There are at least 2 type of offers");
                int rand3 = random.nextInt(offerList.size());
                WebElement selectedOffer = offerList.get(rand3);
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", selectedOffer);
                wait.until(ExpectedConditions.elementToBeClickable(selectedOffer));

                out.println("Attempting to click offer " + rand3);
                try {
                    actions.moveToElement(selectedOffer).click().perform();
                    out.println("Offer clicked");
                } catch (Exception e) {
                    out.println("Offer click failed");
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", selectedOffer);
                }

                out.println("Selected offer " + rand3);
            } else {
                out.println("Only one offer is available");
            }

            clickNextButton();
        } catch (Exception e) {
            out.println("Error: no element found - " + e.getMessage());
            Assert.fail("No offer found");
        }
    }

    // Method to select the cabin
    public void selectCabin() {
        try {
            out.println("I'm inside selectCabin");
            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".hooper-list")));

            List<WebElement> cabinList = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector("button[automation-id^='cabin-type-selection-button-']")));

            if (cabinList.isEmpty()) {
                out.println("No cabins found, waiting for elements...");
                cabinList = driver.findElements(selectedCabin);
            }

            out.println("There are " + cabinList.size() + " cabins");
            Assert.assertFalse(cabinList.isEmpty(), "Error: No cabins are present");

            WebElement selectedCabinElement;
            if (cabinList.size() > 1) {
                if (cabinList.size() > 4) {
                    out.println("There are more than 4 cabins, selecting from the second one");
                    Random rand = new Random();
                    int randomIndex = rand.nextInt(cabinList.size() - 1) + 1;
                    selectedCabinElement = cabinList.get(randomIndex);
                } else {
                    out.println("4 or fewer cabins, selecting the first one");
                    selectedCabinElement = cabinList.get(0);
                }

                out.println("Selected cabin: " + selectedCabinElement.getText());
                BasePage.scrollToElementSmoothly(driver, selectedCabinElement);
                BasePage.waitForElementToBeClickable(driver, selectedCabinElement);
                BasePage.clickWithActions(driver, selectedCabinElement);

                List<WebElement> cabinLabels = driver.findElements(By.cssSelector(".cabin-type__content__name--wrapper span"));
                boolean isMSCYachtClub = cabinLabels.stream()
                        .anyMatch(el -> el.getText().trim().equals("MSC Yacht Club"));

                closeWhatIsInclusive();
                if (isMSCYachtClub) {
                    out.println("MSC Yacht Club selected - clicking Next button 3 times.");
                    int attempts = 0;
                    int maxAttempts = 5;
                    while (attempts < maxAttempts) {
                        closeWhatIsInclusive();
                        clickNextButton();
                        Thread.sleep(500);

                        try {
                            WebElement passengersSection = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#section--passengers")));
                            if (passengersSection.isDisplayed()) {
                                out.println("Reached #section--passengers after " + (attempts + 1) + " clicks.");
                                break;
                            }
                        } catch (TimeoutException e) {
                            out.println("Still not at #section--passengers, clicking Next again...");
                        }
                        attempts++;
                    }
                    if (attempts == maxAttempts) {
                        out.println("Warning: Reached max attempts for clicking Next.");
                    }
                } else {
                    closeWhatIsInclusive();
                    clickNextButton();
                    if (verifyPassengerSection()) {
                        out.println("We are on Passenger Information page");
                    } else {
                        closeWhatIsInclusive();
                        clickNextButton();
                    }
                }
            } else {
                WebElement onlyCabin = cabinList.get(0);
                if (onlyCabin.getAttribute("class").contains("checked")) {
                    out.println("Only one cabin is available and already selected, clicking 'Next'.");
                    closeWhatIsInclusive();
                    clickNextButton();
                } else {
                    out.println("Only one cabin is available, selecting it and then clicking 'Next'.");
                    BasePage.scrollToElementSmoothly(driver, onlyCabin);
                    BasePage.waitForElementToBeClickable(driver, onlyCabin);
                    BasePage.clickWithActions(driver, onlyCabin);
                    closeWhatIsInclusive();
                    clickNextButton();
                }
            }
        } catch (Exception e) {
            out.println("Error: no element found - " + e.getMessage());
            Assert.fail("No cabin found");
        }
    }

    // Method to select the experience
    public void selectExperience() {
        try {
            out.println("I'm inside selectExperience");
            wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector(".hooper-track")));

            List<WebElement> experienceList = driver.findElements(By.cssSelector("div.hooper-list ul.hooper-track"));

            if (experienceList.isEmpty()) {
                out.println("No experience found, waiting for elements...");
                Assert.assertFalse(experienceList.isEmpty(), "Error: No options are present");
                return;
            }

            out.println("There are " + experienceList.size() + " type of experiences");

            if (experienceList.size() == 1) {
                out.println("Only one experience available, proceed to click Next button...");
                clickNextButton();
                return;
            }

            WebElement selectedExperience = experienceList.get(0);
            WebElement experienceButton = null;
            String textExperience = selectedExperience.getText().trim();

            if (textExperience.contains("Bella")) {
                out.println("The selected experience is Bella so the next step is Guest information");
                clickNextButton();
                if (verifyPassengerSection()) {
                    out.println("We are on Passenger Information page");
                } else {
                    clickNextButton();
                }
                return;
            } else {
                out.println("The experience isn't Bella so the next step isn't Guest information");
                experienceButton = selectedExperience.findElement(By.cssSelector("button[automation-id^='cabin-experience-selection-button-']"));
                wait.until(ExpectedConditions.elementToBeClickable(experienceButton));

                for (int i = 0; i < 5; i++) {
                    ((JavascriptExecutor) driver).executeScript("window.scrollBy(0, -300);");
                    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", experienceButton);
                    if (experienceButton.isDisplayed()) {
                        break;
                    }
                    ((JavascriptExecutor) driver).executeScript("window.scrollBy(0, -300);");
                    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", experienceButton);
                    Thread.sleep(500);
                }

                if (experienceButton.isDisplayed()) {
                    out.println("Attempting to click on the experience");
                    try {
                        actions.moveToElement(experienceButton).click().perform();
                        out.println("Experience clicked successfully");
                    } catch (Exception e) {
                        out.println("Experience click failed, trying JavaScript click");
                        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", experienceButton);
                    }
                } else {
                    out.println("Experience button is not visible after scrolling.");
                }
            }
        } catch (Exception e) {
            out.println("Error: no element found " + e.getMessage());
            Assert.fail("Error occurred while selecting experience");
        }
    }

    // Method to select the cabin position
    public void selectCabinPosition () {
        try {
            out.println("I'm inside testCabinPosition");
            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".hooper-track")));
            List<WebElement> cabinPositionList = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector("div.hooper-list ul.hooper-track")));
            if (cabinPositionList.isEmpty()) {
                out.println("No cabin position found, waiting for elements...");
                Assert.assertFalse(cabinPositionList.isEmpty(), "Error: No options are present");
                return;
            }
            out.println("There are" + cabinPositionList.size() + "type of cabins");
            if (cabinPositionList.size() == 1) {
                out.println("Only one experience available, proceed to click Next button...");
                clickNextButton();
                return;
            }
            WebElement selectedCabinPosition = cabinPositionList.getFirst();
            WebElement cabinPositionButton = null;
            try {
                cabinPositionButton = driver.findElement(By.cssSelector("div.cabin-type__content.cabin-type__content--cabins > button"));
            } catch (Exception e) {
                out.println("Cabin position button not found");
                Assert.fail("Cabin position button not found");
                return;
            }
            for (int i = 0; i < 5; i++) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", selectedCabinPosition);
                if (cabinPositionButton.isDisplayed()) break;
            }
            ((JavascriptExecutor) driver).executeScript("window.scroll(0,-100)");
            wait.until(ExpectedConditions.visibilityOf(cabinPositionButton));
            wait.until(ExpectedConditions.elementToBeClickable(cabinPositionButton));
            out.println("Attempting to click cabin position");
            try {
                actions.moveToElement(cabinPositionButton).clickAndHold().release().perform();
                out.println("Cabin position clicked");
            } catch (Exception e) {
                out.println("Cabin position click failed");
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", cabinPositionButton);
            }
            out.println("Selected cabin position");
            clickNextButton();
        } catch (Exception e) {
            out.println("Error: no element find " + e.getMessage());
            Assert.fail("No cabin position found");
        }
    }

    // Method to select the cabin number
    public void selectCabinNumber () {
        try {
            out.println("I'm inside testCabinNumber");
            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".app-booking-funnel-step-cabin-location")));
            List<WebElement> cabinNumberList = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector(".swiper-wrapper")));
            if (cabinNumberList.isEmpty()) {
                out.println("No cabin number found, waiting for elements...");
                Assert.assertFalse(cabinNumberList.isEmpty(), "Error: No options are present");
                return;
            }
            out.println("There are" + cabinNumberList.size() + " cabins numbers");
            if (cabinNumberList.size() == 1) {
                out.println("Only one experience available, proceed to click Next button...");
                clickNextButton();
                return;
            }
            WebElement selectedCabinNumber = cabinNumberList.getFirst();
            WebElement cabinNumberButton = null;
            try {
                cabinNumberButton = driver.findElement(By.cssSelector("span.cabins-carousel__item"));
            } catch (Exception e) {
                out.println("Cabin number button not found");
                Assert.fail("Cabin number button not found");
                return;
            }
            for (int i = 0; i < 5; i++) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", cabinNumberButton);
                if (selectedCabinNumber.isDisplayed()) break;
            }
            ((JavascriptExecutor) driver).executeScript("window.scroll(0,-100)");
            wait.until(ExpectedConditions.visibilityOf(cabinNumberButton));
            wait.until(ExpectedConditions.elementToBeClickable(cabinNumberButton));
            out.println("Attempting to click cabin number ");
            try {
                actions.moveToElement(cabinNumberButton).clickAndHold().release().perform();
                out.println("Cabin number clicked");
            } catch (Exception e) {
                out.println("Cabin number click failed");
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", cabinNumberButton);
            }
            out.println("Selected cabin number");
            clickNextButton();
        } catch (Exception e) {
            out.println("Error: no element find " + e.getMessage());
            Assert.fail("No cabin numberfound");
        }
    }

    // Method to fill the passenger form
    public void passengerRegister () {
        try {
            out.println("I'm inside testRegistroPasegero");
            Thread.sleep(500);
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#section--passengers")));
            String name1 = faker.name().firstName();
            String lastname1 = faker.name().lastName();
            String name2 = faker.name().firstName();
            String lastname2 = faker.name().lastName();
            String email1 = faker.internet().emailAddress();
            String phone1 = faker.phoneNumber().cellPhone();
            clickOutsidePopup();
            LocalDate birthPassenger1 = faker.date().birthday(20, 70).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate birthPassenger2 = faker.date().birthday(20, 70).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            clickOutsidePopup();
            BasePage.selectDinningOptions();
            BasePage.fillPassengerForm(1, name1, lastname1, email1, phone1, birthPassenger1, true);
            BasePage.fillPassengerForm(2, name2, lastname2, "", "", birthPassenger2, false);
            clickOutsidePopup();
            BasePage.fillDocumentType(1);
            BasePage.fillDocumentType(2);
            clickOutsidePopup();
            BasePage.fillEmergencyContact();
            Thread.sleep(500);
            clickNextButton();
        } catch (Exception e) {
            out.println("Error: no element find " + e.getMessage());
            e.printStackTrace();
            Assert.fail("Test failed due to an error");
        }
    }

    // Method to select the offers
    public void selectSpecialOffers () {
        try {
            out.println("I'm inside testSpecialOffers");
            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#pricing")));
            List<WebElement> specialOffersList = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector("div.hooper-list ul.hooper-track li")));
            if (specialOffersList.isEmpty()) {
                out.println("No special offers found, stopping test.");
                Assert.fail("Error: No special offers available.");
                return;
            }
            WebElement firstOffer = specialOffersList.get(0);
            try {
                firstOffer.click();
                out.println("Selected first available special offer.");
                WebElement increaseButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button.counter__button")));
                increaseButton.click();
                out.println("Clicked on '+' button to increase quantity.");
            } catch (Exception e) {
                out.println("Failed to select special offer or click '+': " + e.getMessage());
            }
            clickNextButton();
        } catch (Exception e) {
            out.println("Error in the special offer step: " + e.getMessage());
            clickNextButton();
        }
    }

    // Method to select the insurance
    public void proceedToCheckout () {
        try {
            clickNextButton();
            WebElement checkoutPage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("a[automation-id^='nav-action-next']")));
            if (checkoutPage.isDisplayed()) {
                try {
                    WebElement radioOption = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".payment-choice__radio--left input[type='radio']")));
                    if (!radioOption.isSelected()) {
                        radioOption.click();
                        out.println("Payment method selected");
                    } else {
                        out.println("Payment option was already selected");
                    }
                } catch (Exception e) {
                    out.println("No payment option available or already selected");
                }
                clickNextButton();
                try {
                    WebElement popupPayments = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#payment-method")));
                    if (popupPayments.isDisplayed()) {
                        out.println("Payment method displayed.");
                        out.println("The test was successfully concluded.");
                    }
                } catch (Exception e) {
                    out.println("Payment popup did not appear. Test might need revision.");
                }
            }
        } catch (Exception e) {
            out.println("Restart from the beginning... something went wrong.");
            e.printStackTrace();
        }
    }

    /*****AUXILIARY METHODS*****/

    //close popups methods
    private void closePopupIfExists (By popupLocator, By closeButtonLocator, String popupName){
        try {
            if (BasePage.isElementVisible(driver, popupLocator)) {
                out.println("Closing " + popupName + "...");
                List<WebElement> closeButtons = driver.findElements(closeButtonLocator);
                if (!closeButtons.isEmpty()) {
                    closeButtons.get(0).click();
                    out.println(popupName + " closed using close button.");
                } else {
                    out.println(popupName + " has no close button, trying to click outside...");
                    clickOutsidePopup();
                }
                BasePage.waitForElementToDisappear(driver, popupLocator);
                out.println(popupName + " closed successfully.");
            } else {
                out.println(popupName + " not found or already closed.");
            }
        } catch (Exception e) {
            out.println("Error closing " + popupName + ": " + e.getMessage());
        }
    }

    public void closeAdvancedFiltersDropdown () {
        try {
            out.println("Inside closeAdvancedFiltersDropdown method");
            if (BasePage.isElementVisible(driver, advancedFiltersDropdownLocator)) {
                out.println("Advanced Filters dropdown detected, attempting to close it...");
                WebElement advanceFilter = driver.findElement(advancedFiltersDropdownLocator);
                BasePage.clickElement(driver, advanceFilter);
                wait.until(ExpectedConditions.invisibilityOfElementLocated(advancedFiltersDropdownLocator));
                out.println("Advanced Filters dropdown closed successfully.");
            }
        } catch (Exception e) {
            out.println("No advanced filters dropdown detected or error closing it: " + e.getMessage());
        }
    }

    public void closeWhatIsInclusive () {
        try {
            WebElement inclusivePopup = driver.findElement(By.cssSelector("div.modal-body"));
            if (inclusivePopup.isDisplayed()) {
                out.println("What is Inclusive detected, attempting to close it...");
                clickOutsidePopup();
                wait.until(ExpectedConditions.invisibilityOf(inclusivePopup));
                out.println("What is inclusive popup closed successfully.");
            }
        } catch (Exception e) {
            out.println("No what is inclusive popup detected or error closing it: " + e.getMessage());
        }
    }

    public void clickOutsidePopup () {
        try {
            Dimension windowSize = driver.manage().window().getSize();
            int offsetX = windowSize.width - 10;
            int offsetY = windowSize.height - 10;
            Actions actions = new Actions(driver);
            actions.moveByOffset(offsetX, offsetY).click().perform();
            out.println("Clicked outside to close the popup.");
        } catch (Exception e) {
            out.println("Failed to click outside the popup: " + e.getMessage());
        }
    }

    // Closing all popups if present
    private void closeAllPopups () {
        out.println("Attempting to close all popups if present...");
        closePopupIfExists(By.cssSelector("#outerContainer > div.ui-dialog.insightera-dialog"), By.cssSelector("#Layer_1"), "Popup #outerContainer");
        closePopupIfExists(By.cssSelector("body > div:nth-child(1)"), By.cssSelector("body > div:nth-child(1)"), "Popup body > div:nth-child(1)");
        closePopupIfExists(dropdownContainer, departureDropdown, "Departure dropdown");
        closeAdvancedFiltersDropdown();
        clickOutsidePopup();
        out.println("All necessary popups have been handled.");
    }

    // Method to manage opened dropdowns
    private void handleOpenedDropdown (WebDriver driver, By locator){
        boolean isDropdownVisible = BasePage.searchSelectorInDOM(locator);
        if (isDropdownVisible) {
            WebElement dropdownMenu = driver.findElement(locator);
            BasePage.scrollToElementSmoothly(driver, dropdownMenu);
            closePopupIfExists(locator, locator, "");
            BasePage.waitForElementToDisappear(driver, locator);
            out.println("Dropdown closed successfully.");
        } else {
            out.println("Dropdown was not visible or already closed.");
        }
    }

    public boolean verifyPassengerSection () {
        try {
            out.println("Verifying if we reached the passengers section...");
            WebElement passengersSection = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#section--passengers")));
            if (passengersSection.isDisplayed()) {
                out.println("Successfully reached the passengers section.");
                return true;
            } else {
                out.println("Failed to reach the passengers section.");
                return false;
            }
        } catch (TimeoutException e) {
            out.println("Timeout: Unable to find the passengers section within the expected time.");
            return false;
        } catch (Exception e) {
            out.println("Error verifying the passengers section: " + e.getMessage());
            return false;
        }
    }

    public WebElement getNextButton () {
        return wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a[automation-id^='nav-action-next']")));
    }

    public void clickNextButton() {
        try {
            Thread.sleep(500);
            WebElement nextbutton = getNextButton();
            wait.until(ExpectedConditions.visibilityOf(nextbutton));
            wait.until(ExpectedConditions.elementToBeClickable(nextbutton));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", nextbutton);
            Thread.sleep(500);
            try {
                actions.moveToElement(nextbutton).clickAndHold().release().perform();
                out.println("Next button clicked");
            } catch (Exception e) {
                out.println("Click failed " + e.getMessage());
                try {
                    actions.moveToElement(nextbutton).click().perform();
                    out.println("Next button clicked normaly");
                } catch (Exception e2) {
                    out.println("Next button not clicked normaly");
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", nextbutton);
                }
            }
            out.println("Next button clicked successffully");
        } catch (Exception e) {
            out.println("Error clicking Next button: " + e.getMessage());
            Assert.fail("Next button click failed.");
        }
    }

}
