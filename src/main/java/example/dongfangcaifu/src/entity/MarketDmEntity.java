package example.dongfangcaifu.src.entity;

import com.baomidou.mybatisplus.annotation.TableName;
 import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;


/**
 * 每一分钟大盘的数据
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
@TableName("market_dm")
public class MarketDmEntity   {

	private static final long serialVersionUID = 1L;

	public static final String APPEAR_MARKET = "appear_market";
	public static final String START_PRICE = "start_price";
	public static final String NOW_PRICE = "now_price";
	public static final String INCREASE = "increase";
	public static final String INCREASE_AMOUNT = "increase_amount";
	public static final String DM = "dm";
	public static final String DF = "df";
	public static final String IS_DELETED = "is_deleted";

    /** 股票代码 */
	private String appearMarket;

    /** 开盘价格 */
	private String startPrice;

    /** 当前价格 */
	private String nowPrice;

    /** 涨幅 百分比 负数为减少 */
	private String increase;

    /** 涨跌额 */
	private String increaseAmount;

    /** 时间dm分区 分钟存储 */
	private String dm;

    /** 时间df分区 */
	private String df;

    /** 是否删除 0- 否 1-删除 */
	private Integer isDeleted;

}
