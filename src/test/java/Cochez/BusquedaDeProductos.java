package Cochez;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertTrue;

public class BusquedaDeProductos {
    private WebDriver driver;

    @Before
    public void setUp() {
        System.setProperty("webdriver.edge.driver", "C:\\Users\\Ricardo\\Desktop\\Selenium\\Prueba\\src\\main\\resources\\Driver\\msedgedriver.exe");
        driver = new EdgeDriver();
        driver.manage().window().maximize();
        driver.get("https://www.cochezycia.com/");
    }

    @Test
    public void testBusquedaDeProductos() {
        String[] elementosABuscar = {"cama", "Lampara"};

        for (String elemento : elementosABuscar) {
            WebElement searchbox = driver.findElement(By.cssSelector("input.form-control.search_input.cc_search_input"));
            searchbox.clear();
            searchbox.sendKeys(elemento);
            driver.findElement(By.xpath("//button[@class=' search_button']")).click();
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

            // Check if the search result contains the searched product
            assertTrue(driver.getPageSource().contains(elemento));
        }

        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0, 1000);");
    }

    @After
    public void tearDown() {
        driver.quit();
    }
}
