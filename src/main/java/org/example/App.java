package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class App {
    private static final String PASSWORD_GENERATOR_URL = "https://www.calculator.net/password-generator.html";
    private static final String CHROME_DRIVER_PATH = "/Users/ivankarpich/Downloads/chromedriver-mac-arm64/chromedriver";
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(
            "(?s).*?([A-Za-z0-9!\"#$%&'()*+,\\-./:;<=>?@\\\\\\[\\\\\\]^_`{|}~]{8,})Password Strength:.*"
    );

    public static void main(String[] args) {
        printGeneratedPassword();
        Task2.printClientIpAddress();
        Task3.writeForecastTable();
    }

    private static void printGeneratedPassword() {
        WebDriver webDriver = null;
        try {
            System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_PATH);
            webDriver = new ChromeDriver();
            webDriver.get(PASSWORD_GENERATOR_URL);

            String pageText = webDriver.findElement(By.tagName("body")).getText();
            String password = extractPassword(pageText);
            System.out.println("Сгенерированный пароль: " + password);
        } catch (Exception e) {
            System.out.println("Error");
            System.out.println(e);
        } finally {
            if (webDriver != null) {
                webDriver.quit();
            }
        }
    }

    private static String extractPassword(String pageText) {
        Matcher matcher = PASSWORD_PATTERN.matcher(pageText);
        if (matcher.matches()) {
            return matcher.group(1);
        }

        throw new IllegalStateException("Не удалось извлечь пароль со страницы генератора.");
    }
}