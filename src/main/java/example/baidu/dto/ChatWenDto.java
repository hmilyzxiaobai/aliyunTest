package example.baidu.dto;

import lombok.Data;

import java.util.List;

@Data
public class ChatWenDto {
    private List<MessageDto> messages;
    private Float temperature;
    private Float top_p;
    private Float penalty_score;
    private Boolean stream;
    private String system;
    private List<String> stop;
    private Boolean disable_search;
    private Boolean enable_citation;
    private String response_format;
    private String user_id;

}
