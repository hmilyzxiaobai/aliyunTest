package example.service;

import okhttp3.*;

import java.io.IOException;

public class BaiDu {
    static final String key = "Gr8DOsehr1Rf9GN3XCNstYy6";
    static final String secret= "2FXE9dIUHvuMpXAg3IZUKylsbXcdV12R";

    static final OkHttpClient HTTP_CLIENT = new OkHttpClient().newBuilder().build();

    public static void main(String []args) throws IOException {
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "");
        Request request = new Request.Builder()
                .url("https://aip.baidubce.com/oauth/2.0/token?client_id="+key+"&client_secret="+secret+"&grant_type=client_credentials")
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .build();
        Response response = HTTP_CLIENT.newCall(request).execute();
        System.out.println(response.body().string());

    }

}
