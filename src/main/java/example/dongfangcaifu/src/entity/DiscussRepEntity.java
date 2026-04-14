package example.dongfangcaifu.src.entity;

import com.baomidou.mybatisplus.annotation.TableName;
 import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;


/**
 * 每条评论消息落库 df 分区
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
@TableName("discuss_rep")
public class DiscussRepEntity   {

	private static final long serialVersionUID = 1L;

	public static final String URL = "url";
	public static final String REPORT_ID = "report_id";
	public static final String COMPANY_NAME = "company_name";
	public static final String DISCUSS_COMMENT = "discuss_comment";
	public static final String DISCUSS_SOURCE = "discuss_source";
	public static final String INFLUENCE = "influence";
	public static final String INFLUENCE_COMPANY_CODE = "influence_company_code";
	public static final String INFLUENCE_BUSINESS_CODE = "influence_business_code";
	public static final String DF = "df";
	public static final String IS_DELETED = "is_deleted";

    /** 爬取评论的url */
	private String url;

    /** 新闻报道的id */
	private String reportId;

    /** 公司名称 */
	private String companyName;

    /** 评论的内容 */
	private String discussComment;

    /** 评论来源 1-个人 2-机构 3-政府 */
	private String discussSource;

    /** 影响趋势 -10 --- 10 负数为负面影响 正数为正面影响 */
	private String influence;

    /** 影响公司的code */
	private String influenceCompanyCode;

    /** 影响行业的code */
	private String influenceBusinessCode;

    /** 时间df分区 */
	private String df;

    /** 是否删除 0- 否 1-删除 */
	private Integer isDeleted;

}
