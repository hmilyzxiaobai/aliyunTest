package example.dongfangcaifu.feishu.res;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RecordResponse {
    private Integer code;
    private String msg;
    private Data data;

    @lombok.Data
    public static class Data {
        private Boolean hasMore;
        private String pageToken;
        private Integer total;
        private List<Record> items;
    }
}