package example.dongfangcaifu.mapper;



import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import example.dongfangcaifu.src.entity.MonitoringEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.util.List;
import java.util.Map;

@Mapper
public interface MonitoringMapper extends BaseMapper<MonitoringEntity> {

    /**
     * 大涨大跌统计 - 各类型数量
     */
    @Select("<script>" +
            "SELECT " +
            "  SUM(CASE WHEN type = 1 THEN 1 ELSE 0 END) AS sh_up, " +
            "  SUM(CASE WHEN type = 2 THEN 1 ELSE 0 END) AS sz_up, " +
            "  SUM(CASE WHEN type = 3 THEN 1 ELSE 0 END) AS sh_down, " +
            "  SUM(CASE WHEN type = 4 THEN 1 ELSE 0 END) AS sz_down, " +
            "  SUM(CASE WHEN type = 5 THEN 1 ELSE 0 END) AS sz_large_up, " +
            "  SUM(CASE WHEN type = 6 THEN 1 ELSE 0 END) AS sz_large_down " +
            "FROM monitoring " +
            "WHERE is_deleted = 0 " +
            "  AND type IN (1,2,3,4,5,6) " +
            "<if test='startDate != null and startDate != \"\"'>" +
            "  AND date_his &gt;= #{startDate} " +
            "</if>" +
            "<if test='endDate != null and endDate != \"\"'>" +
            "  AND date_his &lt;= #{endDate} " +
            "</if>" +
            "</script>")
    Map<String, Object> selectStats(@Param("startDate") String startDate,
                                     @Param("endDate") String endDate);

    /**
     * 分页查询大涨大跌列表
     */
    @Select("<script>" +
            "SELECT * FROM monitoring " +
            "WHERE is_deleted = 0 " +
            "  AND type IN (1,2,3,4,5,6) " +
            "<if test='startDate != null and startDate != \"\"'>" +
            "  AND date_his &gt;= #{startDate} " +
            "</if>" +
            "<if test='endDate != null and endDate != \"\"'>" +
            "  AND date_his &lt;= #{endDate} " +
            "</if>" +
            "<if test='type != null and type != 0'>" +
            "  AND type = #{type} " +
            "</if>" +
            "ORDER BY date_his DESC, CAST(change_details AS DECIMAL(10,2)) DESC " +
            "LIMIT #{offset}, #{pageSize} " +
            "</script>")
    List<MonitoringEntity> selectLimitUpDownList(@Param("startDate") String startDate,
                                                 @Param("endDate") String endDate,
                                                 @Param("type") Integer type,
                                                 @Param("offset") Integer offset,
                                                 @Param("pageSize") Integer pageSize);

    /**
     * 查询总记录数
     */
    @Select("<script>" +
            "SELECT COUNT(*) FROM monitoring " +
            "WHERE is_deleted = 0 " +
            "  AND type IN (1,2,3,4,5,6) " +
            "<if test='startDate != null and startDate != \"\"'>" +
            "  AND date_his &gt;= #{startDate} " +
            "</if>" +
            "<if test='endDate != null and endDate != \"\"'>" +
            "  AND date_his &lt;= #{endDate} " +
            "</if>" +
            "<if test='type != null and type != 0'>" +
            "  AND type = #{type} " +
            "</if>" +
            "</script>")
    Long selectCount(@Param("startDate") String startDate,
                     @Param("endDate") String endDate,
                     @Param("type") Integer type);
}
