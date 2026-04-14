package example.dongfangcaifu.news;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import example.dongfangcaifu.enums.NewEnums;
import example.dongfangcaifu.news.interfaceFile.NewInterFace;
import example.dongfangcaifu.service.NewUrlService;
import example.dongfangcaifu.src.entity.NewUrlEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Service
public class MIITNews implements NewInterFace {

    public void readALl(NewUrlService newUrlService) throws Exception{
        URL url = new URL(
                NewEnums.MIIT.getUrl()
        );

        // 打开连接
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        // 设置请求方法为GET
        connection.setRequestMethod("GET");

        // 设置请求头
        connection.setRequestProperty("Accept", "*/*");
        connection.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.9");
        connection.setRequestProperty("Connection", "keep-alive");
        connection.setRequestProperty("Cookie", "__jsluid_s=5ea40abf5832fd8cfe0516c6e8262718");
        connection.setRequestProperty("Referer", "https://www.miit.gov.cn/xwfb/gxdt/index.html");
        connection.setRequestProperty("Sec-Fetch-Dest", "empty");
        connection.setRequestProperty("Sec-Fetch-Mode", "cors");
        connection.setRequestProperty("Sec-Fetch-Site", "same-origin");
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/130.0.0.0 Safari/537.36");
        connection.setRequestProperty("X-Requested-With", "XMLHttpRequest");
        connection.setRequestProperty("sec-ch-ua", "\"Chromium\";v=\"130\", \"Google Chrome\";v=\"130\", \"Not?A_Brand\";v=\"99\"");
        connection.setRequestProperty("sec-ch-ua-mobile", "?0");
        connection.setRequestProperty("sec-ch-ua-platform", "\"Windows\"");

        // 允许输入输出
        connection.setDoInput(true);
        connection.setDoOutput(false); // 对于GET请求，不需要设置输出

        // 连接并获取响应码
        int responseCode = connection.getResponseCode();
        System.out.println("Response Code: " + responseCode);

        // 读取响应内容
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        // 打印响应内容
        String string = response.toString();
        //System.out.println(getAllContent(string));
        getAllContent(string,newUrlService);
    }

    private  String getAllContent(String jsonResponse,NewUrlService newUrlService){
       // String htmlContent = "<a href=\"https://example.com\" title=\"Example Title\">Link Text</a>";

        JSONObject jsonObject = JSONUtil.parseObj(jsonResponse);
        String string = jsonObject.get("data").toString();

        Document doc = Jsoup.parse(string);

        // 提取所有a标签
        Elements links = doc.select("a");

        List<NewUrlEntity> newList = new ArrayList<>();
        // 遍历并提取href和title
        for (Element link : links) {
            String href = link.attr("href");  // 获取href属性
            href = href.replace("\\\"", "");  // 去掉转义的双引号
            if (!href.startsWith("http://www.miit.gov.cn")){
                href="https://www.miit.gov.cn"+href;
            }
            String title = link.attr("title");  // 获取title属性
            title = title.replace("\\\"", "");  // 去掉转义的双引号
            String htmlContent = getHtmlContent(href);
            NewUrlEntity urlEntity = new NewUrlEntity();
            urlEntity.setUrl(href);
            urlEntity.setContentType(NewEnums.MIIT.getGroup());
            urlEntity.setTitle(title);
            urlEntity.setContent(htmlContent);
            newList.add(urlEntity);
           // newUrlService.judgeInsert(href,NewEnums.MIIT.getGroup(), title,htmlContent);
            System.out.println("Link: " + href + ", Title: " + title+",Content"+htmlContent);
        }
    newUrlService.insertBatchAndAnalyse(newList,NewEnums.MIIT.getGroup());
        return "";
    }
    private static  String getHtmlContent(String url){
        try {

            Document doc = Jsoup.connect(url).get();  // 获取网页内容

            // 获取网页中的所有文本内容
            String textContent = doc.text();  // 获取页面所有文本
           // System.out.println("Text Content: \n" + textContent);

            // 如果你想提取特定部分的内容，可以使用CSS选择器来选择相应的元素
            // 例如，获取页面中所有的<p>标签中的文本
            Elements paragraphs = doc.select("p");
            StringBuilder sb = new StringBuilder();
            for (org.jsoup.nodes.Element paragraph : paragraphs) {
          //      System.out.println(paragraph.text());  // 输出每个段落的文本内容
                sb.append(paragraph.text());
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
