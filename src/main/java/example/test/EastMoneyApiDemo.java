package example.test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class EastMoneyApiDemo {

    public static void main(String[] args) throws IOException, URISyntaxException {
       // String url = "https://push2.eastmoney.com/api/qt/clist/get?pn=1&pz=20&po=1&np=1&ut=bd1d9ddb04089700cf9c27f6f7426281&fltt=2&invt=2&dect=1&wbp2u=|0|0|0|web&fid=f26&fs=b:BK0707&fields=f1,f2,f3,f4,f5,f6,f7,f8,f9,f10,f12,f13,f14,f15,f16,f17,f18,f20,f21,f23,f24,f25,f26,f22,f11,f62,f128,f136,f115,f152";

        URI url = new URIBuilder()
                .setScheme("https")
                .setHost("push2.eastmoney.com")
                .setPath("/api/qt/clist/get")
                .setParameter("pn", "1")
                .setParameter("pz", "20")
                .setParameter("po", "1")
                .setParameter("np", "1")
                .setParameter("ut", "bd1d9ddb04089700cf9c27f6f7426281")
                .setParameter("fltt", "2")
                .setParameter("invt", "2")
                .setParameter("dect", "1")
                .setParameter("wbp2u", "|0|0|0|web")  // 自动编码 |
                .setParameter("fid", "f26")
                .setParameter("fs", "b:BK0707")       // 自动编码 :
                .setParameter("fields", "f1,f2,f3,f4,f5,f6,f7,f8,f9,f10,f12,f13,f14,f15,f16,f17,f18,f20,f21,f23,f24,f25,f26,f22,f11,f62,f128,f136,f115,f152")
                .build();

        //System.out.println("编码后的URL: " + uri.toString());
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(url);
            request.setHeader("User-Agent", "Mozilla/5.0");

            try (CloseableHttpResponse response = client.execute(request)) {
                String json = EntityUtils.toString(response.getEntity());
                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readTree(json);

                // 解析数据
                JsonNode data = root.get("data");
                if (data != null && data.has("diff")) {
                    JsonNode stocks = data.get("diff");
                    for (JsonNode stock : stocks) {
                        String name = stock.get("f14").asText();    // 股票名称
                        String code = stock.get("f12").asText();    // 股票代码
                        double price = stock.get("f2").asDouble();   // 最新价
                        double changePercent = stock.get("f3").asDouble(); // 涨跌幅

                        System.out.printf("名称: %s(%s), 价格: %.2f, 涨跌幅: %.2f%%\n",
                                name, code, price, changePercent);
                    }
                }
            }
        }
    }
}