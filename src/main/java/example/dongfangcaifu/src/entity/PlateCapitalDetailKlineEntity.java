package example.dongfangcaifu.src.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper=false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("plate_capital_detail_kline")
public class PlateCapitalDetailKlineEntity {

    /** 板块名字 */
    private String conceptName;

    /** 板块代码 */
    private String conceptCode;

    /** 记录时间 */
    private String dateHis;

    /** 记录具体时间 */
    private String klineDate;

    /**当天价格*/
    private String price;

    /**主板或者概念股*/
    private String bkType;

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
