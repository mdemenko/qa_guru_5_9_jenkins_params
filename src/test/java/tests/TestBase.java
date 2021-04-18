package tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import config.DriverConfig;
import io.qameta.allure.selenide.AllureSelenide;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.openqa.selenium.remote.DesiredCapabilities;

import static com.codeborne.selenide.Selenide.closeWebDriver;
import static helpers.AttachmentHelper.*;

public class TestBase {
    static DriverConfig driverConfig = ConfigFactory.create(DriverConfig.class);

    @BeforeAll
    static void setup() {
        SelenideLogger.addListener("allure", new AllureSelenide());
        Configuration.startMaximized = true;

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("enableVNC", true);
        capabilities.setCapability("enableVideo", true);
        Configuration.browserCapabilities = capabilities;

        //        gradle clean test -Dweb.browser=opera
        Configuration.browser = System.getProperty("web.browser", "chrome");

        String remoteWebDriver = System.getProperty("remote.web.driver");

        if (remoteWebDriver != null) {
            String user = driverConfig.remoteWebUser();
            String password = driverConfig.remoteWebPassword();
            Configuration.remote = String.format(remoteWebDriver, user, password);
        }
    }

    @AfterEach
    void afterEach() {
        attachScreenshot("Last screenshot");
        attachPageSource();
        attachAsText("Browser console logs", getConsoleLogs());

        if(System.getProperty("video.storage") != null)
                attachVideo();
            closeWebDriver();
    }
}
