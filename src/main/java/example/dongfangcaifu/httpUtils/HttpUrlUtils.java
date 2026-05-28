package example.dongfangcaifu.httpUtils;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.HttpURLConnection;
import java.net.URL;

@Slf4j
@Component
public class HttpUrlUtils {

    @Value("${dongfangcaifu.cookie}")
    private String cookie;

    public HttpURLConnection httpBuildUrlUtils(URL url,HttpRefererEnum enumCode){
        HttpURLConnection connection = null;
        try{
            connection = (HttpURLConnection) url.openConnection();
            // 设置请求方法为GET
//            connection.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.9");
//            connection.setRequestProperty("Sec-Fetch-Dest", "script");
//            connection.setRequestProperty("Sec-Fetch-Mode", "no-cors");
//            connection.setRequestProperty("Sec-Fetch-Site", "same-site");
//            connection.setRequestProperty("sec-ch-ua", "\"Google Chrome\";v=\"147\", \"Not.A/Brand\";v=\"8\", \"Chromium\";v=\"147\"");
//            connection.setRequestProperty("sec-ch-ua-mobile", "?0");
//            connection.setRequestProperty("sec-ch-ua-platform", "\"Windows\"");
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "*/*");
            connection.setRequestProperty("Cache-Control", "no-cache");
            connection.setRequestProperty("Connection", "keep-alive");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36");
           // connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/147.0.0.0 Safari/537.36");
            connection.setRequestProperty("Origin", "https://quote.eastmoney.com");

            // 此处需要策略 不同的地方的referer不同 暂时不用试试 如果不行就改
           connection.setRequestProperty("Referer", enumCode.getComment());
                        // 设置Cookie（从浏览器复制）
            connection.setRequestProperty("Cookie", cookie);

            connection.setUseCaches(false);
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(30000); // 无限读取，等待推送

        }catch (Exception e){
            e.printStackTrace();
        }
        return connection;
    }


    public HttpURLConnection httpBuildUrlUtils(URL url,String code,HttpRefererEnum enumCode) {
        HttpURLConnection httpURLConnection = httpBuildUrlUtils(url, enumCode);
        httpURLConnection.setRequestProperty("Referer", enumCode.getComment()+code+".html");
        return httpURLConnection;
    }

        public void disconnect(HttpURLConnection connection){
        try {
            connection.disconnect();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


}
