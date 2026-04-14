package example.dongfangcaifu.src.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

import java.time.LocalDateTime;

/**
 * 每天的dm 落库存储表
 *
 * @author Ã¨Â®Â¸Ã¥ÂÂÃ¨Â±Âª xuzhuohao@gongpin.com
 * @since 1.0.0 2024-04-29
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "每天的dm 落库存储表")
public class FinancialInfoDmDTO implements Serializable {

    private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "自增主键", position = 1)
	private Long id;

	@ApiModelProperty(value = "公司代码", position = 2)
	private String companyCode;

	@ApiModelProperty(value = "公司名字", position = 3)
	private String companyName;

	@ApiModelProperty(value = "当天关联的行业", position = 4)
	private String businessCodeList;

	@ApiModelProperty(value = "开盘价", position = 5)
	private String startPrice;

	@ApiModelProperty(value = "最低价格", position = 6)
	private String lowPrice;

	@ApiModelProperty(value = "昨天收盘价格", position = 7)
	private String yesterdayPrice;

	@ApiModelProperty(value = "当前增幅 百分比 ", position = 8)
	private String earnings;

	@ApiModelProperty(value = "最高增加 百分比", position = 9)
	private String topIncrease;

	@ApiModelProperty(value = "最低减少 百分比", position = 10)
	private String lowIncrease;

	@ApiModelProperty(value = "最高增加额度", position = 11)
	private String topAmount;

	@ApiModelProperty(value = "最低增加额度", position = 12)
	private String lowAmount;

	@ApiModelProperty(value = "同时大盘的涨幅", position = 13)
	private String marketIncrease;

	@ApiModelProperty(value = "政策影响系数 0-弱 1-强", position = 14)
	private String policyCoefficient;

	@ApiModelProperty(value = "网友讨论正相关系数", position = 15)
	private Integer discussTrend;

	@ApiModelProperty(value = "时间dm分区", position = 16)
	private String dm;

	@ApiModelProperty(value = "时间df分区", position = 17)
	private String df;

	@ApiModelProperty(value = "创建时间", position = 18)
	private LocalDateTime createTime;

	@ApiModelProperty(value = "修改时间", position = 19)
	private LocalDateTime updateTime;

	@ApiModelProperty(value = "是否删除 0- 否 1-删除", position = 20)
	private Integer isDeleted;


}
