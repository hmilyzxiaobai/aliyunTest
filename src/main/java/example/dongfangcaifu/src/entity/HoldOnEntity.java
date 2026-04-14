package example.dongfangcaifu.src.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.models.auth.In;
import lombok.*;

@Data
//@ToString(callSuper = true)
//@EqualsAndHashCode(callSuper=false)
//@Builder
//@NoArgsConstructor
//@AllArgsConstructor

@TableName("hold_on")
public class HoldOnEntity {

    private static final long serialVersionUID = 1L;
    public static final String COMPANY_NAME = "company_name";
    public static final String COMPANY_CODE = "company_code";
    public static final String HOLD_ON_AMOUNT = "hold_on_amount";
    public static final String HOLD_ON_PRICE = "hold_on_price";
    public static final String IS_SELL = "is_sell";
    public static final String SELL_PRICE = "sell_price";
    public static final String UPDATE_TIME = "update_time";
    public static final String CREATE_TIME = "create_time";
    public static final String DF = "df";


    private String df;

    private String updateTime;
    private String createTime;
    @TableId
    private Long id;

    /** 公司名字 */
    private String companyName;
    /** 公司代码 */
    private String companyCode;

    private String holdOnAmount;

    private String holdOnPrice;

    private String isSell;

    private String sellPrice;

    /** 是否删除 0- 否 1-删除 */
    private Integer isDeleted;

}
