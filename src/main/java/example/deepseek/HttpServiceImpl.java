package example.deepseek;

import org.apache.hc.client5.http.classic.methods.HttpDelete;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.classic.methods.HttpPut;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;

public class HttpServiceImpl implements HttpService {
    private final CloseableHttpClient httpClient;

    public HttpServiceImpl() {
        this.httpClient = HttpClients.createDefault();
    }

    @Override
    public String get(String url) throws Exception {
        HttpGet request = new HttpGet(url);
        try (CloseableHttpResponse response = httpClient.execute(request)) {
            return EntityUtils.toString(response.getEntity());
        }
    }

    @Override
    public String post(String url, String json) throws Exception {
        HttpPost request = new HttpPost(url);
        request.setEntity(new StringEntity(json));
        request.setHeader("Content-Type", "application/json");
        try (CloseableHttpResponse response = httpClient.execute(request)) {
            return EntityUtils.toString(response.getEntity());
        }
    }

    @Override
    public String put(String url, String json) throws Exception {
        HttpPut request = new HttpPut(url);
        request.setEntity(new StringEntity(json));
        request.setHeader("Content-Type", "application/json");
        try (CloseableHttpResponse response = httpClient.execute(request)) {
            return EntityUtils.toString(response.getEntity());
        }
    }

    @Override
    public String delete(String url) throws Exception {
        HttpDelete request = new HttpDelete(url);
        try (CloseableHttpResponse response = httpClient.execute(request)) {
            return EntityUtils.toString(response.getEntity());
        }
    }
}
