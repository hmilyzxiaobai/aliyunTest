package example.dongfangcaifu.src.entity;

import com.baomidou.mybatisplus.annotation.TableName;
 import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;


/**
 * 每天大盘的数据 按天保存
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
@TableName("market_df")
public class MarketDfEntity   {

	private static final long serialVersionUID = 1L;

	public static final String APPEAR_MARKET = "appear_market";
	public static final String START_PRICE = "start_price";
	public static final String END_PRICE = "end_price";
	public static final String INCREASE = "increase";
	public static final String INCREASE_AMOUNT = "increase_amount";
	public static final String TOP_INCREASE = "top_increase";
	public static final String LOW_INCREASE = "low_increase";
	public static final String DF = "df";
	public static final String IS_DELETED = "is_deleted";

    /** 股票代码 */
	private String appearMarket;

    /** 开盘价格 */
	private String startPrice;

    /** 收盘价格 */
	private String endPrice;

    /** 涨幅 百分比 负数为减少 */
	private String increase;

    /** 涨跌额 */
	private String increaseAmount;

    /** 最高点涨幅 */
	private String topIncrease;

    /** 最低点涨幅 */
	private String lowIncrease;

    /** 时间df分区 */
	private String df;

    /** 是否删除 0- 否 1-删除 */
	private Integer isDeleted;

}
