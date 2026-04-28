package example.dongfangcaifu.src.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("macd_info")
public class MacdInfoEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 自增主键 */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 公司代码 */
    @TableField("company_code")
    private String companyCode;

    /** 公司名称 */
    @TableField("company_name")
    private String companyName;

    /** 市场：深圳主板/创业板/上海主板 */
    @TableField("market")
    private String market;

    /** 价格 */
    @TableField("price")
    private String price;

    /** DIF值（快线） */
    @TableField("dif")
    private String dif;

    /** DEA值（慢线） */
    @TableField("dea")
    private String dea;

    /** MACD值（柱状线） */
    @TableField("macd")
    private String macd;

    /** 主力资金净流入额(元) */
    @TableField("net_amount")
    private BigDecimal netAmount;

    /** 时间df分区（格式：yyyyMMdd） */
    @TableField("df")
    private String df;


    /** 是否删除 0-否 1-删除 */
    @TableField("is_deleted")
    @TableLogic
    private Integer isDeleted;

    // 辅助字段（非数据库字段）
    @TableField(exist = false)
    private String signal;  // MACD信号：金叉/死叉/多头/空头

    @TableField(exist = false)
    private String trend;   // 趋势判断：上涨/下跌/震荡
}