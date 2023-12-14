package example.service;

import cn.hutool.core.util.StrUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebCrawler {

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        String url = "https://www.eastmoney.com/"; // 替换成你要爬取的网页地址
        Class.forName("com.mysql.jdbc.Driver");
        String sql = "INSERT INTO log_dongfang (url,title,content) VALUES ";
           String databaseUrl  = "jdbc:mysql://localhost:3306/public_work";
        //"jdbc:mysql://localhost:3306/public_work"; // 替换成你的数据库信息
          String username = "root"; // 替换成你的数据库用户名
          String password = "123456"; // 替换成你的数据库密码

        Connection conn = DriverManager.getConnection(databaseUrl, username, password);

        try {
            Document doc = Jsoup.connect(url).get();
            // 获取页面标题
            String title = doc.title();
            System.out.println("Title: " + title);
            String text = doc.text();
            System.out.println("Text Content: " + text);
            // 获取所有链接
            List<String> values = new ArrayList<String>();
            Elements links = doc.select("a[href]");
            for (Element link : links) {
                String linkHref = link.attr("href");
                String linkText = link.text();
                System.out.println("Link Text: " + linkText + ", Link URL: " + linkHref);
                if (isValidURL(linkHref)) {
                    values.add(readAll(linkHref));
                }
                if (values.size()>30){
                    sql= sql+ StrUtil.join(",",values);
                    insertData(sql,conn);
                    values=new ArrayList<String>();
                }
            }

//            // 获取特定 CSS 类的元素
//            Elements elements = doc.select(".classname");
//            for (Element element : elements) {
//                System.out.println("Element: " + element.text());
//            }
//            Elements dataDivs = doc.select("div.data-container");
//
//            // 遍历每个 div 元素，提取数据
//            for (Element div : dataDivs) {
//                // 提取 div 元素中的文本内容
//                String textDiv = div.text();
//                System.out.println("Data: " + textDiv);
//            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static String readAll(String url){
        try {
            Document doc = Jsoup.connect(url).get();
            // 获取页面标题
            String title = doc.title();
            System.out.println("Title: " + title);
            String text = doc.text();
            System.out.println("Text Content: " + text);
            StringBuilder sb = new StringBuilder();
            sb.append("('").append(url).append("','").append(title).append("','").append(text).append("')");
            return sb.toString();
//            // 获取所有链接
//            Elements links = doc.select("a[href]");
//            for (Element link : links) {
//                String linkHref = link.attr("href");
//                String linkText = link.text();
//                System.out.println("Link Text: " + linkText + ", Link URL: " + linkHref);
//            }
//
//            // 获取特定 CSS 类的元素
//            Elements elements = doc.select(".classname");
//            for (Element element : elements) {
//                System.out.println("Element: " + element.text());
//            }
//            Elements dataDivs = doc.select("div.data-container");
//
//            // 遍历每个 div 元素，提取数据
//            for (Element div : dataDivs) {
//                // 提取 div 元素中的文本内容
//                String textDiv = div.text();
//                System.out.println("Data: " + textDiv);
//            }
        }catch (IOException e) {
            return "('123','123','12414')";
        }
    }
    public static boolean isValidURL(String url) {
        String regex = "^(http|https)://.*$"; // 简单的正则表达式，检查是否以 http:// 或 https:// 开头
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(url);
        return matcher.matches();
    }

    private static void insertData(String sql,Connection conn){
        try  {
            // 要执行的 SQL 语句
             // 替换成你的表格和列名
            // 创建 PreparedStatement 对象
            PreparedStatement pstmt = conn.prepareStatement(sql);
             // 设置第一个参数的值为要插入的数据
            // 执行 SQL 语句
            pstmt.executeUpdate();
            System.out.println("Data inserted successfully!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
