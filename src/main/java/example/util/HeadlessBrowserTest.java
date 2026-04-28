package example.util;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import java.time.Duration;

public class HeadlessBrowserTest {
    public static void main(String[] args) {
        // 设置ChromeDriver路径（根据您的实际路径修改）
        System.setProperty("webdriver.chrome.driver", "D:\\chromedriver\\chromedriver.exe");

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless"); // 无头模式，不显示浏览器窗口
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");

        WebDriver driver = new ChromeDriver(options);
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(10));

        String url = "https://share.eyesnews.cn/news/news-news_detail-news_id-11515117150138.html";

        for (int i = 1; i <= 200; i++) {
            try {
                driver.get(url);
                // 等待JS统计代码执行完成（5秒足够埋点上报）
               // Thread.sleep(5000);
                System.out.println("第 " + i + " 次访问完成");
            } catch (Exception e) {
                System.err.println("第 " + i + " 次访问失败: " + e.getMessage());
            }
        }

        driver.quit();
        System.out.println("测试完成");
    }
}
