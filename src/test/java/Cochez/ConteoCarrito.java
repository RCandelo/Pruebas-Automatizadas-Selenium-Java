package Cochez;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
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

public class ConteoCarrito {
    private WebDriver driver;
    private int itemCount = 0;

    @BeforeClass
    public void setUp() {
        System.setProperty("webdriver.edge.driver", "C:\\Users\\Ricardo\\Desktop\\Selenium\\Prueba\\src\\main\\resources\\Driver\\msedgedriver.exe");
        driver = new EdgeDriver();
        driver.manage().window().maximize();
        driver.get("https://www.cochezycia.com/");
    }

    @DataProvider(name = "searchData")
    public Object[][] searchData() {
        return new Object[][] {
                {"cama"},
                {"Lampara"}
        };
    }

    @Test(dataProvider = "searchData")
    public void testConteoCarrito(String elemento) {
        WebElement searchbox = driver.findElement(By.cssSelector("input.form-control.search_input.cc_search_input"));
        searchbox.clear();
        searchbox.sendKeys(elemento);
        driver.findElement(By.xpath("//button[@class=' search_button']")).click();

        Assert.assertTrue(driver.getPageSource().contains(elemento));

        itemCount += agregarProductoAlCarrito();
    }

    private int agregarProductoAlCarrito() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//span[@class='hawk-cc-addtocart-btn__text'][contains(.,'Agregar al carrito')])[1]")));

        return 1;
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
            resultCell4.setCellValue("Edge");

            try (FileOutputStream fileOut = new FileOutputStream("Informe-" + testName + ".xlsx")) {
                workbook.write(fileOut);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
