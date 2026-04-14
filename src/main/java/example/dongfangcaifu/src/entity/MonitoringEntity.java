package example.dongfangcaifu.src.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper=false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("monitoring")
public class MonitoringEntity {

    /** 公司名字 */
    private String companyName;

    /** 公司代码 */
    private String companyCode;

    /** 记录时间 */
    private String dateHis;

    /** 浮动百分比 */
    private String changeDetails;

    /**当天价格*/
    private String price;

    private String type;




}
