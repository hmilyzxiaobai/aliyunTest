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
@TableName("send_his")
public class SendHisEntity {
    private static final long serialVersionUID = 1L;

    public static final String ID="id";
    @TableId
    private Long id;

    public static final String COMPANY_NAME = "company_name";
    public static final String MESSAGE = "message";
    public static final String COMPANY_CODE = "company_code";
    public static final String DF = "df";
    public static final String SEND_TYPE="type";
    public static final String SEND_STATUS="send_status";
    public static final String TO_EMAIL="to_email";


    private String companyCode;
    private String companyName;
    private String message;
    private String df;

    //0 正常发送 1钩子信息
    private Integer sendType;

    private Integer sendStatus;

    private String toEmail;

}
