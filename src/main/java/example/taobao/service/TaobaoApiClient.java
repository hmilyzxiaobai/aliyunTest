package example.taobao.service;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.EntityUtils;

import java.util.HashMap;
import java.util.Map;

public class TaobaoApiClient {
    private static final String API_URL = "https://api.taobao.com/router/rest";
    private static final String APP_KEY = "your_app_key";
    private static final String SECRET = "your_secret";

//    public static String searchItems(String keyword) throws Exception {
//        Map<String, String> params = new HashMap<>();
//        params.put("method", "taobao.item.search");
//        params.put("q", keyword);
//        params.put("page_no", "1");
//        params.put("page_size", "20");
//        params.put("timestamp", String.valueOf(System.currentTimeMillis()));
//
//        // 生成签名
//        String sign = TopUtils.signTopRequest(params, SECRET);
//        params.put("sign", sign);
//
//        // 发送请求
//        CloseableHttpClient client = HttpClients.createDefault();
//        HttpGet httpGet = new HttpGet(API_URL + "?" + TopUtils.getQueryStr(params));
//        httpGet.addHeader("Authorization", "TOP " + APP_KEY);
//
//        try (CloseableHttpResponse response = client.execute(httpGet)) {
//            return EntityUtils.toString(response.getEntity());
//        }
//    }
}