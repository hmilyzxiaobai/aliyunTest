package example.service;

import okhttp3.*;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class KeepAlivePostRequest {
    public static void main(String[] args) {
        String url = "http://localhost:8080/deliveryContent/test/keep"; // 替换成你的目标 URL
        String postData = "Your POST data"; // 替换成你要发送的 POST 数据

        OkHttpClient client = new OkHttpClient();

        // 构建请求体
        RequestBody requestBody = RequestBody.create(postData, MediaType.parse("application/json"));

        // 循环内发送多个 POST 请求
        for (int i = 0; i < 5; i++) { // 假设发送 5 次 POST 请求
            Request request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                System.out.println("POST Request #" + (i + 1) + " - Response Code: " + response.code());
                // 可以根据需要处理响应内容
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
