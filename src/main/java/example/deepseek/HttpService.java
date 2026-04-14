package example.deepseek;

import org.apache.hc.client5.http.classic.methods.*;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;

public interface HttpService {
    String get(String url) throws Exception;
    String post(String url, String json) throws Exception;
    String put(String url, String json) throws Exception;
    String delete(String url) throws Exception;
}

