package example.dongfangcaifu.src.entity;

import com.baomidou.mybatisplus.annotation.TableName;
 import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;


/**
 * 字典表 对应的行业编码
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
@TableName("dictionaries")
public class DictionariesEntity   {

	private static final long serialVersionUID = 1L;

	public static final String BUSINESS = "business";
	public static final String BUSINESS_CODE = "business_code";
	public static final String COUNTRY_TREND = "country_trend";
	public static final String WORLD_TREND = "world_trend";
	public static final String IS_DELETED = "is_deleted";

    /** 行业中文名 */
	private String business;

    /** 公司code */
	private String businessCode;

    /** 国家政策趋势 0-弱 10 强 */
	private String countryTrend;

    /** 世界发展趋势 0-弱 10-强 */
	private String worldTrend;

    /** 是否删除 0- 否 1-删除 */
	private Integer isDeleted;

}
