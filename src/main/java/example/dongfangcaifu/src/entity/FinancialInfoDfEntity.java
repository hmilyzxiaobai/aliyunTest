package example.dongfangcaifu.src.entity;

import com.baomidou.mybatisplus.annotation.TableName;
 import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;


/**
 * 每日落库  df 分区
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
@TableName("financial_info_df")
public class FinancialInfoDfEntity   {

	private static final long serialVersionUID = 1L;

	public static final String COMPANY_CODE = "company_code";
	public static final String COMPANY_NAME = "company_name";
	public static final String BUSINESS_CODE_LIST = "business_code_list";
	public static final String START_PRICE = "start_price";
	public static final String END_PRICE = "end_price";
	public static final String YESTERDAY_PRICE = "yesterday_price";
	public static final String EARNINGS_RATE = "earnings_rate";
	public static final String TOP_INCREASE = "top_increase";
	public static final String LOW_INCREASE = "low_increase";
	public static final String TOP_AMOUNT = "top_amount";
	public static final String LOW_AMOUNT = "low_amount";
	public static final String FLOW_INTO = "flow_into";
	public static final String FLOW_OUT = "flow_out";
	public static final String JOURNALISM_COEFFICIENT = "journalism_coefficient";
	public static final String POLICY_COEFFICIENT = "policy_coefficient";
	public static final String DISCUSS_TREND = "discuss_trend";
	public static final String DF = "df";
	public static final String IS_DELETED = "is_deleted";

    /** 公司代码 */
	private String companyCode;

    /** 公司名字 */
	private String companyName;

    /** 当天关联的行业 */
	private String businessCodeList;

    /** 开盘价 */
	private String startPrice;

    /** 收盘价 */
	private String endPrice;

    /** 昨天收盘价格 */
	private String yesterdayPrice;

    /** 盈利率 */
	private String earningsRate;

    /** 最高增加 百分比 */
	private String topIncrease;

    /** 最低减少 百分比 */
	private String lowIncrease;

    /** 最高增加额度 */
	private String topAmount;

    /** 最低增加额度 */
	private String lowAmount;

    /** 流入资金 */
	private String flowInto;

    /** 流出资金 */
	private String flowOut;

    /** 新闻影响系数  0-弱 1-强 */
	private String journalismCoefficient;

    /** 政策影响系数 0-弱 1-强 */
	private String policyCoefficient;

    /** 网友讨论正相关系数 */
	private Integer discussTrend;

    /** 时间df分区 */
	private String df;

    /** 是否删除 0- 否 1-删除 */
	private Integer isDeleted;

}
