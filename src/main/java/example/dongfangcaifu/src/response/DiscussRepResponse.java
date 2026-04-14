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
 * 每条评论消息落库 df 分区 响应对象
 *
 * @author Ã¨Â®Â¸Ã¥ÂÂÃ¨Â±Âª xuzhuohao@gongpin.com
 * @since 1.0.0 2024-04-29
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "每条评论消息落库 df 分区 响应对象")
public class DiscussRepResponse implements Serializable {

    private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "自增主键", position = 1)
	private Long id;

	@ApiModelProperty(value = "爬取评论的url", position = 2)
	private String url;

	@ApiModelProperty(value = "新闻报道的id", position = 3)
	private String reportId;

	@ApiModelProperty(value = "公司名称", position = 4)
	private String companyName;

	@ApiModelProperty(value = "评论的内容", position = 5)
	private String discussComment;

	@ApiModelProperty(value = "评论来源 1-个人 2-机构 3-政府", position = 6)
	private String discussSource;

	@ApiModelProperty(value = "影响趋势 -10 --- 10 负数为负面影响 正数为正面影响", position = 7)
	private String influence;

	@ApiModelProperty(value = "影响公司的code", position = 8)
	private String influenceCompanyCode;

	@ApiModelProperty(value = "影响行业的code", position = 9)
	private String influenceBusinessCode;

	@ApiModelProperty(value = "时间df分区", position = 10)
	private String df;

	@ApiModelProperty(value = "创建时间", position = 11)
	private LocalDateTime createTime;

	@ApiModelProperty(value = "修改时间", position = 12)
	private LocalDateTime updateTime;

	@ApiModelProperty(value = "是否删除 0- 否 1-删除", position = 13)
	private Integer isDeleted;

}
