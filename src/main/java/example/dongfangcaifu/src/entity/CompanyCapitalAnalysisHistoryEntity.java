package example.dongfangcaifu.src.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper=false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("company_capital_analysis_history")
public class CompanyCapitalAnalysisHistoryEntity {

    /** 公司名字 */
    private String companyName;

    /** 公司代码 */
    private String companyCode;

    /** 记录时间 */
    private String dateHis;

    /** 浮动百分比 */
    private String changeDetails;

    /**当天价格*/
    private String price;

    private String bkType;

    /**当天主力净流入及占比*/
    private String netAmount;

    private String netProportion;

    /**当天超大单净流入及占比*/
    private String superLargeOrderAmount;

    private String superLargeOrderProportion;

    /**当天大单净流入及占比*/
    private String bigBillAmount;

    private String bigBillProportion;

    /**当天中单净流入及占比*/
    private String middleOrderAmount;

    private String middleOrderProportion;

    /**当天小单净流入及占比*/
    private String littleOrderAmount;

    private String littleOrderProportion;



}
