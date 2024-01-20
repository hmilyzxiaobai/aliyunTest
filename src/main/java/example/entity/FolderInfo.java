package example.entity;


import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("folder_info")
public class FolderInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */

    private String  id;

    private String username;
    private String folderName;
    private String folderRemark;
    private Integer sort;
    /**
     * 修改时间
     */
    @TableField(insertStrategy = FieldStrategy.NEVER,updateStrategy = FieldStrategy.NEVER)
    private Date updateTime;
    /**
     * 修改时间
     */
    @TableField(insertStrategy = FieldStrategy.NEVER,updateStrategy = FieldStrategy.NEVER)
    private Date createTime;
}
