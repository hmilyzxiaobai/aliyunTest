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

    /**
     CREATE TABLE `institutional_purchase` (
     `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '自增主键',
     `institutional_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '机构代码',
     `institutional_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '机构名字',
     `trade_date` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '买入日期',
     `d1_close_adjchrate` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '上榜后第一天涨幅',
     `d2_close_adjchrate` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '上榜后二天涨幅',
     `d3_close_adjchrate` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '上榜后三天涨幅',
     `d5_close_adjchrate` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '上榜后五天涨幅',
     `d10_close_adjchrate` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '上榜后十天涨幅',
     `d20_close_adjchrate` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '上榜后二十天涨幅',
     `d30_close_adjchrate` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '上榜后三十天涨幅',
     `company_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '买入的股票代码',
     `company_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '买入的股票名称',
     `act_buy` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '实际买入金额',
     `act_sell` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '实际卖出金额',
     `net_amt` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '净买入金额',
     `explanation` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '买入理由',
     `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
     `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
     `is_deleted` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除 0- 否 1-删除',
     PRIMARY KEY (`id`) USING BTREE
     ) ENGINE=InnoDB AUTO_INCREMENT=22155618 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='记录机构买入买进和涨幅清空';
     */
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
