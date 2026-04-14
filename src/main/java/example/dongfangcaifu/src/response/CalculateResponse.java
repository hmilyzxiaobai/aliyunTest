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
 * 每条消息记录 响应对象
 *
 * @author Ã¨Â®Â¸Ã¥ÂÂÃ¨Â±Âª xuzhuohao@gongpin.com
 * @since 1.0.0 2024-04-29
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "每条消息记录 响应对象")
public class CalculateResponse implements Serializable {

    private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "自增主键", position = 1)
	private Long id;

	@ApiModelProperty(value = "爬取评论的url", position = 2)
	private String companyCode;

	@ApiModelProperty(value = "标题", position = 3)
	private String title;

	@ApiModelProperty(value = "新闻内容", position = 4)
	private String content;

	@ApiModelProperty(value = "评论来源 1-个人 2-机构 3-政府", position = 5)
	private String discussSource;

	@ApiModelProperty(value = "时间df分区", position = 6)
	private String df;

	@ApiModelProperty(value = "时间dm分区 分钟存储", position = 7)
	private String dm;

	@ApiModelProperty(value = "创建时间", position = 8)
	private LocalDateTime createTime;

	@ApiModelProperty(value = "修改时间", position = 9)
	private LocalDateTime updateTime;

	@ApiModelProperty(value = "是否删除 0- 否 1-删除", position = 10)
	private Integer isDeleted;

}
