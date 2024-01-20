package example.service;

import com.alibaba.fastjson.JSON;
import example.ai.Answer;
import example.ai.Choices;
import org.apache.http.HttpHost;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.List;

public class OtherMethod {
    public static void main(String[] args) throws IOException {
        OtherMethod otherMethod = new OtherMethod();

        System.out.println(otherMethod.doChatGPT("你好"));
    }

    public String doChatGPT(String question) throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        // 新增代码：增加代理
        String proxyHost = "127.0.0.1";
        int proxyPort = 10809;
        HttpHost proxy = new HttpHost(proxyHost, proxyPort);
        RequestConfig requestConfig = RequestConfig.custom()
                .setProxy(proxy)
                .build();

        HttpPost post = new HttpPost("https://api.openai.com/v1/chat/completions");
        post.addHeader("Content-Type", "application/json");
        post.addHeader("Authorization", "Bearer " + "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");
        // 新增代码：将代理类放入配置中
        post.setConfig(requestConfig);
        String paramJson = "{\n" +
                "     \"model\": \"gpt-3.5-turbo\",\n" +
                "     \"messages\": [{\"role\": \"user\", \"content\": \""+question+"\"}],\n" +
                "     \"temperature\": 0.7\n" +
                "   }";

        StringEntity stringEntity = new StringEntity(paramJson, ContentType.create("text/json", "UTF-8"));
        post.setEntity(stringEntity);

        CloseableHttpResponse response = httpClient.execute(post);
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            String jsonStr = EntityUtils.toString(response.getEntity());
         //   logger.info("jsoon={}", jsonStr);
            Answer aiAnswer = JSON.parseObject(jsonStr, Answer.class);
            StringBuilder answers = new StringBuilder();
            List<Choices> choices = aiAnswer.getChoices();
            for (Choices choice : choices) {
            //    answers.append(choice.getMessage().getContent());
            }
            return answers.toString();
        } else {
            throw new RuntimeException("api.openai.com Err Code is " + response.getStatusLine().getStatusCode());
        }
    }

}
