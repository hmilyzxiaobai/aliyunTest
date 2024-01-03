package example.ai;

import lombok.Data;

import java.util.List;

@Data
public class SendObject {
    private String model;
    private List<MessageObject> messages;
    private Float temperature;
    private String action;
    private conversationMode conversation_mode;
    private Boolean history_and_training_disabled;
    private String parent_message_id;
    @Data
    public static class conversationMode{
        private String kind;
    }
    @Data
    public static class MessageObject{
        private String role;
        private String content;
    }
}
