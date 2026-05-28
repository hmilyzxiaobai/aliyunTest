package example.dongfangcaifu.src.entity.institutional;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper=false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("institutional_purchase")
public class InstitutionalPurchaseEntity {

    private String institutionalCode;

    private String institutionalName;

    private String tradeDate;

    private String d1CloseAdjchrate;
    private String d2CloseAdjchrate;
    private String d3CloseAdjchrate;
    private String d5CloseAdjchrate;
    private String d10CloseAdjchrate;
    private String d20CloseAdjchrate;
    private String d30CloseAdjchrate;


    private String actBuy;
    private String actSell;
    private String netAmt;

    private String companyName;
    private String companyCode;

    private String explanation;
}
