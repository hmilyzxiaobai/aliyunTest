package example.dongfangcaifu.src.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
 import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;


/**
 * 主表 公司信息
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
@TableName("company_info")
public class CompanyInfoEntity   {

	private static final long serialVersionUID = 1L;



	public static final String COMPANY_NAME = "company_name";
	public static final String PLATE_CODE = "plate_code";
	public static final String ID = "id";
	public static final String COMPANY_NATURE = "company_nature";
	public static final String COMPANY_CODE = "company_code";
	public static final String APPEAR_MARKET = "appear_market";
	public static final String REGISTRATION_DATE = "registration_date";
	public static final String IN_MARKET = "in_market";
	public static final String IS_DELETED = "is_deleted";
	public static final String PRESSURE = "pressure";
	public static final String SUPPORT = "support";


	@TableId
	private Long id;
    /** 公司名字 */
	private String companyName;

	private String plateCode;

    /** 公司性质 */
	private String companyNature;

    /** 公司代码 */
	private String companyCode;

    /** 上市区域 */
	private String appearMarket;

    /** 上市时间 */
	private String registrationDate;

    /** 是否在市 */
	private String inMarket;

    /** 是否删除 0- 否 1-删除 */
	private Integer isDeleted;

	// 压力位
	private String Pressure;

	// 支撑位
	private String Support;

}
