package example.dongfangcaifu.service.surveillance;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import example.dongfangcaifu.httpUtils.HttpRefererEnum;
import example.dongfangcaifu.httpUtils.HttpUrlUtils;
import example.dongfangcaifu.service.CompanyHistoryService;
import example.dongfangcaifu.service.simulation.JsonExtractor2;
import example.dongfangcaifu.utils.FloatUtils;
import example.util.AnalysisListUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class MonitoringUtilsService {
    static boolean flag = false;
    static Float capitalNow;
    @Autowired
    private CompanyHistoryService companyHistoryService;
    @Autowired
    private HttpUrlUtils httpUrlUtils;
    public void searchData(String companyName, String code, String price) {
        float priceNow = readNowData(code);
        Float setPrice = FloatUtils.stringToFloat(price);
        if (priceNow < setPrice) {
            String s = companyHistoryService.readThreeInfo(code);
            //直接报警
            if (capitalNow > 5000000) {
                log.info("\n股票名称：{}股票代码：{}，当前价格为：{}，达到触底并且流入资金大于500W，请重点关注板块问题，请查看并关注资金流入情况,历史信息为：{}", companyName, code, priceNow,s);
            }
            if (flag) {
                log.info("\n股票名称：{}股票代码：{}，当前价格为：{}，低于预期值，并在上涨中了，请查看,历史信息为：{}", companyName, code, priceNow,s);
            } else {
                log.info("\n股票名称：{}股票代码：{}，当前价格为：{}，低于预期值，正在进行触底，请查看,历史信息为：{}", companyName, code, priceNow,s);
            }
        } else {
            //计算 如果小于百分之5 告警
            if (setPrice * 105 > priceNow * 100) {
                String s = companyHistoryService.readThreeInfo(code);
                if (Objects.isNull(capitalNow)) {
                    log.info("is null");
                }
//
                if (capitalNow > 10000000) {
                    log.info("\n股票名称：{}股票代码：{}，当前价格为：{}，达到触底的百分之5，并且流入资金大于1000W，请重点关注板块问题，请查看并关注资金流入情况,历史信息为：{}", companyName, code,priceNow, s);
                } else
                    log.info("\n股票名称：{}股票代码：{}，当前价格为：{}，达到触底的百分之5，请查看并关注资金流入情况,历史信息为：{}", companyName, code, priceNow,s);
            }
        }

    }


    private Float readNowData(String code) {
        String nowPrice = "0";
        String prePrice = "0";
        try {
            // 设置要发送请求的URL
            String urlString =
                    "https://push2.eastmoney.com/api/qt/stock/details/get?fields1=f1,f2,f3,f4&fields2=f51,f52,f53,f54,f55&fltt=2&cb=jQuery3510259253290888554_1767067259427&pos=-11&secid=1." + code + "&ut=fa5fd1943c7b386f172d6893dbfba10b&wbp2u=%7C0%7C0%7C0%7Cweb&_=1767067259428";
            URL url = new URL(urlString);
            // 打开连接
            HttpURLConnection connection = httpUrlUtils.httpBuildUrlUtils(url,code, HttpRefererEnum.HUDATA);
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
            String dataAll = response.toString();
            String[] splitOne = dataAll.split("\\(");
            String[] splitTwo = splitOne[1].split("\\)");
            String data = splitTwo[0];

            JSON parse = JSONUtil.parse(data);
            Object analysisData = parse.getByPath("data");
            if (JSONUtil.isNull(analysisData)) {
                // 换成0
                urlString =
                        "https://push2.eastmoney.com/api/qt/stock/details/get?fields1=f1,f2,f3,f4&fields2=f51,f52,f53,f54,f55&fltt=2&cb=jQuery3510259253290888554_1767067259427&pos=-11&secid=0." + code + "&ut=fa5fd1943c7b386f172d6893dbfba10b&wbp2u=%7C0%7C0%7C0%7Cweb&_=1767067259428";
                url = new URL(urlString);
                // 打开连接
                HttpURLConnection connection1 = (HttpURLConnection) url.openConnection();
                // 设置请求方法为GET
                connection1.setRequestMethod("GET");
                // 获取响应内容
                BufferedReader in0 = new BufferedReader(new InputStreamReader(connection1.getInputStream()));
                StringBuilder response0 = new StringBuilder();
                String inputLine0;
                while ((inputLine0 = in0.readLine()) != null) {
                    response0.append(inputLine0);
                }
                in0.close();
                // 输出响应内容
                // System.out.println("响应内容：");
                dataAll = response0.toString();
                splitOne = dataAll.split("\\(");
                splitTwo = splitOne[1].split("\\)");
                data = splitTwo[0];
                parse = JSONUtil.parse(data);
                analysisData = parse.getByPath("data");
                getCapital("0", code);
            } else {
                getCapital("1", code);
            }

            // 做分析 这是昨天的
            prePrice = getString(JSONUtil.parse(analysisData).getByPath("prePrice"));
            List<String> analysis = JsonExtractor2.analysis(getString(analysisData));
            if (CollectionUtils.isEmpty(analysis)) {
                return FloatUtils.stringToFloat(prePrice);
            }
            // 这是今天的 当前价格
             nowPrice = analysis.get(analysis.size() - 1);

            flag= AnalysisListUtils.analysis(analysis);
//            if (FloatUtils.stringToFloat(prePrice) < FloatUtils.stringToFloat(nowPrice)) {
//                //上涨
//                flag = true;
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return FloatUtils.stringToFloat(nowPrice);
    }

    private static String getString(Object analysisData) {
        return analysisData.toString();
    }

    // 拿到资金流入情况
    private void getCapital(String type, String code) {
        try {
            String urlString =
                    "https://push2.eastmoney.com/api/qt/stock/get?invt=2&fltt=1&cb=jQuery35102821228802484421_1767857879669&fields=f138%2Cf139%2Cf141%2Cf142%2Cf144%2Cf145%2Cf147%2Cf148%2Cf137%2Cf193%2Cf152%2Cf140%2Cf194%2Cf143%2Cf195%2Cf146%2Cf196%2Cf149%2Cf197&secid=" + type + "." + code + "&ut=fa5fd1943c7b386f172d6893dbfba10b&wbp2u=%7C0%7C0%7C0%7Cweb&dect=1&_=1767857879670";
            URL url = new URL(urlString);
            // 打开连接
            HttpURLConnection connection = httpUrlUtils.httpBuildUrlUtils(url,code,HttpRefererEnum.HUDATA);
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
            String dataAll = response.toString();
            String[] splitOne = dataAll.split("\\(");
            String[] splitTwo = splitOne[1].split("\\)");
            String dataRes = splitTwo[0];
            JSON parse = JSONUtil.parse(dataRes);
            String string = JSONUtil.parse(parse.getByPath("data")).getByPath("f137").toString();
            capitalNow = FloatUtils.stringToFloat(string);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

