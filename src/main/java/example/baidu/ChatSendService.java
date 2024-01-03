package example.baidu;

import com.alibaba.fastjson.JSON;
import example.baidu.dto.ChatWenDto;
import example.baidu.dto.MessageDto;
import example.baidu.vo.ChatResVoList;
import example.baidu.vo.ChatResponse;
import okhttp3.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class ChatSendService {

    static final OkHttpClient HTTP_CLIENT = new OkHttpClient()
            .newBuilder().connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60,TimeUnit.SECONDS)
            .build();


    public ChatResVoList chat(List<MessageDto> dtoList, String userId) throws IOException {
        WenToken wenToken= new WenToken();
        String token = wenToken.getToken();
        ChatWenDto chatWenDto = init();
        chatWenDto.setUser_id(userId);
        chatWenDto.setMessages(dtoList);
        String s = JSON.toJSON(chatWenDto).toString();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, s);
        Request request = new Request.Builder()
                .url("https://aip.baidubce.com/rpc/2.0/ai_custom/v1/wenxinworkshop/chat/completions_pro?access_token=" + token)
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .build();
        Response response = HTTP_CLIENT.newCall(request).execute();
        String responseStr = response.body().string();
        ChatResponse chatResponse = JSON.parseObject(responseStr, ChatResponse.class);
        MessageDto dtoRes = new MessageDto();
        dtoRes.setRole("assistant");
        dtoRes.setContent(chatResponse.getResult());
        dtoList.add(dtoRes);
        System.out.println("文心一言: "+dtoRes.getContent());
        ChatResVoList chatResVoList = new ChatResVoList();
        chatResVoList.setMessage(chatResponse.getResult());
        chatResVoList.setMessageDtoList(dtoList);
        return chatResVoList;

    }
    public static ChatWenDto init(){
        ChatWenDto chatWenDto = new ChatWenDto();
        chatWenDto.setDisable_search(false);
        chatWenDto.setEnable_citation(false);
        return chatWenDto;
    }
}
