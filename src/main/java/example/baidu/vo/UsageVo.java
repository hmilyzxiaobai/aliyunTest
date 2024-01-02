package example.baidu.vo;

import lombok.Data;

@Data
public class UsageVo {
    private Integer prompt_tokens;
    private Integer completion_tokens;
    private Integer total_tokens;
}
