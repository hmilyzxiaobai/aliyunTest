package example.dongfangcaifu.feishu.res;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FieldValue {
    private String text;
    private String link;
    private List<String> optionIds;
    private List<Attachment> attachments;
    private String type;

    @Data
    public static class Attachment {
        private String fileToken;
        private String name;
        private String size;
        private String tmpUrl;
    }

    // 获取文本值
    public String getTextValue() {
        if (text != null) return text;
        if (link != null) return link;
        if (optionIds != null && !optionIds.isEmpty()) return String.join(",", optionIds);
        if (attachments != null && !attachments.isEmpty()) return attachments.get(0).getName();
        return "";
    }
}