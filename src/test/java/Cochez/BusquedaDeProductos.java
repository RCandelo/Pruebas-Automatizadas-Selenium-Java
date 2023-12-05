package Cochez;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class BusquedaDeProductos {
    private WebDriver driver;
    private int totalProductosEncontrados = 0;
    private int totalBusquedasRealizadas = 0;

    @BeforeClass
    public void setUp() {
        System.setProperty("webdriver.edge.driver", "C:\\Users\\Ricardo\\Desktop\\Selenium\\Prueba\\src\\main\\resources\\Driver\\msedgedriver.exe");
        driver = new EdgeDriver();
        driver.manage().window().maximize();
        driver.get("https://www.cochezycia.com/");
    }

    @DataProvider(name = "searchData")
    public Object[][] searchData() {
        return new Object[][]{
                {"cama"},
                {"Lampara"}
        };
    }

    @Test(dataProvider = "searchData")
    public void testBusquedaDeProductos(String elemento) {
        totalBusquedasRealizadas++;

        WebElement searchbox = driver.findElement(By.cssSelector("input.form-control.search_input.cc_search_input"));
        searchbox.clear();
        searchbox.sendKeys(elemento);
        driver.findElement(By.xpath("//button[@class=' search_button']")).click();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);


        List<WebElement> productElements = driver.findElements(By.xpath("//div[@class='hawk-item-list__item box-shadow-default box-radius-default cc_product_item cc_grid_item p-4']"));
        int numberOfProducts = productElements.size();
        totalProductosEncontrados += numberOfProducts;
        System.out.println("Número de productos encontrados para '" + elemento + "': " + numberOfProducts);
        System.out.println("Total acumulado de productos encontrados: " + totalProductosEncontrados);
        Assert.assertTrue(numberOfProducts > 0, "No se encontraron productos para '" + elemento + "'");
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

            Cell headerCell5 = headerRow.createCell(4);
            headerCell5.setCellValue("Búsquedas Realizadas");

            Cell headerCell6 = headerRow.createCell(5);
            headerCell6.setCellValue("Total Productos Encontrados");

            Row resultRow = sheet.createRow(1);

            Cell resultCell1 = resultRow.createCell(0);
            resultCell1.setCellValue(testName);

            Cell resultCell2 = resultRow.createCell(1);
            resultCell2.setCellValue(isSuccess ? "Éxito" : "Fallo");

            Cell resultCell3 = resultRow.createCell(2);
            resultCell3.setCellValue(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

            Cell resultCell4 = resultRow.createCell(3);
            resultCell4.setCellValue("Edge"); // Puedes obtener el nombre del navegador desde WebDriver si lo necesitas

            Cell resultCell5 = resultRow.createCell(4);
            resultCell5.setCellValue(totalBusquedasRealizadas);

            Cell resultCell6 = resultRow.createCell(5);
            resultCell6.setCellValue(totalProductosEncontrados);

            try (FileOutputStream fileOut = new FileOutputStream("Informe-" + testName + ".xlsx")) {
                workbook.write(fileOut);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
