package org.example;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public final class Task2 {
    private static final String IPIFY_URL = "https://api.ipify.org/?format=json";
    private static final String CHROME_DRIVER_PATH = "/Users/ivankarpich/Downloads/chromedriver-mac-arm64/chromedriver";

    private Task2() {
    }

    public static void printClientIpAddress() {
        WebDriver webDriver = null;
        try {
            System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_PATH);
            webDriver = new ChromeDriver();
            webDriver.get(IPIFY_URL);

            String jsonText = webDriver.findElement(By.tagName("pre")).getText();
            JSONObject jsonObject = (JSONObject) new JSONParser().parse(jsonText);
            String ip = (String) jsonObject.get("ip");
            System.out.println("IP4-адрес клиента: " + ip);
        } catch (Exception e) {
            System.out.println("Error");
            System.out.println(e);
        } finally {
            if (webDriver != null) {
                webDriver.quit();
            }
        }
    }
}