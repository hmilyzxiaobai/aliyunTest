package example.dongfangcaifu.src.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper=false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("company_other")
public class CompanyOtherEntity {

    private static final long serialVersionUID = 1L;

    private static final String ID="id";

    public static final String COMPANY_NAME = "company_name";
    public static final String COMPANY_CODE = "company_code";
    public static final String RECENT_MIN = "recent_min";
    public static final String RECENT_AVG = "recent_avg";
    public static final String IS_DELETED = "is_deleted";
    public static final String RECENT_CAPITAL = "recent_capital";
    public static final String RECENT_HIGH="recent_high";

    @TableId
    private Long id;


    private String companyCode;
    private String companyName;
    private String recentMin;
    private String recentAvg;
    private String recentCapital;
    private String recentHigh;


}
