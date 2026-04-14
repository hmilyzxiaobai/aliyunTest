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
 * 主表 公司信息
 *
 * @author Ã¨Â®Â¸Ã¥ÂÂÃ¨Â±Âª xuzhuohao@gongpin.com
 * @since 1.0.0 2024-04-29
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "主表 公司信息")
public class CompanyInfoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "自增主键", position = 1)
	private Long id;

	@ApiModelProperty(value = "公司名字", position = 2)
	private String companyName;

	@ApiModelProperty(value = "公司性质", position = 3)
	private String companyNature;

	@ApiModelProperty(value = "公司代码", position = 4)
	private String companyCode;

	@ApiModelProperty(value = "上市区域", position = 5)
	private String appearMarket;

	@ApiModelProperty(value = "上市时间", position = 6)
	private String registrationDate;

	@ApiModelProperty(value = "是否在市", position = 7)
	private String inMarket;

	@ApiModelProperty(value = "创建时间", position = 8)
	private LocalDateTime createTime;

	@ApiModelProperty(value = "修改时间", position = 9)
	private LocalDateTime updateTime;

	@ApiModelProperty(value = "是否删除 0- 否 1-删除", position = 10)
	private Integer isDeleted;


}
