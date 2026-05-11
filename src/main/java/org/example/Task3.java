package org.example;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;

public final class Task3 {
    private static final String FORECAST_URL = "https://api.open-meteo.com/v1/forecast?latitude=56&longitude=44&hourly=temperature_2m,rain&current=cloud_cover&timezone=Europe%2FMoscow&forecast_days=1&wind_speed_unit=ms";
    private static final String CHROME_DRIVER_PATH = "/Users/ivankarpich/Downloads/chromedriver-mac-arm64/chromedriver";

    private Task3() {
    }

    public static void writeForecastTable() {
        Path outputPath = Path.of("result", "forecast.txt");
        WebDriver webDriver = null;
        try {
            System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_PATH);
            webDriver = new ChromeDriver();
            webDriver.get(FORECAST_URL);

            String jsonText = webDriver.findElement(By.tagName("pre")).getText();
            String table = buildForecastTable(jsonText);

            System.out.print(table);
            writeTable(outputPath, table);
        } catch (Exception e) {
            System.out.println("Error");
            System.out.println(e);
        } finally {
            if (webDriver != null) {
                webDriver.quit();
            }
        }
    }

    private static String buildForecastTable(String jsonText) throws Exception {
        JSONObject root = (JSONObject) new JSONParser().parse(jsonText);
        JSONObject hourly = (JSONObject) root.get("hourly");

        JSONArray times = (JSONArray) hourly.get("time");
        JSONArray temperatures = (JSONArray) hourly.get("temperature_2m");
        JSONArray rains = (JSONArray) hourly.get("rain");

        StringBuilder builder = new StringBuilder();
        builder.append(String.format("| %-3s | %-16s | %-11s | %-12s |%n", "№", "Дата/время", "Температура", "Осадки (мм)"));
        builder.append(String.format("| %-3s | %-16s | %-11s | %-12s |%n", "--", "-------------", "-----------", "------------"));

        for (int index = 0; index < times.size(); index++) {
            String time = (String) times.get(index);
            double temperature = ((Number) temperatures.get(index)).doubleValue();
            double rain = ((Number) rains.get(index)).doubleValue();

            builder.append(String.format(Locale.US, "| %2d | %-16s | %11.1f | %12.2f |%n", index + 1, time, temperature, rain));
        }

        return builder.toString();
    }

    private static void writeTable(Path outputPath, String table) throws IOException {
        Path parent = outputPath.getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }

        Files.writeString(outputPath, table, StandardCharsets.UTF_8);
    }
}