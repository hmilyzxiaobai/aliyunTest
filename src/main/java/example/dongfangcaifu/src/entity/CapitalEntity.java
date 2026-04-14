package example.dongfangcaifu.src.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper=false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("capital")
public class CapitalEntity {

    private static final long serialVersionUID = 1L;

    public static final String ID = "id";

    public static final String CAPITAL = "capital";

    private Integer id;

    /** 总资产 */
    private String capital;


    /** 是否删除 0- 否 1-删除 */
    private Integer isDeleted;
}
