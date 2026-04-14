package example.dongfangcaifu.src.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
 import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.io.Serializable;
import java.util.Date;


/**
 * 每天的dm 落库存储表
 *
 * @author Ã¨Â®Â¸Ã¥ÂÂÃ¨Â±Âª xuzhuohao@gongpin.com
 * @since 1.0.0 2024-04-29
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper=false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("financial_info_dm")
public class FinancialInfoDmEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String COMPANY_CODE = "company_code";
	//public static final String ID = "id";
	public static final String COMPANY_NAME = "company_name";
	public static final String BUSINESS_CODE_LIST = "business_code_list";
	public static final String START_PRICE = "start_price";
	public static final String LOW_PRICE = "low_price";
	public static final String YESTERDAY_PRICE = "yesterday_price";
	public static final String EARNINGS = "earnings";
	public static final String TOP_INCREASE = "top_increase";
	public static final String LOW_INCREASE = "low_increase";
	public static final String TOP_AMOUNT = "top_amount";
	public static final String CAPITAL_NOW = "capital_now";
	public static final String TOTAL_CAPITAL = "total_capital";
	public static final String PROPORTION = "proportion";
	public static final String DISCUSS_TREND = "discuss_trend";
	public static final String DM = "dm";
	public static final String DF = "df";
	public static final String IS_DELETED = "is_deleted";
	public static final String NOW_PRICE = "now_price";
	public static final String CREATE_TIME = "create_time";
	public static final String TURNOVER_RATE = "turnover_rate";
	public static final String TRADING_VOLUME = "trading_volume";
	public static final String VOLUME_OF_TRANSACTION = "volume_of_transaction";

	private Date createTime;

	//@Id
	//@GeneratedValue(strategy = GenerationType.IDENTITY) // 或使用其他适合的策略
	//private String id;


    /** 公司代码 */
	private String companyCode;

    /** 公司名字 */
	private String companyName;

    /** 当天关联的行业 */
	private String businessCodeList;

	private String nowPrice;
    /** 开盘价 */
	private String startPrice;

    /** 最低价格 */
	private String lowPrice;

    /** 昨天收盘价格 */
	private String yesterdayPrice;

    /** 当前增幅 百分比  */
	private String earnings;

    /** 最高增加 百分比  最高的价格 */
	private String topIncrease;

    /** 最低减少 百分比 */
	private String lowIncrease;

    /** 最高增加额度 */
	private String topAmount;

    /** 净流入 */
	private String capitalNow;

    /** 同时大盘的涨幅 */
	private String totalCapital;

    /** 政策影响系数 0-弱 1-强 */
	private String proportion;

    /** 网友讨论正相关系数 */
	private Integer discussTrend;

    /** 时间dm分区 */
	private String dm;

    /** 时间df分区 */
	private String df;

    /** 是否删除 0- 否 1-删除 */
	private Integer isDeleted;

	/**换手率*/
	private String turnoverRate;

	/**成交量*/
	private String tradingVolume;
	/**成交额*/
	private String volumeOfTransaction;


}
