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
 * 每天爬虫的url汇总以及相关url信息 响应对象
 *
 * @author Ã¨Â®Â¸Ã¥ÂÂÃ¨Â±Âª xuzhuohao@gongpin.com
 * @since 1.0.0 2024-04-29
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "每天爬虫的url汇总以及相关url信息 响应对象")
public class ReptileUrlResponse implements Serializable {

    private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "自增主键", position = 1)
	private Long id;

	@ApiModelProperty(value = "链接", position = 2)
	private String url;

	@ApiModelProperty(value = "链接类型——0-国家政策-1-财经新闻 2-媒体报道 4-网友评论  5-证监会公告 6-公司公告（财报）", position = 3)
	private Integer urlType;

	@ApiModelProperty(value = "关联行业", position = 4)
	private String businessCode;

	@ApiModelProperty(value = "关联行业程度 0-弱 10-强", position = 5)
	private Integer businessTrend;

	@ApiModelProperty(value = "关联公司", position = 6)
	private String companyCode;

	@ApiModelProperty(value = "关联公司程度 0-弱 10-强", position = 7)
	private Integer companyTrend;

	@ApiModelProperty(value = "时间df分区", position = 8)
	private String df;

	@ApiModelProperty(value = "创建时间", position = 9)
	private LocalDateTime createTime;

	@ApiModelProperty(value = "修改时间", position = 10)
	private LocalDateTime updateTime;

	@ApiModelProperty(value = "是否删除 0- 否 1-删除", position = 11)
	private Integer isDeleted;

}
