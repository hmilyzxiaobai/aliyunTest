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
 * 中间表  公司信息和行业表
 *
 * @author Ã¨Â®Â¸Ã¥ÂÂÃ¨Â±Âª xuzhuohao@gongpin.com
 * @since 1.0.0 2024-04-29
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "中间表  公司信息和行业表")
public class BridgeCompanyDicDTO implements Serializable {

    private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "自增主键", position = 1)
	private Long id;

	@ApiModelProperty(value = "公司代码", position = 2)
	private String companyCode;

	@ApiModelProperty(value = "字典代码", position = 3)
	private String businessCode;

	@ApiModelProperty(value = "行业关联程度 0-10 0弱 10 强", position = 4)
	private String relationalDegree;

	@ApiModelProperty(value = "时间df分区", position = 5)
	private String df;

	@ApiModelProperty(value = "创建时间", position = 6)
	private LocalDateTime createTime;

	@ApiModelProperty(value = "修改时间", position = 7)
	private LocalDateTime updateTime;

	@ApiModelProperty(value = "是否删除 0- 否 1-删除", position = 8)
	private Integer isDeleted;


}
