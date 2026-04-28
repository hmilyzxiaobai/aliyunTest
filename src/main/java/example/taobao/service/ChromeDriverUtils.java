//package example.taobao.service;
//import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.chrome.ChromeDriver;
//import org.openqa.selenium.chrome.ChromeOptions;
//import org.openqa.selenium.devtools.DevTools;
//import org.openqa.selenium.devtools.v85.network.Network;
//
//import java.util.*;
//
//public class ChromeDriverUtils {
//
//    static WebDriver driver;
//    public static void driver() {
//// 设置 ChromeDriver 路径
//        System.setProperty("webdriver.chrome.driver", "D:\\Chromedriver_win32\\chromedriver.exe");
//
//// 创建 ChromeOptions 实例并设置 User-Agent
//        ChromeOptions options = new ChromeOptions();
//        options.setBinary("C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe");
//
//// 创建 ChromeDriver 并传入 ChromeOptions
//        driver = new ChromeDriver(options);
//
//// 启用 Chrome DevTools
//        DevTools devTools = ((ChromeDriver) driver).getDevTools();
//        devTools.createSession();
//
//        List<String> requests = new ArrayList<>();
//        List<String> responses = new ArrayList<>();
//
//        devTools.addListener(Network.requestWillBeSent(), request -> {
//            String requestType = String.valueOf(request.getType());
//            if (requestType.equals("Fetch") || requestType.equals("XHR")) {
//                requests.add(request.getRequest().getUrl());
//            }
//        });
//
//        devTools.addListener(Network.responseReceived(), response -> {
//            String requestType = String.valueOf(response.getType());
//            if (requestType.equals("Fetch") || requestType.equals("XHR")) {
//                responses.add(response.getResponse().getUrl());
//            }
//        });
//
//// 启用监听器
//        devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));
//
//// 访问目标网页
//        driver.get("https://www.taobao.com/");
//
//// 关闭浏览器
//
//    }
//
//    public void quit(){
//        driver.quit();
//
////        System.err.println(responses.size() + "----------分割线----------");
////        requests.forEach(System.out::println);
////        System.err.println(responses.size() + "----------分割线----------");
////        responses.forEach(System.out::println);
//    }
//}