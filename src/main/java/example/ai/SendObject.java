package example.ai;

import lombok.Data;

import java.util.List;

@Data
public class SendObject {
    private String model;
    private List<MessageObject> messages;
    private Float temperature;
    @Data
    public static class MessageObject{
        private String role;
        private String content;
    }
}
