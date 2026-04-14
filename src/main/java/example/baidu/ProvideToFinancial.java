package example.baidu;

import com.alibaba.fastjson.JSON;
import example.baidu.dto.ChatWenDto;
import example.baidu.dto.MessageDto;
import example.baidu.vo.ChatResponse;
import okhttp3.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class ProvideToFinancial {
    static final OkHttpClient HTTP_CLIENT = new OkHttpClient()
            .newBuilder().connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60,TimeUnit.SECONDS)
            .build();



   public String chatToBaiDu(List<MessageDto> list,String userId){

//       List<MessageDto> list = new ArrayList<>();
//       MessageDto dto = new MessageDto();
//       dto.setRole("user");
//       dto.setContent(content);
//       list.add(dto);
//       String userId = UUID.randomUUID().toString();
//
       ChatWenDto chatWenDto = init();
       chatWenDto.setUser_id(userId);
       chatWenDto.setMessages(list);

       String s = JSON.toJSON(chatWenDto).toString();
       MediaType mediaType = MediaType.parse("application/json");
       RequestBody body = RequestBody.create(mediaType, s);
        try {
            //MediaType mediaType = MediaType.parse("application/json");
          //  RequestBody body = RequestBody.create(mediaType, "{\"messages\":[{\"role\":\"user\",\"content\":\"你好\"},{\"role\":\"assistant\",\"content\":\"您好！有什么我可以帮助您的吗？\"}],\"temperature\":0.95,\"top_p\":0.7,\"penalty_score\":1,\"enable_user_memory\":false,\"user_memory_extract_level\":0}");
            Request request = new Request.Builder()
                    .url("https://aip.baidubce.com/rpc/2.0/ai_custom/v1/wenxinworkshop/chat/ernie-char-fiction-8k?access_token=" + WenToken.getAccessToken())
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .build();
            Response response = HTTP_CLIENT.newCall(request).execute();
            String responseStr = response.body().string();
            ChatResponse chatResponse = JSON.parseObject(responseStr, ChatResponse.class);
            return chatResponse.getResult();
            // System.out.println(response.body().string());
        }catch (Exception e){
            e.printStackTrace();
        }
        return "错误";
   }
    private static ChatWenDto init(){
        ChatWenDto chatWenDto = new ChatWenDto();
        chatWenDto.setDisable_search(false);
        chatWenDto.setEnable_citation(false);
        chatWenDto.setTemperature(0.95f);
        chatWenDto.setTop_p(0.7f);
        chatWenDto.setPenalty_score(1f);
        return chatWenDto;
    }
}
