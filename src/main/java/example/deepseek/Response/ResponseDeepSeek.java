package example.deepseek.Response;

import lombok.Data;

import java.util.List;

@Data
public class ResponseDeepSeek {
    String id;
    String object;
    Long created;
    String model;
    String systemFingerprint;
    List<Choice> choices;
    Usage usage;

}
