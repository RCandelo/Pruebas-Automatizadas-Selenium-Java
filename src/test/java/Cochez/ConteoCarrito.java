package Cochez;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.Assert.assertTrue;

public class ConteoCarrito {
    private WebDriver driver;
    private int itemCount = 0; // Agregar la variable itemCount aquí

    @Before
    public void setUp() {
        System.setProperty("webdriver.edge.driver", "C:\\Users\\Ricardo\\Desktop\\Selenium\\Prueba\\src\\main\\resources\\Driver\\msedgedriver.exe");
        driver = new EdgeDriver();
        driver.manage().window().maximize();
        driver.get("https://www.cochezycia.com/");
    }

    @Test
    public void testConteoCarrito() {
        String[] elementosABuscar = {"cama", "Lampara"};

        for (String elemento : elementosABuscar) {
            WebElement searchbox = driver.findElement(By.cssSelector("input.form-control.search_input.cc_search_input"));
            searchbox.clear();
            searchbox.sendKeys(elemento);
            driver.findElement(By.xpath("//button[@class=' search_button']")).click();

            // Check if the search result contains the searched product
            assertTrue(driver.getPageSource().contains(elemento));

            itemCount += agregarProductoAlCarrito(); // Actualizar itemCount con el resultado del método
        }

        System.out.println("Total items in the cart: " + itemCount);

        // Optionally, perform additional assertions based on the total item count.
    }

    private int agregarProductoAlCarrito() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//span[@class='hawk-cc-addtocart-btn__text'][contains(.,'Agregar al carrito')])[1]")));

        return 1;
    }

    @After
    public void tearDown() {
        driver.quit();
    }
}
