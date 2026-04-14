package example.taobao.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DataGet {

    public static void main(String [] args){
        try {
            String urlString = "https://h5api.m.taobao.com/h5/mtop.relationrecommend.wirelessrecommend.recommend/2.0/?jsv=2.7.2&appKey=12574478&t=1742786589352&sign=a8a85f35c30eddcafc0a57bef0d2f59d&api=mtop.relationrecommend.wirelessrecommend.recommend&v=2.0&type=jsonp&dataType=jsonp&callback=mtopjsonp4&data=%7B%22appId%22%3A%2243356%22%2C%22params%22%3A%22%7B%5C%22device%5C%22%3A%5C%22HMA-AL00%5C%22%2C%5C%22isBeta%5C%22%3A%5C%22false%5C%22%2C%5C%22grayHair%5C%22%3A%5C%22false%5C%22%2C%5C%22from%5C%22%3A%5C%22nt_history%5C%22%2C%5C%22brand%5C%22%3A%5C%22HUAWEI%5C%22%2C%5C%22info%5C%22%3A%5C%22wifi%5C%22%2C%5C%22index%5C%22%3A%5C%224%5C%22%2C%5C%22rainbow%5C%22%3A%5C%22%5C%22%2C%5C%22schemaType%5C%22%3A%5C%22auction%5C%22%2C%5C%22elderHome%5C%22%3A%5C%22false%5C%22%2C%5C%22isEnterSrpSearch%5C%22%3A%5C%22true%5C%22%2C%5C%22newSearch%5C%22%3A%5C%22false%5C%22%2C%5C%22network%5C%22%3A%5C%22wifi%5C%22%2C%5C%22subtype%5C%22%3A%5C%22%5C%22%2C%5C%22hasPreposeFilter%5C%22%3A%5C%22false%5C%22%2C%5C%22prepositionVersion%5C%22%3A%5C%22v2%5C%22%2C%5C%22client_os%5C%22%3A%5C%22Android%5C%22%2C%5C%22gpsEnabled%5C%22%3A%5C%22false%5C%22%2C%5C%22searchDoorFrom%5C%22%3A%5C%22srp%5C%22%2C%5C%22debug_rerankNewOpenCard%5C%22%3A%5C%22false%5C%22%2C%5C%22homePageVersion%5C%22%3A%5C%22v7%5C%22%2C%5C%22searchElderHomeOpen%5C%22%3A%5C%22false%5C%22%2C%5C%22search_action%5C%22%3A%5C%22initiative%5C%22%2C%5C%22sugg%5C%22%3A%5C%22_4_1%5C%22%2C%5C%22sversion%5C%22%3A%5C%2213.6%5C%22%2C%5C%22style%5C%22%3A%5C%22list%5C%22%2C%5C%22ttid%5C%22%3A%5C%22600000%40taobao_pc_10.7.0%5C%22%2C%5C%22needTabs%5C%22%3A%5C%22true%5C%22%2C%5C%22areaCode%5C%22%3A%5C%22CN%5C%22%2C%5C%22vm%5C%22%3A%5C%22nw%5C%22%2C%5C%22countryNum%5C%22%3A%5C%22156%5C%22%2C%5C%22m%5C%22%3A%5C%22pc_sem%5C%22%2C%5C%22page%5C%22%3A%5C%221%5C%22%2C%5C%22n%5C%22%3A48%2C%5C%22q%5C%22%3A%5C%22%25E7%2581%25AF%25E6%25B3%25A1%25E8%259E%25BA%25E5%258F%25A3led%25E7%2581%25AF%5C%22%2C%5C%22qSource%5C%22%3A%5C%22url%5C%22%2C%5C%22pageSource%5C%22%3A%5C%22tbpc.pc_sem_alimama%2Fa.201856.d13%5C%22%2C%5C%22tab%5C%22%3A%5C%22all%5C%22%2C%5C%22pageSize%5C%22%3A48%2C%5C%22totalPage%5C%22%3A100%2C%5C%22totalResults%5C%22%3A4800%2C%5C%22sourceS%5C%22%3A%5C%220%5C%22%2C%5C%22sort%5C%22%3A%5C%22_coefp%5C%22%2C%5C%22bcoffset%5C%22%3A%5C%22%5C%22%2C%5C%22ntoffset%5C%22%3A%5C%22%5C%22%2C%5C%22filterTag%5C%22%3A%5C%22%5C%22%2C%5C%22service%5C%22%3A%5C%22%5C%22%2C%5C%22prop%5C%22%3A%5C%22%5C%22%2C%5C%22loc%5C%22%3A%5C%22%5C%22%2C%5C%22start_price%5C%22%3Anull%2C%5C%22end_price%5C%22%3Anull%2C%5C%22startPrice%5C%22%3Anull%2C%5C%22endPrice%5C%22%3Anull%2C%5C%22itemIds%5C%22%3Anull%2C%5C%22p4pIds%5C%22%3Anull%2C%5C%22categoryp%5C%22%3A%5C%22%5C%22%2C%5C%22myCNA%5C%22%3A%5C%22C79eIPeRCEICAd5VoPaB3f3R%5C%22%2C%5C%22clk1%5C%22%3A%5C%229fee493a33427eafd038b1f8e6d39018%5C%22%2C%5C%22refpid%5C%22%3A%5C%22mm_26632258_3504122_32538762%5C%22%7D%22%7D";
            URL url = new URL(urlString);
            // 打开连接
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            // 设置请求方法为GET
            connection.setRequestMethod("GET");
            // 获取响应内容
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            // 输出响应内容
            //System.out.println("响应内容：");
            String dataAll = response.toString();
            System.out.println(dataAll);
        }catch (Exception e){

        }
    }
}
