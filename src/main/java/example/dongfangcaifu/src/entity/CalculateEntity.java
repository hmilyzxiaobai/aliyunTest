package example.dongfangcaifu.src.entity;

import com.baomidou.mybatisplus.annotation.TableName;
 import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;


/**
 * 每条消息记录
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
@TableName("calculate")
public class CalculateEntity   {

	private static final long serialVersionUID = 1L;

	public static final String COMPANY_CODE = "company_code";
	public static final String TITLE = "title";
	public static final String CONTENT = "content";
	public static final String DISCUSS_SOURCE = "discuss_source";
	public static final String DF = "df";
	public static final String DM = "dm";
	public static final String IS_DELETED = "is_deleted";

    /** 爬取评论的url */
	private String companyCode;

    /** 标题 */
	private String title;

    /** 新闻内容 */
	private String content;

    /** 评论来源 1-个人 2-机构 3-政府 */
	private String discussSource;

    /** 时间df分区 */
	private String df;

    /** 时间dm分区 分钟存储 */
	private String dm;

    /** 是否删除 0- 否 1-删除 */
	private Integer isDeleted;

}
