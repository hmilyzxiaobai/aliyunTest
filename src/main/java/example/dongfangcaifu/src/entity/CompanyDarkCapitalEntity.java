package example.dongfangcaifu.src.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper=false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("company_dark_capital")
public class CompanyDarkCapitalEntity {

    /** 公司名字 */
    private String companyName;

    /** 公司代码 */
    private String companyCode;

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

}
