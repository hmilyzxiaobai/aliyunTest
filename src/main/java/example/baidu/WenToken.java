package example.baidu;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import example.baidu.vo.TokenVo;
import okhttp3.*;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class WenToken {
    static final String key = "Gr8DOsehr1Rf9GN3XCNstYy6";
    static final String secret= "2FXE9dIUHvuMpXAg3IZUKylsbXcdV12R";
    private static Cache<String,String> cache = CacheBuilder.newBuilder()
            .concurrencyLevel(Runtime.getRuntime().availableProcessors())
            .initialCapacity(100)
            .maximumSize(5000)
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .build();
    private static final String TOKEN="token";

    static final OkHttpClient HTTP_CLIENT = new OkHttpClient().newBuilder().build();


    public String getToken() throws IOException {
        if (!StrUtil.isBlankIfStr(cache.getIfPresent(TOKEN))){
            return cache.getIfPresent(TOKEN);
        }

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "");
        Request request = new Request.Builder()
                .url("https://aip.baidubce.com/oauth/2.0/token?client_id="+key+"&client_secret="+secret+"&grant_type=client_credentials")
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .build();
        Response response = HTTP_CLIENT.newCall(request).execute();
        String string = response.body().string();
        TokenVo tokenVo = JSON.parseObject(string, TokenVo.class);
        System.out.println(tokenVo.getAccess_token());
        cache.put(TOKEN,tokenVo.getAccess_token());
        return cache.getIfPresent(TOKEN);
    }

}

