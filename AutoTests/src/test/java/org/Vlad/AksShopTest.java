package org.Vlad;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;

public class AksShopTest {

    private WebDriver chromeDriver;
    private static final String BASE_URL = "https://aks.ua";

    @BeforeClass(alwaysRun = true)
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "chromedriver-win64\\chromedriver.exe");
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--start-fullscreen");
        chromeOptions.setImplicitWaitTimeout(Duration.ofSeconds(15));
        this.chromeDriver = new ChromeDriver(chromeOptions);
    }

    @BeforeMethod
    public void preconditions() {
        chromeDriver.get(BASE_URL);
    }

    @AfterClass(alwaysRun = true)
    public void tearDown() {
        if (chromeDriver != null) {
            chromeDriver.quit();
        }
    }

    @Test
    public void testSearchProduct() {
        String searchQuery = "ноутбук";

        // 1. Непрямий XPath
        WebElement searchInput = chromeDriver.findElement(
                By.xpath("//form[@id='search-form']//input[@id='search-input']")
        );

        // 2. Перевірка наявності поля пошуку
        Assert.assertNotNull(searchInput, "Поле пошуку має бути");

        // 3. Клік і ввід даних
        searchInput.click();
        searchInput.sendKeys(searchQuery);

        // 4. Непрямий XPath — кнопка пошуку
        WebElement searchButton = chromeDriver.findElement(
                By.xpath("//form[@id='search-form']//button[@class='search-button']")
        );
        searchButton.click();

        // 5. Перевірка що URL містить 'search'
        String currentUrl = chromeDriver.getCurrentUrl();
        Assert.assertTrue(
                currentUrl.contains("search"),
                "URL має містити 'search' після пошуку"
        );

        // 6. Непрямий XPath — картки товарів всередині контейнера результатів
        List<WebElement> productCards = chromeDriver.findElements(
                By.xpath("//div[contains(@class,'catalog-search')]//div[@class='catalog-item']")
        );

        // 7. Перевірка умови — знайдено хоча б один товар
        Assert.assertTrue(
                productCards.size() > 0,
                "Результати пошуку повинні мати хоча б один товар, знайдено: " + productCards.size()
        );

        // 8. Перевірка наявності даних - назва першого товару не порожня
        WebElement firstProductName = productCards.get(0).findElement(
                By.xpath(".//div[@class='catalog-name']//a")
        );
        String productName = firstProductName.getText();
        Assert.assertFalse(
                productName.isEmpty(),
                "Назва першого товару не повинна бути порожньою"
        );
    }
}