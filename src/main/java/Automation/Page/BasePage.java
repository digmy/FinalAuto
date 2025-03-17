package Automation.Page;

import Automation.TestConfig.DriverTestBase;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import com.github.javafaker.Faker;


import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.time.ZoneId;

import static java.lang.System.out;

public class BasePage {
    protected static WebDriver driver = DriverTestBase.getDriver();
    protected static WebDriverWait wait = DriverTestBase.getWait();
    protected static Actions actions = new Actions(driver);
    protected static JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
    private static final Faker faker = new Faker();
    private static final int timeout = 20;

    public static WebDriverWait getWait() {
        if (wait == null) {
            wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
        }
        return wait;
    }

    public static Faker getFaker() {
        return faker;
    }

    public static void openSite(String url) {
        driver.get(url);
        try {
            WebElement acceptCookies = wait.until(ExpectedConditions.elementToBeClickable(By.id("didomi-notice-agree-button")));
            acceptCookies.click();
        } catch (Exception e) {
            System.out.println("Cookie banner isn't present: " + e.getMessage());
        }
    }

    public static boolean waitForElementToDisappear(WebDriver driver, By locator) {
        try {
            return wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
        } catch (TimeoutException e) {
            out.println("Element still visible: " + locator);
            return false;
        }
    }

    public static List<WebElement> waitForElementsPresentAndVisible(WebDriver driver, By locator) {
        try {
            WebDriverWait wait = getWait();

            List<WebElement> elements = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));

            return wait.until(ExpectedConditions.visibilityOfAllElements(elements));
        } catch (TimeoutException e) {
            out.println("Timeout: Los elementos no se encontraron o no se hicieron visibles a tiempo.");
            return new ArrayList<>();
        } catch (Exception e) {
            out.println("Error al esperar los elementos: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public static WebElement waitForElementToBeClickable(WebDriver driver, WebElement element) {
        return new WebDriverWait(driver, Duration.ofSeconds(timeout))
                .until(ExpectedConditions.elementToBeClickable(element));
    }

    public static WebElement waitForElementToBeClickableLocator(WebDriver driver, By locator) {
        return new WebDriverWait(driver, Duration.ofSeconds(timeout)).until(ExpectedConditions.elementToBeClickable(locator));
    }

    public static void clickWithActions(WebDriver driver, WebElement element) {
        try {
            actions.moveToElement(element).click().perform();
        } catch (Exception e) {
            clickWithJS(driver, element);
        }
    }

    public static void clickWithJS(WebDriver driver, WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }

    public static void clickElement(WebDriver driver, WebElement element) {
        try {
            new Actions(driver).moveToElement(element).click().perform();
            out.println("Clicked on the element.");
        } catch (Exception e) {
            try {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
                out.println("Clicked on the element using JavaScript.");
            } catch (Exception jsException) {
                out.println("Failed to click on the element: " + jsException.getMessage());
                Assert.fail("Failed to click on the element.");
            }
        }
    }

    public static void scrollToElementSmoothly(WebDriver driver, WebElement element) {
        try {
            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element
            );
            Thread.sleep(300);
        } catch (Exception e) {
            out.println("Error scrolling to element: " + e.getMessage());
        }
    }

    public static boolean isElementVisible(WebDriver driver, By locator) {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }

    public static WebElement findElement(WebDriver driver, By locator) {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            WebElement element = driver.findElement(locator);
            if (element != null) {
                return element;
            } else {
                out.println("Element not found for the locator: " + locator);
                return null;
            }
        } catch (Exception e) {
            out.println("Error finding element: " + e.getMessage());
            return null;
        }
    }

    public static boolean searchSelectorInDOM(By locator) {
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(locator));
            JavascriptExecutor js = (JavascriptExecutor) driver;
            String selector = convertSelectorToString(locator);
            String script = "var element = document.querySelector(arguments[0]);"
                    + "if (element) {"
                    + "  element.scrollIntoView({behavior: 'smooth', block: 'center', inline: 'center'});"
                    + "  return (element.offsetWidth > 0 && element.offsetHeight > 0 && window.getComputedStyle(element).visibility !== 'hidden' && !element.disabled);"
                    + "} else {"
                    + "  return false; "
                    + "}";
            return (Boolean) js.executeScript(script, selector);
        } catch (Exception e) {
            out.println("Error in searchSelectorInDOM: " + e.getMessage());
            return false;
        }
    }

    public static String convertSelectorToString(By selector) {
        String selectorStr = selector.toString();
        if (selectorStr.startsWith("By.cssSelector: ")) {
            return selectorStr.replace("By.cssSelector: ", "").trim();
        } else if (selectorStr.startsWith("By.xpath: ")) {
            return selectorStr.replace("By.xpath: ", "").trim();
        }
        return selectorStr;
    }

    private static WebElement waitForElement(By locator) {
        WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        jsExecutor.executeScript("arguments[0].scrollIntoView(true);", element);
        return element;
    }

    private static void selectByIndex(WebElement element, int index) {
        Select select = new Select(element);
        select.selectByIndex(index);
    }

    public static void selectDinningOptions() {
        try {
            List<WebElement> dinningElements = driver.findElements(
                    By.cssSelector("#ddl_73c65394-222b-b842-75c2-6c814d7cd934 select[autocomplete='on']")
            );
            if (dinningElements.isEmpty()) {
                out.println("Dining option is not available. Proceeding with passenger registration.");
                return;
            }
            WebElement dinningDropdown = dinningElements.get(0);
            Select selectOne = new Select(dinningDropdown);
            if (selectOne.getOptions().size() > 1) {
                out.println("There are at least 2 options available for dinner");
                selectOne.selectByIndex(1);
                out.println("Dinner option selected");
            } else {
                out.println("Only one dining option available, proceeding with normal flow.");
            }

        } catch (Exception e) {
            out.println("Error handling dining options: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void fillPassengerForm(int passengerNumber, String firstName, String lastName, String email, String phone, LocalDate birthDate, boolean isFirstPassenger) {
        try {
            fillNameFields(passengerNumber, firstName, lastName);
            if (isFirstPassenger) {
                fillContactDetails(passengerNumber, email, phone);
            } else {
                selectCountryOfResidence(passengerNumber);
            }
            fillBirthDate(passengerNumber, birthDate);
            selectGender(passengerNumber);
        } catch (Exception e) {
            System.out.println("Error filling passenger " + passengerNumber + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void fillNameFields(int passengerNumber, String firstName, String lastName) {
        WebElement nameField = waitForElement(By.cssSelector("#FirstName_" + passengerNumber + "_1"));
        nameField.sendKeys(firstName);

        WebElement lastNameField = waitForElement(By.cssSelector("#LastName_" + passengerNumber + "_1"));
        lastNameField.sendKeys(lastName);
    }

    private static void fillContactDetails(int passengerNumber, String email, String phone) {
        WebElement emailField = waitForElement(By.cssSelector("#Email__" + passengerNumber + "_1"));
        emailField.sendKeys(email);

        WebElement confirmEmailField = waitForElement(By.cssSelector("#ConfirmEmail_" + passengerNumber + "_1"));
        confirmEmailField.sendKeys(email);

        WebElement prefixDropdown = waitForElement(By.cssSelector("#ddl_f638e776-0812-945d-ce60-4f87c1ae7d63"));
        selectByIndex(prefixDropdown, 109);

        WebElement phoneField = waitForElement(By.cssSelector("#PhoneNumber_" + passengerNumber + "_1"));
        phoneField.sendKeys(phone);
    }

    private static void selectCountryOfResidence(int passengerNumber) {
        WebElement countryDropdown = waitForElement(By.cssSelector("#CountryOfResidence_" + passengerNumber + "_1"));
        selectByIndex(countryDropdown, 1);
    }

    private static void fillBirthDate(int passengerNumber, LocalDate birthDate) {
        try {
            WebElement birthField = waitForElement(By.id("DateOfBirth_" + passengerNumber + "_1"));
            String formattedDate = birthDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            birthField.clear();
            Thread.sleep(500);
            birthField.sendKeys(formattedDate);
        } catch (Exception e) {
            System.out.println("Error filling birth date for passenger " + passengerNumber + ": " + e.getMessage());
            e.printStackTrace();
        }
        /*static LocalDate birthGenerator() {
        long startEpochDay = LocalDate.of(1950, 1, 1).toEpochDay();
        long endEpochDay = LocalDate.of(2000, 12, 31).toEpochDay();
        long randomDay = startEpochDay + Math.abs(random.nextLong()) % (endEpochDay - startEpochDay);
        return LocalDate.ofEpochDay(randomDay);
    }*/
    }

    private static void selectGender(int passengerNumber) {
        WebElement genderDropdown = waitForElement(By.cssSelector("#Gender_" + passengerNumber + "_1"));
        selectByIndex(genderDropdown, 0);
    }

    public static void fillDocumentType(int passengerNumber) {
        try {
            WebElement documentTypeDropdown = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#DocumentType_" + passengerNumber + "_1")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", documentTypeDropdown);
            Select documentTypeSelect = new Select(documentTypeDropdown);
            documentTypeSelect.selectByValue("pas");
            fillPasaportFields(passengerNumber);
        } catch (Exception e) {
            out.println("Error filling document type for passenger " + passengerNumber + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void fillPasaportFields(int passengerNumber) {
        try {
            WebElement documentNumberField = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#DocumentNumber_" + passengerNumber + "_1")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", documentNumberField);
            documentNumberField.sendKeys(faker.number().digits(8));

            WebElement issuingCountryDropdown = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#IssuingCountry_" + passengerNumber + "_1")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", issuingCountryDropdown);
            Select issuingCountrySelect = new Select(issuingCountryDropdown);
            issuingCountrySelect.selectByVisibleText(faker.country().name());

            WebElement issueDateField = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#IssueDate_" + passengerNumber + "_1")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", issueDateField);
            LocalDate issueDate = faker.date().birthday(20, 70).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            issueDateField.sendKeys(issueDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

            WebElement expirationDateField = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#ExpirationDate_" + passengerNumber + "_1")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", expirationDateField);
            LocalDate expirationDate = issueDate.plusYears(10);
            expirationDateField.sendKeys(expirationDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

            WebElement nationalityDropdown = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#Nationality_" + passengerNumber + "_1")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", nationalityDropdown);
            Select nationalitySelect = new Select(nationalityDropdown);
            nationalitySelect.selectByVisibleText(faker.nation().nationality());
        } catch (Exception e) {
            out.println("Error filling Passaporto fields for passenger " + passengerNumber + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void fillEmergencyContact() {
        try {
            WebElement contactNameField = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#EmergencyContact_1")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", contactNameField);
            contactNameField.sendKeys(faker.name().fullName());

            WebElement phoneField = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#EmergencyPhone_1")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", phoneField);
            phoneField.sendKeys(faker.phoneNumber().cellPhone());
        } catch (Exception e) {
            out.println("Error filling emergency contact: " + e.getMessage());
            e.printStackTrace();
        }
    }

}

