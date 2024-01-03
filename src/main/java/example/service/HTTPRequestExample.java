package example.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import example.ai.Answer;
import example.ai.Choices;
import example.ai.SendObject;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.rmi.ServerException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class HTTPRequestExample {

    private static SSLConnectionSocketFactory getSslConnectionSocketFactory() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        TrustStrategy acceptingTrustStrategy = (x509Certificates, s) -> true;
        SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy).build();
        return new SSLConnectionSocketFactory(sslContext, new NoopHostnameVerifier());
    }


    public static void main(String[] args) {

        try (CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setSSLSocketFactory(getSslConnectionSocketFactory())
                .build()) {
            submit(httpClient, getHttpPost());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }



    }

    private static void submit(CloseableHttpClient httpClient, HttpPost post) throws IOException {
        Scanner scanner = new Scanner(System.in);
        String question;
        while (true) {
            System.out.print("You: ");
            question = scanner.nextLine();
            System.out.print("AI: ");
            StringEntity stringEntity = new StringEntity(JSONObject.toJSONString(init(question)), getContentType());
            post.setEntity(stringEntity);
            CloseableHttpResponse response;
            try {
                response = httpClient.execute(post);
            } catch (SocketTimeoutException e) {
                System.out.println("-- warning: Read timed out!");
                continue;
            } catch (SocketException e) {
                System.out.println("-- warning: Connection reset!");
                continue;
            } catch (Exception e) {
                System.out.println("-- warning: Please try again!");
                continue;
            }
            printAnswer(response);
        }
    }

    private static void printAnswer(CloseableHttpResponse response) throws IOException {
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            String responseJson = EntityUtils.toString(response.getEntity());
            Answer answer = JSON.parseObject(responseJson, Answer.class);
            StringBuilder answers = new StringBuilder();
            List<Choices> choices = answer.getChoices();
            for (Choices choice : choices) {
                answers.append(choice.getText());
            }
            System.out.println(answers.substring(2, answers.length()));
        } else if (response.getStatusLine().getStatusCode() == 429) {
            System.out.println("-- warning: Too Many Requests!");
        } else if (response.getStatusLine().getStatusCode() == HttpStatus.SC_INTERNAL_SERVER_ERROR) {
            throw new ServerException("------ Server error, program terminated! ------");
        } else {
            System.out.println("-- warning: Error, please try again!");
        }
    }
    private static ContentType getContentType() {
        return ContentType.create("text/json", "UTF-8");
    }
    private static SendObject init(String message){
        SendObject sendObject = new SendObject();
        sendObject.setModel("gpt-3.5-turbo");
        sendObject.setTemperature(0.7f);
        List<SendObject.MessageObject> messages = new ArrayList<SendObject.MessageObject>();
        SendObject.MessageObject messageObject= new SendObject.MessageObject();
        messageObject.setContent(message);
        messageObject.setRole("user");
        messages.add(messageObject);
        sendObject.setMessages(messages);
        return sendObject;
    }

    private static HttpPost getHttpPost(){

        try {
            String openAiKey = "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";

            // 创建 URL 对象
            URL url = new URL("https://api.openai.com/v1/chat/completions"); // 替换成你的目标 URL
            HttpPost post = new HttpPost("https://api.openai.com/v1/chat/completions");
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectTimeout(6000).setConnectionRequestTimeout(6000)
                    .setSocketTimeout(6000).build();
            post.setConfig(requestConfig);
            post.addHeader("Content-Type", "application/json");
            post.addHeader("Authorization", "Bearer " + openAiKey);
            post.setConfig(requestConfig);
            return post;


//            // 创建 HttpURLConnection 对象
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            // 设置请求方法（GET、POST等）
//            connection.setRequestMethod("POST");
//            // 设置请求头
//            connection.setRequestProperty("Content-Type", "application/json");
//            connection.setRequestProperty("Authorization", "Bearer "+openAiKey); // 替换成你的授权 Token
//
//            // 设置是否向服务器输出数据（POST 请求需要设置为 true）
//            connection.setDoOutput(true);
//
//            // 构建请求体
//            String requestBody = JSONObject.toJSONString(init()); // 替换成你要发送的对象
//            System.out.println(requestBody);
//            // 发送请求体数据
//            connection.getOutputStream().write(requestBody.getBytes());
//
//            // 获取响应
//            int responseCode = connection.getResponseCode();
//            System.out.println("Response Code: " + responseCode);
//
//            // 读取响应内容
//            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//            String inputLine;
//            StringBuilder response = new StringBuilder();
//            while ((inputLine = in.readLine()) != null) {
//                response.append(inputLine);
//            }
//            in.close();
//            Answer answer = JSON.parseObject(response.toString(), Answer.class);
//
//            // 打印响应内容
//            System.out.println("Response: " + answer);
//
//            // 关闭连接
//            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
