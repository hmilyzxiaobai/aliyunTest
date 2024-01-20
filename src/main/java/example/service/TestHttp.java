package example.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import example.ai.Answer;
import example.ai.SendObject;
import okhttp3.*;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.entity.StringEntity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TestHttp {
    static final String gpt3= "gpt-3.5-turbo";
    static final String chatModle = "text-davinci-002-render-sha";

    public static void main(String[] args) throws IOException {
        String openAiKey = "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";

        // 创建 URL 对象
            URL url = new URL("https://api.openai.com/v1/chat/completions"); // 替换成你的目标 URL
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            // 设置请求方法（GET、POST等）
            connection.setRequestMethod("POST");
            // 设置请求头
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Authorization", "Bearer "+openAiKey); // 替换成你的授权 Token
            connection.setRequestProperty("Connection", "Keep-Alive"); // 保持连接状态

            // 设置是否向服务器输出数据（POST 请求需要设置为 true）
            connection.setDoOutput(true);
        OkHttpClient client = new OkHttpClient();

        Scanner scanner = new Scanner(System.in);
        String question;
        int index=0;
        String id="";
        while (index<1000) {
            index++;
            System.out.print("You: ");
            question = scanner.nextLine();
            // 构建请求体
            String postData = JSONObject.toJSONString(init(question,id)); // 替换成你要发送的对象

            RequestBody requestBody = RequestBody.create(postData, MediaType.parse("application/json"));

            Request request = new Request.Builder()
                    .url(url)
                    .post(requestBody).addHeader("Content-Type", "application/json").addHeader("Authorization", "Bearer "+openAiKey)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    Answer answer = JSON.parseObject(responseBody, Answer.class);
                    id=answer.getId();
                    System.out.println("POST Request #" + (  1) + " - Response Body: " + responseBody);
                } else {
                    System.out.println("POST Request #" + (  1) + " - Response Code: " + response.code());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println();
        }
        connection.disconnect();
    }
    private static SendObject init(String message,String parentId){
        SendObject sendObject = new SendObject();
        sendObject.setModel(gpt3);
        sendObject.setTemperature(0.7f);
        List<SendObject.MessageObject> messages = new ArrayList<SendObject.MessageObject>();
        SendObject.MessageObject messageObject= new SendObject.MessageObject();
        messageObject.setContent(message);
        messageObject.setRole("user");
        messages.add(messageObject);
        sendObject.setMessages(messages);
        sendObject.setHistory_and_training_disabled(false);
        SendObject.conversationMode conversationMode= new SendObject.conversationMode();
        conversationMode.setKind("primary_assistant");
        sendObject.setConversation_mode(conversationMode);
        sendObject.setAction("action");
        sendObject.setParent_message_id(parentId);
        return sendObject;
    }
}
