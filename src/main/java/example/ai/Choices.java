package example.ai;


import lombok.Data;

@Data
public class Choices {
    private String text;
    private Integer index;
    private String logprobs;
    private String finish_reason;
    private Message message;
    @Data
    public static class Message{
        private String role;
        private String content;
    }
}

