package example.dongfangcaifu.src.response;

import lombok.Data;

import java.util.Map;

@Data
public class GouResponse {
    boolean isGou;
    Map<String,String> gouHis;
    int gouLen;
}
