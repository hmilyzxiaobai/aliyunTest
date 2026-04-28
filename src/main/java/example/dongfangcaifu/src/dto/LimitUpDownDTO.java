package example.dongfangcaifu.src.dto;

import lombok.Data;
import java.util.List;

@Data
public class LimitUpDownDTO {

    private StatsDTO stats;
    private List<MonitoringRecordDTO> records;
    private Long total;

    @Data
    public static class StatsDTO {
        private Integer shUp;        // 1 沪A涨停
        private Integer szUp;        // 2 深A涨停
        private Integer shDown;      // 3 沪A跌停
        private Integer szDown;      // 4 深A跌停
        private Integer szLargeUp;   // 5 深A大涨
        private Integer szLargeDown; // 6 深A大跌
    }

    @Data
    public static class MonitoringRecordDTO {
        private Long id;
        private String companyName;
        private String companyCode;
        private String price;
        private String changeDetails;
        private String dateHis;
        private Integer type;
        private String typeName;
    }
}
