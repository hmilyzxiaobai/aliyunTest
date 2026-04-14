package example.deepseek.Response;

import example.deepseek.Request.Message;
import lombok.Data;

@Data
public class Choice {
    Integer index;
    String logprobs;
    String finish_reason;
    Message message;
}
