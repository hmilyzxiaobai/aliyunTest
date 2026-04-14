package example.dongfangcaifu.src.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;


/**
 * 中间表  公司信息和行业表
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
@TableName("bridge_company_dic")
public class BridgeCompanyDicEntity  {

	private static final long serialVersionUID = 1L;

	public static final String COMPANY_CODE = "company_code";
	public static final String BUSINESS_CODE = "business_code";
	public static final String RELATIONAL_DEGREE = "relational_degree";
	public static final String DF = "df";
	public static final String IS_DELETED = "is_deleted";

    /** 公司代码 */
	private String companyCode;

    /** 字典代码 */
	private String businessCode;

    /** 行业关联程度 0-10 0弱 10 强 */
	private String relationalDegree;

    /** 时间df分区 */
	private String df;

    /** 是否删除 0- 否 1-删除 */
	private Integer isDeleted;

}
