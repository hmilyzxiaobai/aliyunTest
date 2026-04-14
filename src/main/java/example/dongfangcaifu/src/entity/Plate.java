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
@TableName("plate")
public class Plate {
    private static final long serialVersionUID = 1L;

    public static final String ID = "id";

    public static final String CONCEPT_NAME = "concept_name";
    public static final String CONCEPT_CODE = "concept_code";
    public static final String IS_LIVE = "is_live";
    public static final String DF_START = "df_start";
    public static final String DF_END = "df_end";

    @TableId
    private Long id;
    private String conceptName;
    private String conceptCode;
    private String isLive;
    private String dfStart;
    private String dfEnd;



}
