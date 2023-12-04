package Cochez;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
public class TiempoCarga {

    private WebDriver driver;

    @Before
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

            // Cerrar el navegador después de cada prueba
            driver.quit();
            // Volver a crear una nueva instancia para la próxima iteración
            driver = new EdgeDriver();
            driver.manage().window().maximize();
        }
    }

    @After
    public void tearDown() {
        driver.quit();
    }
}
