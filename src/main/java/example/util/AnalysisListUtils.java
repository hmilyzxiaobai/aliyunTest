package example.util;

import example.dongfangcaifu.utils.FloatUtils;
import org.springframework.util.CollectionUtils;

import java.util.List;

public class AnalysisListUtils {

    public static boolean analysis(List<String> list){
        if (CollectionUtils.isEmpty(list)){
            return false;
        }
        int size = list.size();
        float high = 0;
        for(String s:list){
            high=Math.max(high, FloatUtils.stringToFloat(s));
        }
        for(int i=size/2;i<size;i++){
            if (high==FloatUtils.stringToFloat(list.get(i))){
                return true;
            }
        }
        return false;
    }
}
