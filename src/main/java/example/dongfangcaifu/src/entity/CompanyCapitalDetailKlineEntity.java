package example.dongfangcaifu.src.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper=false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("company_capital_detail_kline")
public class CompanyCapitalDetailKlineEntity {

    /** 公司名字 */
    private String companyName;

    /** 公司代码 */
    private String companyCode;

    /** 记录时间 */
    private String dateHis;

    /** 记录具体时间 */
    private String klineDate;

    /**当天价格*/
 //   private String price;



    /**当天主力净流入及占比*/
    private String netAmount;

    /**当天超大单净流入及占比*/
    private String superLargeOrderAmount;

    /**当天大单净流入及占比*/
    private String bigBillAmount;

    /**当天中单净流入及占比*/
    private String middleOrderAmount;

    /**当天小单净流入及占比*/
    private String littleOrderAmount;

}
