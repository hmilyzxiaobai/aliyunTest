package example.dongfangcaifu.feishu.res;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TokenResponse {
    private Integer code;
    private String msg;
    private String tenantAccessToken;
    private Integer expire;
}

