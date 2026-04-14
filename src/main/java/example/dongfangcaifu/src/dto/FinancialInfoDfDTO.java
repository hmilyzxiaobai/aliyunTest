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
 * 每日落库  df 分区
 *
 * @author Ã¨Â®Â¸Ã¥ÂÂÃ¨Â±Âª xuzhuohao@gongpin.com
 * @since 1.0.0 2024-04-29
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "每日落库  df 分区")
public class FinancialInfoDfDTO implements Serializable {

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

	@ApiModelProperty(value = "收盘价", position = 6)
	private String endPrice;

	@ApiModelProperty(value = "昨天收盘价格", position = 7)
	private String yesterdayPrice;

	@ApiModelProperty(value = "盈利率", position = 8)
	private String earningsRate;

	@ApiModelProperty(value = "最高增加 百分比", position = 9)
	private String topIncrease;

	@ApiModelProperty(value = "最低减少 百分比", position = 10)
	private String lowIncrease;

	@ApiModelProperty(value = "最高增加额度", position = 11)
	private String topAmount;

	@ApiModelProperty(value = "最低增加额度", position = 12)
	private String lowAmount;

	@ApiModelProperty(value = "流入资金", position = 13)
	private String flowInto;

	@ApiModelProperty(value = "流出资金", position = 14)
	private String flowOut;

	@ApiModelProperty(value = "新闻影响系数  0-弱 1-强", position = 15)
	private String journalismCoefficient;

	@ApiModelProperty(value = "政策影响系数 0-弱 1-强", position = 16)
	private String policyCoefficient;

	@ApiModelProperty(value = "网友讨论正相关系数", position = 17)
	private Integer discussTrend;

	@ApiModelProperty(value = "时间df分区", position = 18)
	private String df;

	@ApiModelProperty(value = "创建时间", position = 19)
	private LocalDateTime createTime;

	@ApiModelProperty(value = "修改时间", position = 20)
	private LocalDateTime updateTime;

	@ApiModelProperty(value = "是否删除 0- 否 1-删除", position = 21)
	private Integer isDeleted;


}
