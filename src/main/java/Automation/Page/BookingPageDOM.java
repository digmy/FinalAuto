package Automation.Page;

import com.github.javafaker.Faker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;


import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static java.lang.System.out;

public class BookingPageDOM extends BasePage {

    protected Actions actions;
    protected WebDriverWait wait;
    protected Random random = new Random();
    protected Faker faker;
    Logger logger = LogManager.getLogger(BookingPageDOM.class);

    /*****SELECTORS*****/

    // Advance filters selector
    private By advancedFiltersDropdownLocator = By.cssSelector("div #search-advanced-filters-button");

    // Popup X
    private By popupSelectorX = By.cssSelector("div #fpw_text_container");

    // Destination selector
    private By destinationDropdown = By.cssSelector("div[automation-id='search-destination']");

    // Dates selectors
    private By dateDropdown = By.cssSelector("div[automation-id='search-dates']");

    // Departures selectors
    private By departureDropdown = By.cssSelector("div[automation-id='search-departures']");
    private By dropdownContainer = By.cssSelector("div .dropdown__container");


    // Search selector
    private By searchButton = By.cssSelector("div[automation-id='search-button']");

    // Cruise Cards selectors
    private By cruiseListContainer = By.cssSelector("div .search_results__container");
    private By promoRibbons = By.cssSelector("div .promo-ribbon");
    private By firstButtonLocator = By.cssSelector("button[id^='SelectItinerary_']");
    private By itineraryDetailsSection = By.cssSelector("div .itinerary-options__buttons-wrapper");
    private By secondButtonLocator = By.cssSelector("button[id^='SelectItinerarySummary_']");


    // Offer selectors
    private By selectedOffer = By.cssSelector("button.button.checkButton[automation-id='price-type-selection-button-1']");
    private By listContainer = By.cssSelector("div #pricing");


    // Cabin selectors
    private By cabList = By.cssSelector("div .hooper-list");
    private By selectedCabin = By.cssSelector("button[automation-id^='cabin-type-selection-button-']");


    public BookingPageDOM(WebDriver driver) {
        super(driver);
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
    public void selectDestination () {
        try {
                logger.info("Selecting destination");
                logger.info("The destination is: " + destinationDropdown);
            WebElement destinationDropdownElement = BasePage.waitForElementToBeClickableLocator(driver, destinationDropdown);
                BasePage.scrollToElementSmoothly(driver, destinationDropdownElement);
                BasePage.clickWithActions(driver, destinationDropdownElement);
                List<WebElement> destinationList = driver.findElements(By.cssSelector("div.checkbox-label.checklist-item__label.checkbox-label--enabled"));
                if (destinationList.isEmpty()) {
                    throw new AssertionError("Error: No destinations available");
                }
                Random rand = new Random();
                int randomIndex = rand.nextInt(destinationList.size());
                WebElement randomDestination = destinationList.get(randomIndex);
                logger.info("Random destination selected: " + randomDestination.getText());
                BasePage.scrollToElementSmoothly(driver, randomDestination);
                BasePage.clickWithActions(driver, randomDestination);
        } catch (Exception e) {
                logger.info("Error to select the destination: " + e.getMessage());
        }
    }

    // Method to select the date
    public void selectDate () {
            try {
                logger.info("Selecting date");
                WebElement dateDropdownElement = BasePage.waitForElementToBeClickableLocator(driver, dateDropdown);
                BasePage.scrollToElementSmoothly(driver, dateDropdownElement);
                BasePage.clickWithActions(driver, dateDropdownElement);
                WebElement calendarContainer = driver.findElement(By.cssSelector(".cell.month"));
                List<WebElement> allDateOptions = calendarContainer.findElements(By.cssSelector(".cell.month"));
                List<WebElement> enabledDateOptions = allDateOptions.stream()
                        .filter(option -> !option.getAttribute("class").contains("disable"))
                        .collect(Collectors.toList());
                if (enabledDateOptions.isEmpty()) {
                    throw new AssertionError("Error: No available dates to select");
                }
                int indexToSelect = new Random().nextInt(enabledDateOptions.size());
                WebElement selectedDate = enabledDateOptions.get(indexToSelect);
                logger.info("Selected date: " + selectedDate.getText());
                BasePage.scrollToElementSmoothly(driver, selectedDate);
                BasePage.clickWithActions(driver, selectedDate);
                if (!selectedDate.isSelected()) {
                    BasePage.clickWithJS(driver, selectedDate);
                    logger.info("Date clicked via JavaScript.");
                }
            } catch (Exception e) {
                logger.info("Error selecting the date: " + e.getMessage());
            }
    }

    // Method to select the departures
    public void selectDeparture () {
            try {
                logger.info("Selecting departure");
                logger.info("Selector for departures: " + departureDropdown);
                WebElement departureDropdownElement = BasePage.waitForElementToBeClickableLocator(driver, departureDropdown);
                BasePage.scrollToElementSmoothly(driver, departureDropdownElement);
                BasePage.clickWithActions(driver, departureDropdownElement);
                List<WebElement> departureList = driver.findElements(By.cssSelector("div.checkbox-label.checklist-item__label.checkbox-label--enabled"));
                if (departureList.isEmpty()) {
                    throw new AssertionError("Error: No departures available");
                }
                Random rand = new Random();
                int randomIndex = rand.nextInt(departureList.size());
                WebElement randomDeparture = departureList.get(randomIndex);
                logger.info("Random departure selected: " + randomDeparture.getText());
                BasePage.scrollToElementSmoothly(driver, randomDeparture);
                BasePage.clickWithActions(driver, randomDeparture);
            } catch (Exception e) {
                logger.info("Error to select the departure: " + e.getMessage());
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
                logger.info("I'm inside selectCruiseCard");
                BasePage.waitForElementsPresentAndVisible(driver, cruiseListContainer);
                List<WebElement> cruiseList = BasePage.waitForElementsPresentAndVisible(driver, By.cssSelector(".search_results__container .itinerary-card"));
                logger.info("There are " + cruiseList.size() + " cruise cards");
                Assert.assertFalse(cruiseList.isEmpty(), "Error: No options are present");
                WebElement selectedCruiseCard = cruiseList.get(0);
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", selectedCruiseCard);
                List<WebElement> promoRibs = selectedCruiseCard.findElements(promoRibbons);
                for (WebElement ribbon : promoRibs) {
                    if (ribbon.getText().trim().equalsIgnoreCase("VOLI INCLUSI")) {
                        logger.info("Error: Selected cruise has 'VOLI INCLUSI', test aborted");
                        Assert.fail("Error: Selected cruise has 'VOLI INCLUSI', test aborted");
                        return;
                    }
                }
                closePopupIfExists(By.cssSelector("#outerContainer > div.ui-dialog.insightera-dialog"), By.cssSelector("#Layer_1"), "Popup #outerContainer");
                WebElement firstButton = BasePage.findElement(driver, firstButtonLocator);
                logger.info("The first button is visible: " + firstButton.isDisplayed());
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center', inline: 'center'});", firstButton);
                try {
                    BasePage.clickWithActions(driver, firstButton);
                    logger.info("First button clicked");
                } catch (Exception e) {
                    logger.info("First button not clicked, trying JavaScript");
                    BasePage.clickWithJS(driver, firstButton);
                }
                WebElement itineraryDetails = BasePage.findElement(driver, itineraryDetailsSection);
                logger.info("Itinerary details section is visible");
                WebElement secondButton = null;
                int scrollTries = 0;
                while (scrollTries < 5) {
                    try {
                        secondButton = BasePage.waitForElementToBeClickableLocator(driver, secondButtonLocator);
                        if (secondButton.isDisplayed() && secondButton.isEnabled()) {
                            break;
                        }
                    } catch (TimeoutException e) {
                        logger.info("Waiting for the second button");
                    }
                    BasePage.scrollToElementSmoothly(driver, itineraryDetails);
                    scrollTries++;
                }

                if (secondButton == null || !secondButton.isDisplayed() || !secondButton.isEnabled()) {
                    logger.info("Error: Second button is not visible or enabled");
                    Assert.fail("Error: Second button is not visible");
                    return;
                }
                logger.info("Second button clicked");
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center', inline: 'center'});", secondButton);
                try {
                    BasePage.waitForElementToBeClickable(driver, secondButton);
                    BasePage.clickWithActions(driver, secondButton);
                    logger.info("Second button clicked successfully");
                } catch (Exception e) {
                    logger.info("Click failed, trying JavaScript");
                    BasePage.clickWithJS(driver, secondButton);
                }
            } catch (Exception e) {
                logger.info("Error: No element found " + e.getMessage());
                Assert.fail("Test failed");
            }
    }

    // Method to select the offer
    public void selectOffer () {
            try {
                logger.info("I'm inside selectOffer");
                BasePage.waitForElementsPresentAndVisible(driver, listContainer);
                List<WebElement> offerList = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector("button[automation-id^='price-type-selection-button-']")));
                if (offerList.isEmpty()) {
                    logger.info("No offers found, waiting for elements...");
                    wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("button[automation-id^='price-type-selection-button-']")));
                    offerList = driver.findElements(By.cssSelector("button[automation-id^='price-type-selection-button-']"));
                }
                logger.info("There are " + offerList.size() + " offers");
                Assert.assertFalse(offerList.isEmpty(), "Error: No options are present");
                if (offerList.size() > 1) {
                    logger.info("There are at least 2 type of offers");
                    int rand3 = random.nextInt(offerList.size());
                    WebElement selectedOffer = offerList.get(rand3);
                    for (int i = 0; i < 5; i++) {
                        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", selectedOffer);
                        Thread.sleep(500);
                    }
                    wait.until(ExpectedConditions.visibilityOf(selectedOffer));

                    wait.until(ExpectedConditions.elementToBeClickable(selectedOffer));

                    logger.info("Attempting to click offer " + rand3);

                    try {
                        actions.moveToElement(selectedOffer).click().perform();
                        logger.info("Offer clicked");
                    } catch (Exception e) {
                        logger.info("Offer click failed");
                        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", selectedOffer);
                    }
                    logger.info("Selected offer " + rand3);
                } else {
                    logger.info("Only one offer is available");
                }
                clickNextButton();

            } catch (Exception e) {
                logger.info("Error: no element found - " + e.getMessage());
                Assert.fail("No offer found");
            }
        }

    // Method to select the cabin
    public void selectCabin () {
            try {
                logger.info("I'm inside selectCabin");
                wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".hooper-list")));
                List<WebElement> cabinList = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector("button[automation-id^='cabin-type-selection-button-']")));
                if (cabinList.isEmpty()) {
                    logger.info("No cabins found, waiting for elements...");
                    cabinList = driver.findElements(selectedCabin);
                }
                logger.info("There are " + cabinList.size() + " cabins");
                Assert.assertFalse(cabinList.isEmpty(), "Error: No cabins are present");
                WebElement selectedCabinElement;
                if (cabinList.size() > 1) {
                    if (cabinList.size() > 4) {
                        logger.info("There are more than 4 cabins, selecting from the second one");
                        Random rand = new Random();
                        int randomIndex = rand.nextInt(cabinList.size() - 1) + 1;
                        selectedCabinElement = cabinList.get(randomIndex);
                    } else {
                        logger.info("4 or fewer cabins, selecting the first one");
                        selectedCabinElement = cabinList.get(0);
                    }
                    logger.info("Selected cabin: " + selectedCabinElement.getText());
                    BasePage.scrollToElementSmoothly(driver, selectedCabinElement);
                    BasePage.waitForElementToBeClickable(driver, selectedCabinElement);
                    BasePage.clickWithActions(driver, selectedCabinElement);
                    List<WebElement> cabinLabels = driver.findElements(By.cssSelector(".cabin-type__content__name--wrapper span"));
                    boolean isMSCYachtClub = cabinLabels.stream()
                            .anyMatch(el -> el.getText().trim().equals("MSC Yacht Club"));
                    closeWhatIsInclusive();
                    if (isMSCYachtClub) {
                        logger.info("MSC Yacht Club selected - clicking Next button 3 times.");
                        int attempts = 0;
                        int maxAttempts = 5;
                        while (attempts < maxAttempts) {
                            closeWhatIsInclusive();
                            clickNextButton();
                            Thread.sleep(500);

                            try {
                                WebElement passengersSection = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#section--passengers")));
                                if (passengersSection.isDisplayed()) {
                                    logger.info("Reached #section--passengers after " + (attempts + 1) + " clicks.");
                                    break;
                                }
                            } catch (TimeoutException e) {
                                logger.info("Still not at #section--passengers, clicking Next again...");
                            }
                            attempts++;
                        }
                        if (attempts == maxAttempts) {
                            logger.info("Warning: Reached max attempts for clicking Next.");
                        }
                    } else {
                        closeWhatIsInclusive();
                        clickNextButton();
                        if (verifyPassengerSection()) {
                            logger.info("We are on Passenger Information page");
                        } else {
                            closeWhatIsInclusive();
                            clickNextButton();
                        }
                    }
                } else {
                    WebElement onlyCabin = cabinList.get(0);
                    if (onlyCabin.getAttribute("class").contains("checked")) {
                        logger.info("Only one cabin is available and already selected, clicking 'Next'.");
                        closeWhatIsInclusive();
                        clickNextButton();
                        if (verifyPassengerSection()) {
                            logger.info("We are on Passenger Information page");
                        } else {
                            closeWhatIsInclusive();
                            clickNextButton();
                        }
                    } else {
                        logger.info("Only one cabin is available, selecting it and then clicking 'Next'.");
                        BasePage.scrollToElementSmoothly(driver, onlyCabin);
                        BasePage.waitForElementToBeClickable(driver, onlyCabin);
                        BasePage.clickWithActions(driver, onlyCabin);
                        closeWhatIsInclusive();
                        clickNextButton();
                        if (verifyPassengerSection()) {
                            logger.info("We are on Passenger Information page");
                        } else {
                            closeWhatIsInclusive();
                            clickNextButton();
                        }
                    }
                }
            } catch (Exception e) {
                logger.info("Error: no element found - " + e.getMessage());
                Assert.fail("No cabin found");
            }
        }

    // Method to select the experience
    public void selectExperience () {
            try {
                logger.info("I'm inside selectExperience");
                wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector(".hooper-track")));
                List<WebElement> experienceList = driver.findElements(By.cssSelector("div.hooper-list ul.hooper-track"));
                if (experienceList.isEmpty()) {
                    logger.info("No experience found, waiting for elements...");
                    Assert.assertFalse(experienceList.isEmpty(), "Error: No options are present");
                    return;
                }
                logger.info("There are " + experienceList.size() + " type of experiences");
                if (experienceList.size() == 1) {
                    logger.info("Only one experience available, proceed to click Next button...");
                    clickNextButton();
                    return;
                }
                WebElement selectedExperience = experienceList.get(0);
                WebElement experienceButton = null;
                String textExperience = selectedExperience.getText().trim();
                if (textExperience.contains("Bella")) {
                    logger.info("The selected experience is Bella so the next step is Guest information");
                    clickNextButton();
                    if (verifyPassengerSection()) {
                        logger.info("We are on Passenger Information page");
                    } else {
                        clickNextButton();
                    }
                    return;
                } else {
                    logger.info("The experience isn't Bella so the next step isn't Guest information");
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
                        logger.info("Attempting to click on the experience");
                        try {
                            actions.moveToElement(experienceButton).click().perform();
                            logger.info("Experience clicked successfully");
                        } catch (Exception e) {
                            logger.info("Experience click failed, trying JavaScript click");
                            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", experienceButton);
                        }
                    } else {
                        logger.info("Experience button is not visible after scrolling.");
                    }
                }
            } catch (Exception e) {
                logger.info("Error: no element found " + e.getMessage());
                Assert.fail("Error occurred while selecting experience");
            }
        }

    // Method to select the cabin position
    public void selectCabinPosition () {
            try {
                logger.info("I'm inside testCabinPosition");
                wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".hooper-track")));
                List<WebElement> cabinPositionList = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector("div.hooper-list ul.hooper-track")));
                if (cabinPositionList.isEmpty()) {
                    logger.info("No cabin position found, waiting for elements...");
                    Assert.assertFalse(cabinPositionList.isEmpty(), "Error: No options are present");
                    return;
                }
                logger.info("There are" + cabinPositionList.size() + "type of cabins");
                if (cabinPositionList.size() == 1) {
                    logger.info("Only one experience available, proceed to click Next button...");
                    clickNextButton();
                    return;
                }
                WebElement selectedCabinPosition = cabinPositionList.getFirst();
                WebElement cabinPositionButton = null;
                try {
                    cabinPositionButton = driver.findElement(By.cssSelector("div.cabin-type__content.cabin-type__content--cabins > button"));
                } catch (Exception e) {
                    logger.info("Cabin position button not found");
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
                logger.info("Attempting to click cabin position");
                try {
                    actions.moveToElement(cabinPositionButton).clickAndHold().release().perform();
                    logger.info("Cabin position clicked");
                } catch (Exception e) {
                    logger.info("Cabin position click failed");
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", cabinPositionButton);
                }
                logger.info("Selected cabin position");
                clickNextButton();
            } catch (Exception e) {
                logger.info("Error: no element find " + e.getMessage());
                Assert.fail("No cabin position found");
            }
        }

    // Method to select the cabin number
    public void selectCabinNumber () {
            try {
                logger.info("I'm inside testCabinNumber");
                wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".app-booking-funnel-step-cabin-location")));
                List<WebElement> cabinNumberList = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector(".swiper-wrapper")));
                if (cabinNumberList.isEmpty()) {
                    logger.info("No cabin number found, waiting for elements...");
                    Assert.assertFalse(cabinNumberList.isEmpty(), "Error: No options are present");
                    return;
                }
                logger.info("There are" + cabinNumberList.size() + " cabins numbers");
                if (cabinNumberList.size() == 1) {
                    logger.info("Only one experience available, proceed to click Next button...");
                    clickNextButton();
                    return;
                }
                WebElement selectedCabinNumber = cabinNumberList.getFirst();
                WebElement cabinNumberButton = null;
                try {
                    cabinNumberButton = driver.findElement(By.cssSelector("span.cabins-carousel__item"));
                } catch (Exception e) {
                    logger.info("Cabin number button not found");
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
                logger.info("Attempting to click cabin number ");
                try {
                    actions.moveToElement(cabinNumberButton).clickAndHold().release().perform();
                    logger.info("Cabin number clicked");
                } catch (Exception e) {
                    logger.info("Cabin number click failed");
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", cabinNumberButton);
                }
                logger.info("Selected cabin number");
                clickNextButton();
            } catch (Exception e) {
                logger.info("Error: no element find " + e.getMessage());
                Assert.fail("No cabin numberfound");
            }
        }

    // Method to fill the passenger form
    public void passengerRegister () {
            try {
                logger.info("I'm inside testRegistroPasegero");
                Thread.sleep(500);
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#section--passengers")));
                String name1 = faker.name().firstName();
                String lastname1 = faker.name().lastName();
                String name2 = faker.name().firstName();
                String lastname2 = faker.name().lastName();
                String email1 = faker.internet().emailAddress();
                String phone1 = faker.phoneNumber().cellPhone();
                LocalDate birthPassenger1 = faker.date().birthday(20, 70).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                LocalDate birthPassenger2 = faker.date().birthday(20, 70).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                clickOutsidePopup();
                BasePage.selectDinningOptions();
                BasePage.fillPassengerForm(1, name1, lastname1, email1, phone1, birthPassenger1, true);
                BasePage.fillPassengerForm(2, name2, lastname2, "", "", birthPassenger2, false);
                BasePage.fillDocumentType(1);
                BasePage.fillDocumentType(2);
                BasePage.fillEmergencyContact();
                Thread.sleep(500);
                clickNextButton();
            } catch (Exception e) {
                logger.info("Error: no element find " + e.getMessage());
                e.printStackTrace();
                Assert.fail("Test failed due to an error");
            }
        }

    // Method to select the offers
    public void selectSpecialOffers () {
            try {
                logger.info("I'm inside testSpecialOffers");
                wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#pricing")));
                List<WebElement> specialOffersList = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector("div.hooper-list ul.hooper-track li")));
                if (specialOffersList.isEmpty()) {
                    logger.info("No special offers found, stopping test.");
                    Assert.fail("Error: No special offers available.");
                    return;
                }
                WebElement firstOffer = specialOffersList.get(0);
                try {
                    firstOffer.click();
                    logger.info("Selected first available special offer.");
                    WebElement increaseButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button.counter__button")));
                    increaseButton.click();
                    logger.info("Clicked on '+' button to increase quantity.");
                } catch (Exception e) {
                    logger.info("Failed to select special offer or click '+': " + e.getMessage());
                }
                clickNextButton();
            } catch (Exception e) {
                logger.info("Error in the special offer step: " + e.getMessage());
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
                            logger.info("Payment method selected");
                        } else {
                            logger.info("Payment option was already selected");
                        }
                    } catch (Exception e) {
                        logger.info("No payment option available or already selected");
                    }
                    clickNextButton();
                    try {
                        WebElement popupPayments = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#payment-method")));
                        if (popupPayments.isDisplayed()) {
                            logger.info("Payment method displayed.");
                            logger.info("The test was successfully concluded.");
                        }
                    } catch (Exception e) {
                        logger.info("Payment popup did not appear. Test might need revision.");
                    }
                }
            } catch (Exception e) {
                logger.info("Restart from the beginning... something went wrong.");
                e.printStackTrace();
            }
        }

    /*****AUXILIARY METHODS*****/

    //close popups methods
    private void closePopupIfExists (By popupLocator, By closeButtonLocator, String popupName){
            try {
                if (BasePage.isElementVisible(driver, popupLocator)) {
                    logger.info("Closing " + popupName + "...");
                    List<WebElement> closeButtons = driver.findElements(closeButtonLocator);
                    if (!closeButtons.isEmpty()) {
                        closeButtons.get(0).click();
                        logger.info(popupName + " closed using close button.");
                    } else {
                        logger.info(popupName + " has no close button, trying to click outside...");
                        clickOutsidePopup();
                    }
                    BasePage.waitForElementToDisappear(driver, popupLocator);
                    logger.info(popupName + " closed successfully.");
                } else {
                    logger.info(popupName + " not found or already closed.");
                }
            } catch (Exception e) {
                logger.info("Error closing " + popupName + ": " + e.getMessage());
            }
        }

    public void closeAdvancedFiltersDropdown () {
            try {
                logger.info("Inside closeAdvancedFiltersDropdown method");
                if (BasePage.isElementVisible(driver, advancedFiltersDropdownLocator)) {
                    logger.info("Advanced Filters dropdown detected, attempting to close it...");
                    WebElement advanceFilter = driver.findElement(advancedFiltersDropdownLocator);
                    BasePage.clickElement(driver, advanceFilter);
                    wait.until(ExpectedConditions.invisibilityOfElementLocated(advancedFiltersDropdownLocator));
                    logger.info("Advanced Filters dropdown closed successfully.");
                }
            } catch (Exception e) {
                logger.info("No advanced filters dropdown detected or error closing it: " + e.getMessage());
            }
        }

    public void closeWhatIsInclusive () {
            try {
                WebElement inclusivePopup = driver.findElement(By.cssSelector("div.modal-body"));

                if (inclusivePopup.isDisplayed()) {
                    logger.info("What is Inclusive detected, attempting to close it...");

                    // click
                    // actions.moveByOffset(10, 10).click().perform();
                    clickOutsidePopup();

                    // wait until what is inclusive popup isn't shown
                    wait.until(ExpectedConditions.invisibilityOf(inclusivePopup));

                    logger.info("What is inclusive popup closed successfully.");
                }
            } catch (Exception e) {
                logger.info("No what is inclusive popup detected or error closing it: " + e.getMessage());
            }
        }

    public void clickOutsidePopup () {
            try {
                Dimension windowSize = driver.manage().window().getSize();
                int offsetX = windowSize.width - 10;
                int offsetY = windowSize.height - 10;
                Actions actions = new Actions(driver);
                actions.moveByOffset(offsetX, offsetY).click().perform();
                logger.info("Clicked outside to close the popup.");
            } catch (Exception e) {
                logger.info("Failed to click outside the popup: " + e.getMessage());
            }
        }

    // Closing all popups if present
    private void closeAllPopups () {
            logger.info("Attempting to close all popups if present...");
            closePopupIfExists(By.cssSelector("#outerContainer > div.ui-dialog.insightera-dialog"), By.cssSelector("#Layer_1"), "Popup #outerContainer");
            closePopupIfExists(By.cssSelector("body > div:nth-child(1)"), By.cssSelector("body > div:nth-child(1)"), "Popup body > div:nth-child(1)");
            closePopupIfExists(dropdownContainer, departureDropdown, "Departure dropdown");
            closeAdvancedFiltersDropdown();
            clickOutsidePopup();
            logger.info("All necessary popups have been handled.");
        }

    // Method to manage opened dropdowns
    private void handleOpenedDropdown (WebDriver driver, By locator){
        boolean isDropdownVisible = BasePage.searchSelectorInDOM(locator);
        if (isDropdownVisible) {
            WebElement dropdownMenu = driver.findElement(locator);
            BasePage.scrollToElementSmoothly(driver, dropdownMenu);
            closePopupIfExists(locator, locator, "");
            BasePage.waitForElementToDisappear(driver, locator);
            logger.info("Dropdown closed successfully.");
        } else {
            logger.info("Dropdown was not visible or already closed.");
        }
    }

    public boolean verifyPassengerSection () {
            try {
                logger.info("Verifying if we reached the passengers section...");
                WebElement passengersSection = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#section--passengers")));
                if (passengersSection.isDisplayed()) {
                    logger.info("Successfully reached the passengers section.");
                    return true;
                } else {
                    logger.info("Failed to reach the passengers section.");
                    return false;
                }
            } catch (TimeoutException e) {
                logger.info("Timeout: Unable to find the passengers section within the expected time.");
                return false;
            } catch (Exception e) {
                logger.info("Error verifying the passengers section: " + e.getMessage());
                return false;
            }
        }

    public WebElement getNextButton () {
            return wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a[automation-id^='nav-action-next']")));

        }

    public void clickNextButton () {
            try {
                Thread.sleep(500);
                WebElement nextbutton = getNextButton();
                wait.until(ExpectedConditions.visibilityOf(nextbutton));
                wait.until(ExpectedConditions.elementToBeClickable(nextbutton));
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", nextbutton);
                Thread.sleep(500);
                try {
                    actions.moveToElement(nextbutton).clickAndHold().release().perform();
                    logger.info("Next button clicked");
                } catch (Exception e) {
                    logger.info("Click failed " + e.getMessage());
                    try {
                        actions.moveToElement(nextbutton).click().perform();
                        logger.info("Next button clicked normaly");
                    } catch (Exception e2) {
                        logger.info("Next button not clicked normaly");
                        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", nextbutton);
                    }
                }
                logger.info("Next button clicked successffully");
            } catch (Exception e) {
                logger.info("Error clicking Next button: " + e.getMessage());
                Assert.fail("Next button click failed.");
            }
        }

}
