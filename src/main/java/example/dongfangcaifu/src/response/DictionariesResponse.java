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
 * 字典表 对应的行业编码 响应对象
 *
 * @author Ã¨Â®Â¸Ã¥ÂÂÃ¨Â±Âª xuzhuohao@gongpin.com
 * @since 1.0.0 2024-04-29
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "字典表 对应的行业编码 响应对象")
public class DictionariesResponse implements Serializable {

    private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "自增主键", position = 1)
	private Long id;

	@ApiModelProperty(value = "行业中文名", position = 2)
	private String business;

	@ApiModelProperty(value = "公司code", position = 3)
	private String businessCode;

	@ApiModelProperty(value = "国家政策趋势 0-弱 10 强", position = 4)
	private String countryTrend;

	@ApiModelProperty(value = "世界发展趋势 0-弱 10-强", position = 5)
	private String worldTrend;

	@ApiModelProperty(value = "创建时间", position = 6)
	private LocalDateTime createTime;

	@ApiModelProperty(value = "修改时间", position = 7)
	private LocalDateTime updateTime;

	@ApiModelProperty(value = "是否删除 0- 否 1-删除", position = 8)
	private Integer isDeleted;

}
