package example.service;

import com.alibaba.fastjson.JSONObject;
import example.ai.SendObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class HTTPRequestExample {
    public static void main(String[] args) {
        try {
            // 创建 URL 对象
            URL url = new URL("https://api.openai.com/v1/chat/completions"); // 替换成你的目标 URL

            // 创建 HttpURLConnection 对象
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // 设置请求方法（GET、POST等）
            connection.setRequestMethod("POST");

            // 设置请求头
            String openAiKey = "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";
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Authorization", "Bearer "+openAiKey); // 替换成你的授权 Token

            // 设置是否向服务器输出数据（POST 请求需要设置为 true）
            connection.setDoOutput(true);


            // 构建请求体
            String requestBody = JSONObject.toJSONString(init()); // 替换成你要发送的对象
            System.out.println(requestBody);
            // 发送请求体数据
            connection.getOutputStream().write(requestBody.getBytes());

            // 获取响应
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
            System.out.println("Response: " + response.toString());

            // 关闭连接
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static SendObject init(){
        SendObject sendObject = new SendObject();
        sendObject.setModel("gpt-3.5-turbo");
        sendObject.setTemperature(0.7f);
        List<SendObject.MessageObject> messages = new ArrayList<SendObject.MessageObject>();
        SendObject.MessageObject messageObject= new SendObject.MessageObject();
        messageObject.setContent("你好 ");
        messageObject.setRole("user");
        messages.add(messageObject);
        sendObject.setMessages(messages);
        return sendObject;
    }
}
