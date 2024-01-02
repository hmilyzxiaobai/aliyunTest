package example.baidu;

import com.alibaba.fastjson.JSON;
import example.baidu.dto.ChatWenDto;
import example.baidu.dto.MessageDto;
import example.baidu.vo.ChatResponse;
import example.baidu.vo.TokenVo;
import okhttp3.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class ChatSend {
    static final OkHttpClient HTTP_CLIENT = new OkHttpClient()
            .newBuilder().connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60,TimeUnit.SECONDS)
            .build();

    public static void main(String []args) throws IOException {
        WenToken wenToken= new WenToken();
        TokenVo token = wenToken.getToken();
        ChatWenDto chatWenDto = init();
        String userId = UUID.randomUUID().toString();
        chatWenDto.setUser_id(userId);
        List<MessageDto> list = new ArrayList<>();
        chatWenDto.setMessages(list);
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("You: ");
            String question = scanner.nextLine();
            MessageDto dto = new MessageDto();
            dto.setRole("user");
            dto.setContent(question);
            list.add(dto);
            String s = JSON.toJSON(chatWenDto).toString();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, s);
            Request request = new Request.Builder()
                    .url("https://aip.baidubce.com/rpc/2.0/ai_custom/v1/wenxinworkshop/chat/completions_pro?access_token=" + token.getAccess_token())
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .build();
            Response response = HTTP_CLIENT.newCall(request).execute();
            String responseStr = response.body().string();
            ChatResponse chatResponse = JSON.parseObject(responseStr, ChatResponse.class);
            MessageDto dtoRes = new MessageDto();
            dtoRes.setRole("assistant");
            dtoRes.setContent(chatResponse.getResult());
            list.add(dtoRes);
            System.out.println("文心一言: "+dtoRes.getContent());
        }
    }
    public static ChatWenDto init(){
        ChatWenDto chatWenDto = new ChatWenDto();
        chatWenDto.setDisable_search(false);
        chatWenDto.setEnable_citation(false);
        return chatWenDto;
    }
}
