package Cochez;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.testng.ITestResult;
import org.testng.annotations.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;


public class TiempoCarga {

    private WebDriver driver;

    @BeforeClass
    public void setUp() {
        System.setProperty("webdriver.edge.driver", "C:\\Users\\Ricardo\\Desktop\\Selenium\\Prueba\\src\\main\\resources\\Driver\\msedgedriver.exe");
        driver = new EdgeDriver();
        driver.manage().window().maximize();
        driver.get("https://www.cochezycia.com/");
    }

    @Test
    public void TiempodeCarga() {
        int numUsers = 5;

        for (int i = 0; i < numUsers; i++) {
            long startTime = System.currentTimeMillis();

            driver.get("https://www.cochezycia.com/");

            long endTime = System.currentTimeMillis();

            long loadTime = endTime - startTime;
            System.out.println("Tiempo de carga para el usuario " + (i + 1) + ": " + loadTime + " milisegundos");

            generateExcelReport("Tiempo de carga para el usuario " + (i + 1), loadTime);
        }
    }

    private void generateExcelReport(String testName, long loadTime) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Informe");
            Row headerRow = sheet.createRow(0);

            Cell headerCell1 = headerRow.createCell(0);
            headerCell1.setCellValue("Nombre de la prueba");

            Cell headerCell2 = headerRow.createCell(1);
            headerCell2.setCellValue("Tiempo de carga (milisegundos)");

            Row resultRow = sheet.createRow(1);

            Cell resultCell1 = resultRow.createCell(0);
            resultCell1.setCellValue(testName);

            Cell resultCell2 = resultRow.createCell(1);
            resultCell2.setCellValue(loadTime);

            try (FileOutputStream fileOut = new FileOutputStream("Informe-" + testName + ".xlsx")) {
                workbook.write(fileOut);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @AfterClass
    public void tearDown() {
        driver.quit();
    }

    @AfterMethod
    public void afterMethod(ITestResult result) {
    }
}
