package example.dongfangcaifu.src.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper=false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("plate_dark_capital")
public class PlateDarkCapitalEntity {


    /** 板块名字 */
    private String conceptName;

    /** 板块代码 */
    private String conceptCode;

    /**
     * 板块类型 1-行业板块 2-概念板块
     */
    private String conceptType;
    /** 记录时间 */
    private String dateHis;
    /**
     * 涨幅
     */
    private String changeDetail;

    /**
     * 暗盘活跃度
     */
    private String darkActive;
    /**
     * 总资金
     */
    private String netAmount;
    /**
     * 暗盘资金
     */
    private String darkAmount;
    /**
     * 明盘资金
     */
    private String brightAmount;

    private String upAmount;
    private String downAmount;

    private String createTime;
}
