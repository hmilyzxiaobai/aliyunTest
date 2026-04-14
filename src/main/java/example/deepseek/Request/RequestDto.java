package example.deepseek.Request;

import cn.hutool.core.lang.Singleton;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Data
@Slf4j
public class RequestDto {
    String model;
    Boolean stream;
    List<Message> messages;

    private static RequestDto request;
    static {
        request = new RequestDto();
    }
    private RequestDto() {}
    public static RequestDto getInstance() {
        request.setModel("deepseek-chat");
        request.setStream(false);
        return request;
    }
}
