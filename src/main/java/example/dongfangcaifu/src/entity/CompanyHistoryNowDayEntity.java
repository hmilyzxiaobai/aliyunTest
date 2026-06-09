package example.dongfangcaifu.src.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.io.Serializable;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper=false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("company_history_now_day")
public class CompanyHistoryNowDayEntity implements Serializable {

    private static final long serialVersionUID = 1L;


    public static final String COMPANY_NAME = "company_name";
    public static final String COMPANY_CODE = "company_code";
    public static final String DATE_HIS = "date_his";
    public static final String CHANGE_DETAILS = "change_details";
    public static final String IS_DELETED = "is_deleted";
    public static final String PRICE = "price";
    public static final String HIGH_PRICE = "high_price";
    public static final String LOW_PRICE = "low_price";
    public static final String TURNOVER_RATE = "turnover_rate";

    public static final String TRADING_VOLUME = "trading_volume";
    public static final String VOLUME_OF_TRANSACTION = "volume_of_transaction";


    /** 公司名字 */
    private String companyName;

    private String highPrice;
    private String lowPrice;

    /** 公司代码 */
    private String companyCode;

    /** 记录时间 */
    private String dateHis;

    /** 浮动百分比 */
    private String changeDetails;

    /** 是否删除 0- 否 1-删除 */
    private Integer isDeleted;

    private String price;
    /**换手率*/
    private String turnoverRate;

    /**成交量*/
    private String tradingVolume;
    /**成交额*/
    private String volumeOfTransaction;

    private String createTime;

}
