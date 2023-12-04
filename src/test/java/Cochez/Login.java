package Cochez;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertTrue;

public class Login{
    private WebDriver driver;

    @Before
    public void setUp() {
        System.setProperty("webdriver.edge.driver", "C:\\Users\\Ricardo\\Desktop\\Selenium\\Prueba\\src\\main\\resources\\Driver\\msedgedriver.exe");
        driver = new EdgeDriver();
        driver.manage().window().maximize();
        driver.get("https://www.cochezycia.com/");
    }

    @Test
    public void login() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        driver.findElement(By.xpath("(//i[@class='heigth-user fa fa-user icon-size'])[2]")).click();

        WebElement loginLink = driver.findElement(By.xpath("//span[@class='d-md-down-none'][contains(.,'Iniciar sesión')]"));
        loginLink.click();

        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0, 1000);");

        WebElement emailField = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@name='emailField']")));
        emailField.sendKeys("USUARIO");

        WebElement passwordField = driver.findElement(By.xpath("//input[contains(@name,'passwordField')]"));
        passwordField.sendKeys("CONTRASEÑA");

        WebElement loginButton = driver.findElement(By.xpath("//input[@value='Iniciar sesión']"));
        loginButton.click();

        // Esperar a que la página de inicio de sesión se cargue completamente
        wait.until(ExpectedConditions.titleContains("Inicio"));

        assertTrue("El usuario debería estar autenticado", isLoggedIn());
    }

    private boolean isLoggedIn() {
        // Verificar si existe un elemento que indica que el usuario está autenticado
        try {
            WebElement welcomeMessage = driver.findElement(By.xpath("//div[@class='welcome-message']"));
            return welcomeMessage.isDisplayed();
        } catch (NoSuchElementException e) {
            return false; // Elemento no encontrado, el usuario no está autenticado
        }
    }


    @After
    public void tearDown() {
        driver.quit();
    }
}
