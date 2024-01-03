package example.baidu.vo;

import example.baidu.dto.MessageDto;
import lombok.Data;

import java.util.List;

@Data
public class ChatResVoList {
    private List<MessageDto> MessageDtoList;
    private String message;
}
