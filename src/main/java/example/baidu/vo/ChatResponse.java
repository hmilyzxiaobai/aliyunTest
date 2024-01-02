package example.baidu.vo;
import lombok.Data;

@Data
public class ChatResponse {
    private String id;
    private String object;
    private Long created;
    private String result;
    private Boolean is_truncated;
    private Boolean need_clear_history;
    private String finish_reason;
    private UsageVo usageVo;
}
