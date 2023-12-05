package Cochez;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.*;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Login {
    private WebDriver driver;

    @BeforeClass
    public void setUp() {
        System.setProperty("webdriver.edge.driver", "C:\\Users\\Ricardo\\Desktop\\Selenium\\Prueba\\src\\main\\resources\\Driver\\msedgedriver.exe");
        driver = new EdgeDriver();
        driver.manage().window().maximize();
        driver.get("https://www.cochezycia.com/");
    }

    @DataProvider(name = "userData")
    public Object[][] userData() {
        return new Object[][] {
                {"usuario1", "contraseña1"},
                {"usuario2", "contraseña2"},
                {"usuario3", "contraseña3"}
        };
    }

    @Test(dataProvider = "userData")
    public void login(String username, String password) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        driver.findElement(By.xpath("(//i[@class='heigth-user fa fa-user icon-size'])[2]")).click();

        WebElement loginLink = driver.findElement(By.xpath("//span[@class='d-md-down-none'][contains(.,'Iniciar sesión')]"));
        loginLink.click();

        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0, 1000);");

        WebElement emailField = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@name='emailField']")));
        emailField.sendKeys(username);

        WebElement passwordField = driver.findElement(By.xpath("//input[contains(@name,'passwordField')]"));
        passwordField.sendKeys(password);

        WebElement loginButton = driver.findElement(By.xpath("//input[@value='Iniciar sesión']"));
        loginButton.click();

        wait.until(ExpectedConditions.titleContains("Inicio"));

        boolean success = isLoggedIn();
        Assert.assertTrue(success, "El usuario debería estar autenticado");

        generateExcelReport(username, success);
    }

    private boolean isLoggedIn() {

        try {
            WebElement welcomeMessage = driver.findElement(By.xpath("//div[@class='welcome-message']"));
            return welcomeMessage.isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    @AfterClass
    public void tearDown() {
        driver.quit();
    }

    @AfterMethod
    public void afterMethod(ITestResult result) {
        generateExcelReport(result.getMethod().getMethodName(), result.isSuccess());
    }

    private void generateExcelReport(String testName, boolean isSuccess) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Informe");
            Row headerRow = sheet.createRow(0);

            Cell headerCell1 = headerRow.createCell(0);
            headerCell1.setCellValue("Nombre de la prueba");

            Cell headerCell2 = headerRow.createCell(1);
            headerCell2.setCellValue("Resultado");

            Cell headerCell3 = headerRow.createCell(2);
            headerCell3.setCellValue("Fecha y Hora");

            Cell headerCell4 = headerRow.createCell(3);
            headerCell4.setCellValue("Navegador");

            Row resultRow = sheet.createRow(1);

            Cell resultCell1 = resultRow.createCell(0);
            resultCell1.setCellValue(testName);

            Cell resultCell2 = resultRow.createCell(1);
            resultCell2.setCellValue(isSuccess ? "Éxito" : "Fallo");

            Cell resultCell3 = resultRow.createCell(2);
            resultCell3.setCellValue(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

            Cell resultCell4 = resultRow.createCell(3);
            resultCell4.setCellValue("Edge"); // Puedes obtener el nombre del navegador desde WebDriver si lo necesitas

            try (FileOutputStream fileOut = new FileOutputStream("Informe-" + testName + ".xlsx")) {
                workbook.write(fileOut);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
