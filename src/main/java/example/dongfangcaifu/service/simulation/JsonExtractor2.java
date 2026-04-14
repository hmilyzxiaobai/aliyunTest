package example.dongfangcaifu.service.simulation;


import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

public class JsonExtractor2 {
    public static List<String> analysis(String json) {
//        List<String> objects = new ArrayList<>();
//        objects.add("");
//        return objects;
//    }
   // public static void main(String[] json1) {
 //       String json = "{\"data\":{\"code\":\"600593\",\"market\":1,\"decimal\":2,\"prePrice\":41.46,\"details\":[\"14:56:32,41.39,22,8,1\",\"14:56:35,41.40,144,52,2\"]}}";
        List<String> res =new ArrayList<>();
        try {
            ObjectMapper mapper = new ObjectMapper();
            StockData data = mapper.readValue(json, StockData.class);
           // StockData data = stockData.getData();
//            System.out.println("股票代码: " + data.getCode());
//            System.out.println("前收盘价: " + data.getPrePrice());
//            System.out.println("明细数量: " + data.getDetails().size());
            // 处理明细数据
            for (String detail : data.getDetails()) {
              //  System.out.println("明细: " + detail);
                res.add(detail.split(",")[1]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }
}
