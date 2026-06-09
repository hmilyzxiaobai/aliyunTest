package example.dongfangcaifu.src.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper=false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("plate_his")
public class PlateHis {
    private static final long serialVersionUID = 1L;

    public static final String CONCEPT_NAME = "concept_name";
    public static final String CONCEPT_CODE = "concept_code";
    public static final String PRICE = "price";
    public static final String CHANGE_PRICE = "change_price";
    public static final String CHANGE_PERCENT = "change_percent";
    public static final String ALL_VALUE = "all_value";
    public static final String TURNOVER_RATE = "turnover_rate";
    public static final String UP_AMOUNT = "up_amount";
    public static final String DOWN_AMOUNT = "down_amount";
    public static final String DF = "df";

    public static final String IS_DELETED = "is_deleted";

    private String conceptName;
    private String conceptCode;
    private String price;
    private String changePrice;
    private String changePercent;
    private String allValue;
    private String turnoverRate;
    private String upAmount;
    private String downAmount;
    private String df;
    private String createTime;
    /*

CREATE TABLE `plate` (
				  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '自增主键',
				  `concept_name` varchar(50) NOT NULL DEFAULT '' COMMENT '板块名字',
					`concept_code` varchar(50) NOT NULL DEFAULT '' COMMENT '板块代码',
				  `price` varchar(50) NOT NULL DEFAULT '' COMMENT '当前价格',
				  `change_price` varchar(50) NOT NULL DEFAULT '' COMMENT '涨跌额',
				 `change_percent`  varchar(50) NOT NULL DEFAULT '' COMMENT '涨幅度',
				  `all_value` varchar(50) NOT NULL DEFAULT '' COMMENT '总市值',
				  `turnover_rate` varchar(50) NOT NULL DEFAULT '' COMMENT '换手率',
				  `up_amount` varchar(50) NOT NULL DEFAULT '' COMMENT '上涨数量',
				  `down_amount` varchar(50) NOT NULL DEFAULT '' COMMENT '下跌数量',
				  `df` varchar(50) NOT NULL DEFAULT '' COMMENT '日期',
				  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
				  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
				  `is_deleted` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除 0- 否 1-删除',
				  PRIMARY KEY (`id`)
				) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='板块概念统计';

     */
}
