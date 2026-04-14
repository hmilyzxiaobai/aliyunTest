package example.deepseek.interfaceForest;


import com.dtflys.forest.annotation.*;
import example.deepseek.Request.RequestDto;
import example.deepseek.Response.ResponseDeepSeek;

@BaseRequest(baseURL="https://api.deepseek.com",connectTimeout = 60000,readTimeout = 60000)
public interface ForestInterfaceDeepSeek {
      @Post(url = "/chat/completions")@Headers({"Authorization: ${bearer}",
              "Content-Type: application/json"})
      ResponseDeepSeek chatCompletions(@Var("bearer") String bearer
              , @Body  RequestDto dto);
}

