package example.dongfangcaifu.src.entity.institutional;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper=false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("institutional_info")
public class InstitutionalInfoEntity {

    private String institutionalCode;

    private String institutionalName;
}
