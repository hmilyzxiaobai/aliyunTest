package example.dongfangcaifu.src.entity;

import com.baomidou.mybatisplus.annotation.TableName;
 import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;


/**
 * 每天爬虫的url汇总以及相关url信息
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
@TableName("reptile_url")
public class ReptileUrlEntity   {

	private static final long serialVersionUID = 1L;

	public static final String URL = "url";
	public static final String URL_TYPE = "url_type";
	public static final String BUSINESS_CODE = "business_code";
	public static final String BUSINESS_TREND = "business_trend";
	public static final String COMPANY_CODE = "company_code";
	public static final String COMPANY_TREND = "company_trend";
	public static final String DF = "df";
	public static final String IS_DELETED = "is_deleted";

    /** 链接 */
	private String url;

    /** 链接类型——0-国家政策-1-财经新闻 2-媒体报道 4-网友评论  5-证监会公告 6-公司公告（财报） */
	private Integer urlType;

    /** 关联行业 */
	private String businessCode;

    /** 关联行业程度 0-弱 10-强 */
	private Integer businessTrend;

    /** 关联公司 */
	private String companyCode;

    /** 关联公司程度 0-弱 10-强 */
	private Integer companyTrend;

    /** 时间df分区 */
	private String df;

    /** 是否删除 0- 否 1-删除 */
	private Integer isDeleted;

}
