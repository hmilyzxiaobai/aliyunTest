package example.dongfangcaifu.src.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("monitoring_person")
public class MonitoringPersonEntity {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("company_name")
    private String companyName;

    @TableField("company_code")
    private String companyCode;

    @TableField("monitoring_price")
    private String monitoringPrice;

    @TableField("monitoring_sell_price")
    private String monitoringSellPrice;

    @TableField("ai_text")
    private String aiText;

    @TableField("news_text")
    private String newsText;


    @TableField("is_deleted")
    @TableLogic
    private Integer isDeleted;
}