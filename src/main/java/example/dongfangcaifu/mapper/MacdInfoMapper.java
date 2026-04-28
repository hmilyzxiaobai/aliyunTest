package example.dongfangcaifu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import example.dongfangcaifu.src.entity.MacdInfoEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Mapper
public interface MacdInfoMapper extends BaseMapper<MacdInfoEntity> {

    /**
     * 根据公司代码查询最新MACD数据
     */
    @Select("SELECT * FROM macd_info WHERE company_code = #{companyCode} AND is_deleted = 0 ORDER BY df DESC LIMIT 1")
    MacdInfoEntity selectLatestByCompanyCode(@Param("companyCode") String companyCode);

    /**
     * 查询某公司MACD历史趋势
     */
    @Select("SELECT * FROM macd_info WHERE company_code = #{companyCode} AND is_deleted = 0 ORDER BY df ASC")
    List<MacdInfoEntity> selectHistoryByCompanyCode(@Param("companyCode") String companyCode);

    /**
     * 查询金叉信号（DIF上穿DEA）
     */
    @Select("SELECT * FROM macd_info WHERE is_deleted = 0 AND df = #{df} AND " +
            "CAST(dif AS DECIMAL(10,4)) > CAST(dea AS DECIMAL(10,4)) " +
            "ORDER BY net_amount DESC")
    List<MacdInfoEntity> selectGoldenCross(@Param("df") String df);

    /**
     * 查询死叉信号（DIF下穿DEA）
     */
    @Select("SELECT * FROM macd_info WHERE is_deleted = 0 AND df = #{df} AND " +
            "CAST(dif AS DECIMAL(10,4)) < CAST(dea AS DECIMAL(10,4)) " +
            "ORDER BY net_amount DESC")
    List<MacdInfoEntity> selectDeathCross(@Param("df") String df);

    /**
     * 统计各市场的MACD数据分布
     */
    @Select("SELECT market, COUNT(*) as count, AVG(CAST(macd AS DECIMAL(10,4))) as avg_macd " +
            "FROM macd_info WHERE is_deleted = 0 AND df = #{df} GROUP BY market")
    List<Map<String, Object>> selectMarketStats(@Param("df") String df);

    /**
     * 批量更新资金净流入
     */
    @Select("UPDATE macd_info SET net_amount = #{netAmount} WHERE company_code = #{companyCode} AND df = #{df}")
    int updateNetAmount(@Param("companyCode") String companyCode,
                        @Param("df") String df,
                            @Param("netAmount") BigDecimal netAmount);

    @Select("SELECT df, price, dif, dea, macd FROM macd_info " +
            "WHERE company_code = #{companyCode} AND is_deleted = 0 " +
            "ORDER BY df DESC LIMIT #{days}")
    List<Map<String, Object>> selectRecentMacd(@Param("companyCode") String companyCode,
                                               @Param("days") int days);

    @Select("<script>" +
            "SELECT company_code, df, price, dif, dea, macd FROM (" +
            "  SELECT company_code, df, price, dif, dea, macd," +
            "    ROW_NUMBER() OVER (PARTITION BY company_code ORDER BY df DESC) AS rn " +
            "  FROM macd_info " +
            "  WHERE company_code IN " +
            "  <foreach collection='companyCodes' item='code' open='(' separator=',' close=')'>" +
            "    #{code} " +
            "  </foreach> " +
            "  AND is_deleted = 0 " +
            ") t WHERE rn &lt;= #{days} " +
            "ORDER BY company_code, df DESC" +
            "</script>")
    List<Map<String, Object>> selectBatchRecentMacd(@Param("companyCodes") List<String> companyCodes,
                                                    @Param("days") int days);
}