package example.dongfangcaifu.feishu.res;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Record {
    private String recordId;
    private Map<String, FieldValue> fields;
    private String createdAt;
    private String updatedAt;
}