package example.dongfangcaifu.src.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

@Data
@TableName("capital_flow_history")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper=false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CapitalFlowHistoryEntity {

    private static final long serialVersionUID = 1L;

    public static final String COMPANY_NAME = "company_name";
    public static final String COMPANY_CODE = "company_code";
    public static final String CAPITAL = "capital";
    public static final String DATE_HIS = "date_his";

    public static final String CHANGE_DETAIL  = "change_detail";
    public static final String IS_DELETED = "is_deleted";
    public static final String TOTAL_CAPITAL = "total_capital";
    public static final String PROPORTION = "proportion";
/*
`capital` varchar(20) DEFAULT NULL COMMENT '当天资金流入情况',
  `total_capital` varchar(20) DEFAULT '' COMMENT '总资金',
  `proportion` varchar(20) DEFAULT '' COMMENT '流入占比',
 */
    /** 公司名字 */
    private String companyName;


    /** 公司代码 */
    private String companyCode;

    /** 记录时间 */
    private String dateHis;

    /** 浮动百分比 */
    private String changeDetail;

    /** 是否删除 0- 否 1-删除 */
    private Integer isDeleted;

    private String capital;
    private String totalCapital;
    private String proportion;

}
