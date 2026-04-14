package example.dongfangcaifu.src.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

import java.time.LocalDateTime;

/**
 * 每天大盘的数据 按天保存 响应对象
 *
 * @author Ã¨Â®Â¸Ã¥ÂÂÃ¨Â±Âª xuzhuohao@gongpin.com
 * @since 1.0.0 2024-04-29
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "每天大盘的数据 按天保存 响应对象")
public class MarketDfResponse implements Serializable {

    private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "自增主键", position = 1)
	private Long id;

	@ApiModelProperty(value = "股票代码", position = 2)
	private String appearMarket;

	@ApiModelProperty(value = "开盘价格", position = 3)
	private String startPrice;

	@ApiModelProperty(value = "收盘价格", position = 4)
	private String endPrice;

	@ApiModelProperty(value = "涨幅 百分比 负数为减少", position = 5)
	private String increase;

	@ApiModelProperty(value = "涨跌额", position = 6)
	private String increaseAmount;

	@ApiModelProperty(value = "最高点涨幅", position = 7)
	private String topIncrease;

	@ApiModelProperty(value = "最低点涨幅", position = 8)
	private String lowIncrease;

	@ApiModelProperty(value = "时间df分区", position = 9)
	private String df;

	@ApiModelProperty(value = "创建时间", position = 10)
	private LocalDateTime createTime;

	@ApiModelProperty(value = "修改时间", position = 11)
	private LocalDateTime updateTime;

	@ApiModelProperty(value = "是否删除 0- 否 1-删除", position = 12)
	private Integer isDeleted;

}
