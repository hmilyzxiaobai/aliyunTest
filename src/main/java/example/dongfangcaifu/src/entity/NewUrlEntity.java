package example.dongfangcaifu.src.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper=false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("new_url")
public class NewUrlEntity {

    private static final long serialVersionUID = 1L;

    public static final String URL = "url";
    public static final String TITLE = "title";
    public static final String CONTENT = "content";
    public static final String CONTENT_TYPE = "content_type";
    public static final String ANALYSE_CONTENT = "analyse_content";

    public static final String IS_DELETED = "is_deleted";

    private String url;
    private String title;
    private String content;
    private String contentType;
    private String analyseContent;


    @Override
    public String toString(){

        return "标题为："+title+"\n"
                +"url为："+url+"\n";
    }
}
